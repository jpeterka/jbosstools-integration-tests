package org.jboss.tools.openshift.ui.bot.test.application.create;

import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.wizard.v2.ApplicationCreator;
import org.junit.Test;

/**
 * Test capabilities of creating a new application via OpenShift Explorer.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID401CreateNewApplicationViaExplorerTest {

	public static String applicationName = "diy" + System.currentTimeMillis();
	
	@Test
	public void testCreateNewApplicationViaExplorer() {
		ApplicationCreator newApplicationTemplate = new ApplicationCreator(DatastoreOS2.USERNAME, DatastoreOS2.SERVER,
				DatastoreOS2.DOMAIN, false);
		
		// Assertions are done inside of create method
		newApplicationTemplate.createSimpleApplicationOnBasicCartridges(
				OpenShiftLabel.Cartridge.DIY, applicationName, false, true, true);
	}
}
