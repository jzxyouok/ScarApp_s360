package com.zero2ipo.SDK.demo;

import com.zero2ipo.SDK.config.AppConfig;
import com.zero2ipo.SDK.lib.MESSAGEXsend;
import com.zero2ipo.SDK.utils.ConfigLoader;

public class MessageXSendDemo {

	public static void main(String[] args) {
		AppConfig config = ConfigLoader.load(ConfigLoader.ConfigType.Message);
		
		MESSAGEXsend submail = new MESSAGEXsend(config);
		submail.addTo("13717625140");
		submail.setProject(config.getProjectId().toString());
		submail.addVar("code", "a你好aaaa");
		submail.xsend();
	}	
}
