package com.cloud.testing.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.cloud.testing.constant.SauceLabsConstant;
import com.cloud.testing.data.CloudTestingConfiguration;

import junit.framework.Assert;

public class SauceLabsUtil {
	
	public static WebDriver getDriver(CloudTestingConfiguration conf) {
		DesiredCapabilities cap = getCapability(conf);
		try {
			return new RemoteWebDriver(new URL(SauceLabsConstant.SAUCE_URL), cap);			
		} catch (MalformedURLException e) {
			return null;
		}
	}
	
	public static DesiredCapabilities getCapability(CloudTestingConfiguration conf) {
		DesiredCapabilities capabilities = null;
		
        if(conf.getBrowser() == SauceLabsConstant.Browser.EDGE) {
        	capabilities = DesiredCapabilities.edge();
        } else if(conf.getBrowser() == SauceLabsConstant.Browser.FIREFOX) {
        	capabilities = DesiredCapabilities.firefox();
        } else if(conf.getBrowser() == SauceLabsConstant.Browser.CHROME) {
        	capabilities = DesiredCapabilities.chrome();
        } else if(conf.getBrowser() == SauceLabsConstant.Browser.SAFARI) {
        	capabilities = DesiredCapabilities.safari();
        } else if(conf.getBrowser() == SauceLabsConstant.Browser.IE) {
        	capabilities = DesiredCapabilities.internetExplorer();
        }
        Assert.assertTrue(capabilities != null);
        
        capabilities.setCapability("platform", conf.getPlatform().getName());
        capabilities.setCapability("version", conf.getBrowser().getVersion());
        capabilities.setCapability("username", SauceLabsConstant.USERNAME);
        capabilities.setCapability("accessKey", SauceLabsConstant.ACCESS_KEY);
        
        capabilities.setCapability("name", conf.getTestName());
        capabilities.setCapability("build", conf.getBuildName());
        // TODO Tag는 Service(HRIS, Payroll, ...), Server(Alpha, Beta, ..), Category(Onboarding,...)등의 기본 정보를 담는다.
        capabilities.setCapability("tags", conf.getTags());
        
        capabilities.setCapability("extendedDebugging", true);
        
        capabilities.setCapability("maxDuration", SauceLabsConstant.MAX_DURATION);
        capabilities.setCapability("commandTimeout", SauceLabsConstant.COMMAND_TIMEOUT);
        capabilities.setCapability("idleTimeout", SauceLabsConstant.IDLE_TIMEOUT);
		
		return capabilities;		
	}
}
