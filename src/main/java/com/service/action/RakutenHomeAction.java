package com.service.action;

import com.base.Environment;
import com.base.action.BaseAction;
import com.service.page.RakutenHomePage;

public class RakutenHomeAction extends BaseAction {

	private RakutenHomePage rakutenHomePage;
	
	public RakutenHomeAction(Environment env) {
		super(env);
		rakutenHomePage = new RakutenHomePage(env);
	}

	public RakutenHomePage getRakutenHomePage() {
		return rakutenHomePage;
	}
}