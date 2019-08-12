package com.cloud.testing.constant;

import com.base.util.StringUtil;

public class BrowserStackConstant {
	public static final String USERNAME = "wonderjung1";
	public static final String ACCESS_KEY = "eidwzyAeWRq1E5pQbuGN";
	public static final String HUB_URL = String.format("http://%s:%s@hub-cloud.browserstack.com/wd/hub", USERNAME, ACCESS_KEY);
	
	public static final int MAX_DURATION = 3600;
	public static final int COMMAND_TIMEOUT = 600;
	public static final int IDLE_TIMEOUT = 1000;
	
	// https://wiki.saucelabs.com/display/DOCS/Platform+Configurator#/
	public static enum Browser implements CloudTestingBrowser {
		CHROME("Chrome", "75.0"), 
		FIREFOX("firefox", "68.0"), 
		SAFARI("safari", "12.1"), 
		IE("IE", "11.0"), 
		EDGE("Edge", "18.0");
		
		private String name;
		public String getName() {
			return name;
		}
		
		private String version;
		public String getVersion() {
			return version;
		}		
		
		Browser(String value, String version) {
			this.name = value;
			this.version = version;
		}
		
		public static Browser get(String value) {
			for (Browser browser : Browser.values()) {
				if(StringUtil.equalsAfterTrim(browser.getName(), value)) {
					return browser;
				}
			}
			return null;
		}		
	}
	
	// https://www.browserstack.com/automate/testng
	public static enum Platform implements CloudTestingPlatform {
		MAC("OS X", "Mojave"), WINDOWS("Windows", "10");
		
		private String name;
		public String getName() {
			return name;
		}
		
		private String version;
		public String getVersion() {
			return version;
		}
		
		public String getPlatform() {
			return String.format("%s %s", name, version);
		}
		
		Platform(String name, String version) {
			this.name = name;
			this.version = version;
		}
		
		public static Platform get(String value) {
			for (Platform platform : Platform.values()) {
				if(StringUtil.contains(platform.getName().toLowerCase(), value)) {
					return platform;
				}
			}
			return null;
		}		
	}	
}
