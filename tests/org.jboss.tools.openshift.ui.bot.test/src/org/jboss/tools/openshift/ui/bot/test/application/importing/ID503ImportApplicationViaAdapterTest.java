package org.jboss.tools.openshift.ui.bot.test.application.importing;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.link.DefaultLink;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test capabilities of importing an application via OpenShift server adapter.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID503ImportApplicationViaAdapterTest {

	private String applicationName = "diy" + System.currentTimeMillis();
	
	@Before
	public void createApplicationDeleteProject() {
		ID501ImportApplicationViaExplorerTest.createApplicationWithoutServerAdapterAndProject(applicationName);
	}
	
	@Test
	public void testImportApplicationViaServerAdapter() {
		ServersView servers = new ServersView();
		servers.open();
		
		try {
			new DefaultTree();
			new ContextMenu("New", "Server").select();
		} catch (CoreLayerException ex) {
			// There is no server, so create server via link
			new DefaultLink("No servers are available. Click this link to create a new server...").click();
		}
		
		new DefaultShell("New Server");
		
		AbstractWait.sleep(TimePeriod.getCustom(5));
		new DefaultTreeItem("OpenShift", "OpenShift 2 Server Adapter").select();
		
		new WaitUntil(new WidgetIsEnabled(new NextButton()), TimePeriod.LONG);
		
		new NextButton().click();
		
		for (String connection: new LabeledCombo("Connection:").getItems()) {
			if (connection.contains(DatastoreOS2.USERNAME)) {
				new LabeledCombo("Connection:").setSelection(connection);
				break;
			}
		}
		
		new DefaultLink("Import this application").click();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.IMPORT_APPLICATION), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.IMPORT_APPLICATION);
		
		new FinishButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.IMPORT_APPLICATION), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		assertTrue("There is no project in project explorer. Importing was no successfull.",
				new ProjectExplorer().containsProject(applicationName));
	}
	
	@After
	public void deleteApplication() {
		new DeleteUtils(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, applicationName,
				applicationName).perform();
	}
}
