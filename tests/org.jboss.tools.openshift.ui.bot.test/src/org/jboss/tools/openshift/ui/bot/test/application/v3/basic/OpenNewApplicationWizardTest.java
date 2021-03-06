package org.jboss.tools.openshift.ui.bot.test.application.v3.basic;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.wizard.v3.NewOpenShift3ApplicationWizard;
import org.junit.Test;

public class OpenNewApplicationWizardTest {
	
	@Test
	public void testOpenNewApplicationWizardViaCentral() {
		new NewOpenShift3ApplicationWizard().openWizardFromCentral();
		closeWizard();
	}
	
	@Test
	public void testOpenNewApplicationWizardViaShellMenu() {
		new NewOpenShift3ApplicationWizard().openWizardFromShellMenu();
		closeWizard();
	}
	
	@Test
	public void testOpenNewApplicationWizardViaOpenShiftExplorer() {
		new NewOpenShift3ApplicationWizard().openWizardFromExplorer();
		closeWizard();
	}
	
	private void closeWizard() {
		new CancelButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning());
	}
}
