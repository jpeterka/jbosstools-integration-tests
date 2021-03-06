package org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly10web;

import java.util.Arrays;
import java.util.List;

import org.jboss.tools.runtime.as.ui.bot.test.RuntimeProperties;
import org.jboss.tools.runtime.as.ui.bot.test.entity.Runtime;
import org.jboss.tools.runtime.as.ui.bot.test.template.DetectRuntimeTemplate;

public class DetectWildFly10Web extends DetectRuntimeTemplate {

	public static final String SERVER_ID = "wildfly-web-10.0";
	
	public static final String SERVER_NAME = "Wildfly-Web 10.0";
	
	@Override
	protected String getPathID() {
		return SERVER_ID;
	}
	
	@Override
	protected List<Runtime> getExpectedRuntimes() {
		Runtime expectedServer = new Runtime();
		expectedServer.setName(SERVER_NAME);
		expectedServer.setVersion("10.0");
		expectedServer.setType("Wildfly-Web");
		expectedServer.setLocation(RuntimeProperties.getInstance().getRuntimePath(SERVER_ID));
		return Arrays.asList(expectedServer);
	}
}
