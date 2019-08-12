package com.cloud.testing.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.cloud.testing.constant.LambdaTestConstant;
import com.cloud.testing.data.CloudTestingConfiguration;

public class LambdaTestUtil {
	
	public static WebDriver getDriver(CloudTestingConfiguration conf) {
		DesiredCapabilities cap = getCapability(conf);
		try {
			return new RemoteWebDriver(new URL(LambdaTestConstant.HUB_URL), cap);			
		} catch (MalformedURLException e) {
			return null;
		}
	}
	
	public static DesiredCapabilities getCapability(CloudTestingConfiguration conf) {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		
        capabilities.setCapability("username", LambdaTestConstant.USERNAME);
        capabilities.setCapability("accessKey", LambdaTestConstant.ACCESS_KEY);
        
        capabilities.setCapability("build", conf.getBuildName());
        capabilities.setCapability("name", conf.getTestName());
        capabilities.setCapability("platform", conf.getPlatform().getName());
        if(conf.getBrowser() == LambdaTestConstant.Browser.EDGE) {
        	capabilities.setCapability("browserName", LambdaTestConstant.Browser.EDGE.getName());
        } else if(conf.getBrowser() == LambdaTestConstant.Browser.FIREFOX) {
        	capabilities.setCapability("browserName", LambdaTestConstant.Browser.FIREFOX.getName());
        } else if(conf.getBrowser() == LambdaTestConstant.Browser.CHROME) {
        	capabilities.setCapability("browserName", LambdaTestConstant.Browser.CHROME.getName());
        } else if(conf.getBrowser() == LambdaTestConstant.Browser.IE) {
        	capabilities.setCapability("browserName", LambdaTestConstant.Browser.IE.getName());
        	capabilities.setCapability("ie.compatibility",11001);
        }
        capabilities.setCapability("version", conf.getBrowser().getVersion());
        capabilities.setCapability("visual",true);
		
		return capabilities;		
	}
}
