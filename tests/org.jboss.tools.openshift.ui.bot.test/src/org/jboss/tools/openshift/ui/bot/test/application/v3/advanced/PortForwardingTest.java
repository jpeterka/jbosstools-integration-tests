package org.jboss.tools.openshift.ui.bot.test.application.v3.advanced;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.tools.openshift.reddeer.condition.AmountOfResourcesExists;
import org.jboss.tools.openshift.reddeer.condition.ResourceExists;
import org.jboss.tools.openshift.reddeer.enums.Resource;
import org.jboss.tools.openshift.reddeer.enums.ResourceState;
import org.jboss.tools.openshift.reddeer.requirement.OpenShiftCommandLineToolsRequirement.OCBinary;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.TestUtils;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.OpenShiftProject;
import org.jboss.tools.openshift.reddeer.view.OpenShiftResource;
import org.jboss.tools.openshift.ui.bot.test.application.v3.create.AbstractCreateApplicationTest;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

@OCBinary
public class PortForwardingTest extends AbstractCreateApplicationTest {

	@BeforeClass
	public static void setUpOCBinaryAndWaitForApplication() {
		TestUtils.setUpOcBinary();
		
		new WaitUntil(new ResourceExists(Resource.POD, "eap-app-1-build",
				ResourceState.SUCCEEDED), TimePeriod.getCustom(600),
				true, TimePeriod.getCustom(7));
		
		new WaitUntil(new AmountOfResourcesExists(Resource.POD, 2), TimePeriod.VERY_LONG,
				true, TimePeriod.getCustom(7));
	}
	
	@Test
	public void testPortForwardingButtonsAccessibility() {		
		openPortForwardingDialog();
		
		PushButton startAllButton = new PushButton(OpenShiftLabel.Button.START_ALL);
		PushButton stopAllButton = new PushButton(OpenShiftLabel.Button.STOP_ALL);
		
		assertTrue("Button Start All should be enabled at this point.", startAllButton.isEnabled());
		assertFalse("Button Stop All should be disabled at this point.", stopAllButton.isEnabled());
		
		startAllButton.click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		assertFalse("Button Start All should be disabled at this point.", startAllButton.isEnabled());
		assertTrue("Button Stop All should be enabled at this point.", stopAllButton.isEnabled());
		
		stopAllButton.click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		assertTrue("Button Start All should be enabled at this point.", startAllButton.isEnabled());
		assertFalse("Button Stop All should be disabled at this point.", stopAllButton.isEnabled());
	}
	
	@Test
	public void testFreePortsForPortForwarding() {
		openPortForwardingDialog();
		CheckBox checkBox = new CheckBox(OpenShiftLabel.TextLabels.FIND_FREE_PORTS);
		Table table = new DefaultTable();
		
		assertTrue("Default port should be used for ping on first opening of Port forwarding dialog.", 
				table.getItem("ping").getText(1).equals("8888"));
		assertTrue("Default port should be used for http on first opening of Port forwarding dialog.", 
				table.getItem("http").getText(1).equals("8080"));
		
		checkBox.click();
		
		assertFalse("Free port port should be used for ping at this point.", 
				table.getItem("ping").getText(1).equals("8888"));
		assertFalse("Free port should be used for http at this point.", 
				table.getItem("http").getText(1).equals("8080"));
		
		checkBox.click();
		
		assertTrue("Default port should be used for ping at this point.", 
				table.getItem("ping").getText(1).equals("8888"));
		assertTrue("Default port should be used for http at this point.", 
				table.getItem("http").getText(1).equals("8080"));
		
		
	}
	
	@After
	public void closePortForwardingShell() {
		PushButton stopAllButton = new PushButton(OpenShiftLabel.Button.STOP_ALL);
		if (stopAllButton.isEnabled()) {
			stopAllButton.click();
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG, false);
		}
		
		new DefaultShell(OpenShiftLabel.Shell.APPLICATION_PORT_FORWARDING).close();
	}
	
	private void openPortForwardingDialog() {
		getPodOfEAPApplication().select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.PORT_FORWARD).select();
		
		new DefaultShell(OpenShiftLabel.Shell.APPLICATION_PORT_FORWARDING);
	}
	
	public static OpenShiftResource getPodOfEAPApplication() {
		OpenShiftExplorerView explorer  = new OpenShiftExplorerView();
		explorer.activate();
		
		OpenShiftProject project = explorer.getOpenShift3Connection().
				getProject();
		OpenShiftResource pod = null;
			
		while (pod == null) {
			List<OpenShiftResource> pods = project.getOpenShiftResources(Resource.POD);
			
			for (OpenShiftResource resource: pods) {
				if (!resource.getName().equals("eap-app-1-build") && !resource.getName().equals("eap-app-1-deploy")) {
					pod = resource;
					break;
				}
			}
		}
		
		String podName= pod.getName();
		
		new WaitUntil(new ResourceExists(Resource.POD, pod.getName(), ResourceState.RUNNING), 
				TimePeriod.LONG, true, TimePeriod.getCustom(5));
		
		TreeItem podResourceTreeItem = project.getTreeItem().getItem(Resource.POD.toString());
		
		return new OpenShiftResource(treeViewerHandler.getTreeItem(podResourceTreeItem, podName));
	}
}
