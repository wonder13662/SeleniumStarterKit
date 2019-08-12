package com.base.component;

import java.util.List;
import java.util.function.Function;

import org.openqa.selenium.WebElement;

import com.base.command.BaseCommand;

public class SearchStepBuilder {
	// https://jdm.kr/blog/217
	// https://developer.android.com/reference/android/app/Notification.Builder
	
	private BaseCommand baseCommand;
	private String query;
	private Function<List<WebElement>, Integer> findNthChild;
	private int foundIdx=-1;
	
	public SearchStepBuilder(BaseCommand baseCommand, String query) {
		this.baseCommand = baseCommand;
		this.query = query;
	}
	
	public SearchStepBuilder(BaseCommand baseCommand, String query, int foundIdx) {
		this.baseCommand = baseCommand;
		this.query = query;
		this.foundIdx = foundIdx;
	}
	
	public SearchStepBuilder setFindNthChild(Function<List<WebElement>, Integer> findNthChild) {
		this.findNthChild = findNthChild;
		return this;
	}
	
	public SearchStep build() {
		if(foundIdx > -1) {
			return new SearchStep(baseCommand, query, foundIdx);
		}
		return new SearchStep(baseCommand, query, findNthChild);
	}
}
