package com.cloud.testing.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.cloud.testing.constant.BrowserStackConstant;
import com.cloud.testing.data.CloudTestingConfiguration;

public class BrowserStackUtil {
	
	public static WebDriver getDriver(CloudTestingConfiguration conf) {
		DesiredCapabilities cap = getCapability(conf);
		try {
			return new RemoteWebDriver(new URL(BrowserStackConstant.HUB_URL), cap);			
		} catch (MalformedURLException e) {
			return null;
		}
	}
	
	public static DesiredCapabilities getCapability(CloudTestingConfiguration conf) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browser", conf.getBrowser().getName());
        capabilities.setCapability("browser_version", conf.getBrowser().getVersion());
        capabilities.setCapability("os", conf.getPlatform().getName());
        capabilities.setCapability("os_version", conf.getPlatform().getVersion());
        capabilities.setCapability("resolution", "1920x1200");
        capabilities.setCapability("name", conf.getTestName());
        capabilities.setCapability("browserstack.debug", true);
		
		return capabilities;		
	}
}
