package com.zero2ipo.SDK.demo;

import com.zero2ipo.SDK.config.AppConfig;
import com.zero2ipo.SDK.lib.ADDRESSBOOKMail;
import com.zero2ipo.SDK.utils.ConfigLoader;

public class AddressBookMailUnSubscribe {

	public static void main(String[] args) {

		AppConfig config = ConfigLoader.load(ConfigLoader.ConfigType.Mail);
		ADDRESSBOOKMail addressbook = new ADDRESSBOOKMail(config);
		addressbook.setAddress("leo@apple.cn", "leo");
		addressbook.unsubscribe();
	}	
}
