package com.cloud.testing.constant;

import com.base.util.StringUtil;

public class LambdaTestConstant {
	public static final String USERNAME = "wonder.jung";
	public static final String ACCESS_KEY = "lcHBb6Fowso5DDhTLj94zFpGsM6ZiMsFqkIRCoK0sbpY8iKU78";
	public static final String HUB_URL = "https://wonder.jung:lcHBb6Fowso5DDhTLj94zFpGsM6ZiMsFqkIRCoK0sbpY8iKU78@hub.lambdatest.com/wd/hub";
	
	public static final int MAX_DURATION = 3600;
	public static final int COMMAND_TIMEOUT = 600;
	public static final int IDLE_TIMEOUT = 1000;
	
	// https://www.lambdatest.com/capabilities-generator/
	public static enum Browser implements CloudTestingBrowser {
		CHROME("Chrome", "76.0"),
		FIREFOX("Firefox", "67.0"),  
		IE("Internet Explorer", "11.0"), 
		EDGE("MicrosoftEdge", "18.0");
		
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
	
	// https://www.lambdatest.com/capabilities-generator/
	public static enum Platform implements CloudTestingPlatform {
		WINDOWS("windows", "10");
		
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
		
		Platform(String value, String version) {
			this.name = value;
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
