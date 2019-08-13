package com.service.page;

import com.base.Environment;
import com.base.page.BasePage;
import com.service.component.TopNavBar;
import com.service.constant.ServiceConstant.Service;

public class RakutenHomePage extends BasePage {	
	
	private final TopNavBar topNavBar;
	
	public RakutenHomePage(Environment env) {
		super(env, env.getUrl(Service.RAKUTEN,""));
		this.topNavBar = new TopNavBar(env);
	}

	public TopNavBar getTopNavBar() {
		return topNavBar;
	}
}