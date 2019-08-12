package com.cloud.testing.data;

import java.util.List;

import com.cloud.testing.constant.CloudTestingBrowser;
import com.cloud.testing.constant.CloudTestingPlatform;

public class CloudTestingConfiguration {
	
	private String testName;
	private String buildName;
	private List<String> tags;
	private CloudTestingPlatform platform;
	private CloudTestingBrowser browser;

	public CloudTestingConfiguration(String testName, String buildName, List<String> tags, CloudTestingPlatform platform, CloudTestingBrowser browser) {
		this.testName = testName;
		this.buildName = buildName;
		this.tags = tags;
		this.platform = platform;
		this.browser = browser;
	}

	public String getTestName() {
		return testName;
	}

	public String getBuildName() {
		return buildName;
	}

	public List<String> getTags() {
		return tags;
	}

	public CloudTestingPlatform getPlatform() {
		return platform;
	}

	public CloudTestingBrowser getBrowser() {
		return browser;
	}
}
