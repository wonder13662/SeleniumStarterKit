package com.service.page;

import com.base.Environment;
import com.base.page.BasePage;
import com.service.constant.ServiceConstant.Service;

public class RakutenHomePage extends BasePage {	
	public RakutenHomePage(Environment env) {
		super(env, env.getUrl(Service.RAKUTEN,""));
	}
}