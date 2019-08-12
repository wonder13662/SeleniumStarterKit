package com.base.page;

import java.util.HashMap;

public class PageUrl {
	private final String DEFAULT_KEY = "DEFAULT_KEY";
	private HashMap<String, String> urlMap;
	public PageUrl(String defaultUrl) {
		urlMap = new HashMap<String, String>();
		urlMap.put(DEFAULT_KEY, defaultUrl);
	}
	
	public void add(String key, String value) {
		urlMap.put(key, value);
	}
	
	public String get(String key) {
		return urlMap.get(key);
	}
	
	public String get() {
		return urlMap.get(DEFAULT_KEY);
	}
}
