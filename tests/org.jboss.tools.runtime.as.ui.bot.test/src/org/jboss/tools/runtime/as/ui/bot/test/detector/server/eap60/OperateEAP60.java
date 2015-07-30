package org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap60;

import org.jboss.reddeer.requirements.jre.JRERequirement.JRE;
import org.jboss.tools.runtime.as.ui.bot.test.template.OperateServerTemplate;


@JRE(value=1.7, cleanup=true)
public class OperateEAP60 extends OperateServerTemplate {

	@Override
	protected String getServerName() {
		return DetectEAP60.SERVER_NAME;
	}
}
