package org.jboss.tools.runtime.as.ui.bot.test.detector.server.epp4;

import org.jboss.tools.runtime.as.ui.bot.test.RuntimeProperties;
import org.jboss.tools.runtime.as.ui.bot.test.entity.Runtime;
import org.jboss.tools.runtime.as.ui.bot.test.template.CheckSeamRuntimeTemplate;

public class CheckEPP4Seam extends CheckSeamRuntimeTemplate {

	@Override
	protected Runtime getExpectedRuntime() {
		Runtime server = new Runtime();
		server.setName("Seam seam2 2.0");
		server.setVersion("2.0");
		server.setLocation(RuntimeProperties.getInstance().getRuntimePath(DetectEPP4.SERVER_ID) + "/seam2");
		return server;
	}
}
