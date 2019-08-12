package com.base;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.base.command.BaseCommand;
import com.base.constant.BaseConstant.Browser;
import com.base.enums.EnumServerStage;
import com.base.enums.EnumServiceDomain;
import com.base.log.TestLogList;
import com.service.action.ActionFactory;

public class Environment {

	private final WebDriver driver;
	private final Logger logger;
	private final EnumServerStage serverStage;
	private final Browser browser;
	private final BaseCommand baseCommand;
	private final ActionFactory actionFactory;
	private TestLogList logList;
	
	public Environment(
			WebDriver driver, 
			Logger logger, 
			EnumServerStage serverStage,  
			Browser browser) {
		
		this.driver = driver;
		this.logger = logger;
		this.serverStage = serverStage;
		this.browser = browser;
		this.baseCommand = new BaseCommand(this);
		this.actionFactory = ActionFactory.getInstance(this);
	}

	public WebDriver getDriver() {
		return driver;
	}

	public Logger getLogger() {
		return logger;
	}
	
	public String getUrl(EnumServiceDomain serviceDomain) {
		return getUrl(serviceDomain, "");
	}
	
	public String getUrl(EnumServiceDomain serviceDomain, String path) {
		return String.format("%s/%s", serviceDomain.getDomainUrl(serverStage), path.replaceAll("^/", ""));
	}

	public Browser getBrowser() {
		return browser;
	}

	public ActionFactory getActionFactory() {
		return actionFactory;
	}

	public void info(boolean isDebug, String msg) {
		baseCommand.info(isDebug, msg);
	}
	
	public void info(String msg) {
		baseCommand.info(true, msg);
	}
	
	public BaseCommand getBaseCommand() {
		return baseCommand;
	}

	public void log(String msg) {
		System.out.println(msg);
	}

	public TestLogList getLogList() {
		Assert.assertTrue(logList != null);
		return logList;
	}

	public void setLogList(TestLogList logList) {
		this.logList = logList;
	}	
}
