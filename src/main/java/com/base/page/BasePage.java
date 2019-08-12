package com.base.page;

import org.testng.Assert;

import com.base.Environment;
import com.base.command.BaseCommand;
import com.base.constant.BaseConstant.Timeout;
import com.base.util.StringUtil;

public class BasePage {
	
	protected Environment env;
	protected BaseCommand baseCommand;
	protected PageUrl pageUrl;
	
	public BasePage(Environment env, String defaultUrl) {
		this.env = env;
		this.baseCommand = env.getBaseCommand();
		this.pageUrl = new PageUrl(defaultUrl);
	}
	
	public void open(Timeout timeout) {
		baseCommand.openUrl(pageUrl.get(), timeout);
	}
	
	public String getUrl() {
		return pageUrl.get();
	}
	
	protected void checkUrl() {
		if(pageUrl == null || StringUtil.isEmpty(pageUrl.get())) return;
		checkUrl(pageUrl.get());
	}
	
	protected void checkUrl(String expected) {
		if(pageUrl == null || StringUtil.isEmpty(pageUrl.get())) return;
		String actual = baseCommand.getCurrentUrl();
		Assert.assertEquals(actual, expected);
	}
	
}
