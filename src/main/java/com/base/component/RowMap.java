package com.base.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import com.base.Environment;
import com.base.command.BaseCommand;
import com.base.constant.BaseConstant.Timeout;
import com.base.util.ElementUtil;
import com.base.util.StringUtil;

public class RowMap {
	private BaseCommand baseCommand;
	private String queryList;
	private String queryTitle;
	public RowMap(Environment env, String queryList, String queryTitle) {
		this.baseCommand = env.getBaseCommand();
		this.queryList = queryList;
		this.queryTitle = queryTitle;
	}
	
	private Map<String, SearchStep> getMap() {
		return baseCommand.getSearchStepMap(queryList, queryTitle, 0, false);
	}
	
	public SearchStep getFirstRow() {
		return getRow(0);
	}
	
	public SearchStep getRow(int idx) {
		if(idx < getRowCnt()) {
			Map<String, SearchStep> map = getMap();
			List<String> keyList = getKeyList();
			String key = keyList.get(idx);
			
			return map.get(key);
		}
		
		return null;
	}

	public SearchStep getRow(String title) {
		Map<String, SearchStep> map = getMap();
		return map.get(title.trim());
	}
	
	public SearchStep getRowContainsTitle(String title) {
		return getRowContainsTitle(title, false);
	}
	
	public SearchStep getRowContainsTitleAfterRemovingDecoration(String title) {
		return getRowContainsTitle(title, true);
	}
	
	private SearchStep getRowContainsTitle(String title, boolean isRemovingDecoration) {
		Map<String, SearchStep> map = getMap();
		List<String> keyList = getKeyList();
		for (String key : keyList) {
			String keyFiltered = (isRemovingDecoration)?ElementUtil.removeDecoration(key):key;
			if(StringUtil.contains(keyFiltered, title.trim())) {
				return map.get(key); 
			}
		}
		return null;
	}
	
	
	public SearchStep getRowContainsExactTitle(String title) {
		Map<String, SearchStep> map = getMap();
		List<String> keyList = getKeyList();
		for (String key : keyList) {
			String keyFiltered = ElementUtil.removeDecoration(key);
			if(StringUtil.equalsAfterTrim(keyFiltered, title.trim())) {
				return map.get(key); 
			}
		}
		return null;
	}
	
	public int getRowCnt() {
		return getMap().size();
	}
	
	public void waitUntilHasRow(String name, Timeout timeout) {
		baseCommand.waitUntil(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				SearchStep searchStep = getRowContainsTitleAfterRemovingDecoration(name);
				return searchStep != null;
			}
		}, timeout);
	}
	
	public void waitUntilHasRowDetached(String name, Timeout timeout) {
		baseCommand.waitUntil(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				SearchStep searchStep = getRowContainsTitleAfterRemovingDecoration(name);
				return searchStep == null;
			}
		}, timeout);
	}
	
	public void waitUntilHasRowCnt(int cnt, Timeout timeout) {
		baseCommand.waitUntil(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) { 
				int curRowCnt = getRowCnt();
				return curRowCnt == cnt;
			}
		}, timeout);
	}
	
	public void waitUntilHasRowCntMoreThan(int cnt, Timeout timeout) {
		baseCommand.waitUntil(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) { 
				int curRowCnt = getRowCnt();
				return curRowCnt > cnt;
			}
		}, timeout);
	}
	
	public boolean has(String title) {
		SearchStep row = getRow(title);
		return row != null;
	}
	
	public List<String> getKeyList() {
		List<String> list = new ArrayList<String>();
		Iterator<String> keys = getMap().keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			list.add(key);
		}
		
		return list;
	}	
}
