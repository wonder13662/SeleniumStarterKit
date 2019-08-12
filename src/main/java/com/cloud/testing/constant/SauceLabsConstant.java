package com.cloud.testing.constant;

import com.base.util.StringUtil;

public class SauceLabsConstant {
	public static final String USERNAME = "demo_mg";
	public static final String ACCESS_KEY = "f92329dc-c19d-454e-be1e-4397dde02abf";
	public static final String SAUCE_URL = "https://ondemand.saucelabs.com/wd/hub";
	
	public static final int MAX_DURATION = 3600;
	public static final int COMMAND_TIMEOUT = 600;
	public static final int IDLE_TIMEOUT = 1000;
	
	// https://wiki.saucelabs.com/display/DOCS/Platform+Configurator#/
	public static enum Browser implements CloudTestingBrowser {
		CHROME("chrome", "latest"), 
		FIREFOX("firefox", "latest"), 
		SAFARI("safari", "latest"), 
		IE("internet explorer", "latest"), 
		EDGE("microsoft edge", "latest");
		
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
	
	// https://saucelabs.com/platforms
	public static enum Platform implements CloudTestingPlatform {
		MAC("mac", "10.14"), WINDOWS("windows", "10"), LINUX("linux", "");
		
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
