package org.jboss.tools.openshift.ui.bot.test.connection;

import static org.junit.Assert.assertFalse;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView.ServerType;
import org.junit.Test;

/**
 * Test invalid credentials usage in new connection dialog.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID104InvalidCredentialsValidationTest {
	
	@Test
	public void testInvalidCredentials() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.openConnectionShell();
		
		new DefaultShell("");
		
		new CheckBox(0).click();
		
		new LabeledCombo(OpenShiftLabel.TextLabels.SERVER_TYPE).setSelection(ServerType.OPENSHIFT_2.toString());
		
		incorrectServer();
		verify("server");
		
		incorrectUsername();
		verify("username");
		
		incorrectPassword();
		verify("password");
		
		new CancelButton().click();
	}
	
	private void incorrectServer() {
		new LabeledCombo(OpenShiftLabel.TextLabels.SERVER).setText("https://incorrect.server.url");
		new LabeledText(OpenShiftLabel.TextLabels.USERNAME).setText(DatastoreOS2.USERNAME);
		new LabeledText(OpenShiftLabel.TextLabels.PASSWORD).setText(
				System.getProperty("openshift.password"));		
	}

	private void incorrectUsername() {
		new LabeledCombo(OpenShiftLabel.TextLabels.SERVER).setText(DatastoreOS2.SERVER);
		new LabeledText(OpenShiftLabel.TextLabels.USERNAME).setText("nonexisting");
		new LabeledText(OpenShiftLabel.TextLabels.PASSWORD).setText(
				System.getProperty("openshift.password"));	
	}
	
	private void incorrectPassword() {
		new LabeledCombo(OpenShiftLabel.TextLabels.SERVER).setText(DatastoreOS2.SERVER);
		new LabeledText(OpenShiftLabel.TextLabels.USERNAME).setText(DatastoreOS2.USERNAME);
		new LabeledText(OpenShiftLabel.TextLabels.PASSWORD).setText("incorrectpwd");
	}
	
	private void verify(String credential) {
		new WaitUntil(new WidgetIsEnabled(new FinishButton()), TimePeriod.NORMAL);
		
		new FinishButton().click();
		
		new WaitUntil(new WidgetIsEnabled(new CancelButton()), TimePeriod.LONG);

		assertFalse("Finish button should not be enabled after validation incorrect "
				+ credential + ".", new FinishButton().isEnabled());
	}
}
