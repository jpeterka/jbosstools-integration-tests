package org.jboss.tools.openshift.reddeer.condition;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;

/**
 * Condition notifies about not empty console.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ConsoleHasText extends AbstractWaitCondition{

	private ConsoleView consoleView;
	
	public ConsoleHasText() {
		consoleView = new ConsoleView();
		consoleView.open();
	}
	
	@Override
	public boolean test() {
		return !consoleView.getConsoleText().isEmpty();
	}

	@Override
	public String description() {
		return "console contains text";
	}

	
}
