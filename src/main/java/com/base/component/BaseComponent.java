package com.base.component;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.testng.Assert;

import com.base.Environment;
import com.base.command.BaseCommand;
import com.base.constant.BaseConstant.Timeout;

public class BaseComponent {
	protected Environment env;
	protected SearchStep searchStep;
	protected BaseCommand baseCommand;
	
	public BaseComponent(Environment env, SearchStep searchStep, String query) {
		this.env = env;
		
		SearchStep clone = searchStep.clone();
		clone.push(query);
		this.searchStep = clone;
		this.baseCommand = env.getBaseCommand();
	}
	
	public BaseComponent(Environment env, SearchStep searchStep) {
		this.env = env;
		
		this.searchStep = searchStep;
		this.baseCommand = env.getBaseCommand();
	}
	
	public Environment getEnv() {
		return env;
	}

	protected SearchStep cloneSearchStep() {
		return searchStep.clone();
	}
	
	public SearchStep getSearchStep() {
		return searchStep.clone();
	}
	
	protected SearchStep getSearchStep(String query) {
		return new SearchStepBuilder(baseCommand, query).build();
	}
	
	protected WebElement get() {
		Assert.assertTrue(searchStep != null, String.format("searchStep == null at get()"));
		return searchStep.get();
	}
	
	public void waitUntilPresented() {
		waitUntilPresented(Timeout.MID);
	}
	
	public void waitUntilPresented(Timeout timeout) { // TODO 아래 3개 조건을 한꺼번에 검사하도록 수정해야 함
		baseCommand.waitUntil(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return isPresented();
			}
		}, timeout);
		waitUntilStopped(timeout);
	}
	
	public void waitUntilEnabled() {
		waitUntilEnabled(Timeout.MID);
	}
	
	public void waitUntilDetached(Timeout timeout) {
		baseCommand.waitUntil(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return !isPresented();
			}
		}, timeout);		
	}
	
	public void waitUntilEnabled(Timeout timeout) {
		baseCommand.waitUntil(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return isEnabled();
			}
		}, timeout);
	}
	
	public boolean isEnabled() {
		WebElement element = get();
		if(element == null) {
			baseCommand.info(true, String.format("element == null at BaseCommand.isEnabled"));
			return false;
		}
		
		try {
			baseCommand.info(true, String.format("return element.isEnabled(); at BaseCommand.isEnabled"));
			return element.isEnabled();
		} catch (Exception e) {
			baseCommand.info(true, String.format("Exception e at BaseCommand.isEnabled"));
			return false;
		}
	}
	
	public void expect(boolean expected) {
		boolean actual = baseCommand.isDisplayed(searchStep);
		Assert.assertEquals(actual, expected);
	}
	
	public boolean doesExist() {
		WebElement element = get();
		return element != null;
	}
	
	public boolean isClickable() {
		WebElement element = get();
		return isPresented() && element.isEnabled();
	}
	
	public void click() {
		baseCommand.click(get());
	}
	
	public boolean isPresented() {
		boolean isDebug = false;
		try {
			boolean doesExist = baseCommand.doesExist(searchStep);
			boolean isDisplayed = baseCommand.isDisplayed(searchStep);
			boolean isBiggerThan = baseCommand.isBiggerThan(searchStep, 5, 5);
			boolean isPresented = doesExist && isDisplayed && isBiggerThan;
			if(!isPresented) {				
				baseCommand.info(isDebug, String.format("queryChain:'%s' at BaseComponent.isPresented()", searchStep.getQueryChain()));
				baseCommand.info(isDebug, String.format("doesExist:'%b' at BaseComponent.isPresented()", doesExist));
				baseCommand.info(isDebug, String.format("isDisplayed:'%b' at BaseComponent.isPresented()", isDisplayed));
				baseCommand.info(isDebug, String.format("isBiggerThan:'%b' at BaseComponent.isPresented()", isBiggerThan));
			}
			
			return isPresented;
		} catch (Exception e) {
			return false;
		}
	}
	
	public Point getLocation() {
		WebElement element = get();
		if(element == null) {
			return null;
		}
		
		Point point;
		try {
			point = element.getLocation();
		} catch (Exception e) {
			return null;
		}
		return point;
	}
	
	public void hide() {
		baseCommand.setDisplayHidden(searchStep);
	}

	public BaseCommand getBaseCommand() {
		return baseCommand;
	}
	
	public void waitUntilStopped(Timeout timeout) {
		baseCommand.waitUntilStopped(searchStep, timeout);
	}
}
