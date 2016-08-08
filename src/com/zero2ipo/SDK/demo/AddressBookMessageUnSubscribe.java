package com.zero2ipo.SDK.demo;

import com.zero2ipo.SDK.config.AppConfig;
import com.zero2ipo.SDK.lib.ADDRESSBOOKMessage;
import com.zero2ipo.SDK.utils.ConfigLoader;

public class AddressBookMessageUnSubscribe {

	public static void main(String[] args) {

		AppConfig config = ConfigLoader.load(ConfigLoader.ConfigType.Message);
		ADDRESSBOOKMessage addressbook = new ADDRESSBOOKMessage(config);
		addressbook.setAddress("186****1889");
		addressbook.unsubscribe();
	}	
}
