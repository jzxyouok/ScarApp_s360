package com.zero2ipo.SDK.lib.base;

import com.zero2ipo.SDK.entity.DataStore;


public abstract class SenderWapper {

	protected DataStore requestData = new DataStore();

	public abstract ISender getSender();
}
