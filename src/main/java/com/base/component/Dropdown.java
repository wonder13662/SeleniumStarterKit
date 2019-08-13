package com.base.component;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.base.Environment;
import com.base.constant.BaseConstant.Timeout;
import com.base.util.NumberUtil;
import com.base.util.StringUtil;

public class Dropdown extends BaseComponent {
	
	public static enum OptionAttribute {
		INDEX(), VALUE(), VISIBLE_TEXT();
	}
	
	public Dropdown(Environment env, SearchStep searchStep) { 
		super(env, searchStep);
	}
	
	public boolean doesExist() {
		WebElement dropdown = get();
		return dropdown != null;
	}	
	
	public void debugVisibleTexts() {
		List<String> options = getOptionVisisbleTexts();
		for (String option : options) {
			env.getLogger().info(String.format("option:\"%s\" at debug", option));
		}
	}
	
	private Select getSelect() {
		WebElement element = get();
		if(element == null) {
			return null;
		}
		return baseCommand.getSelect(element);
	}
	
	public boolean hasMoreOptionThan(int expected) {
		Select select = getSelect();
		if(select == null) {
			return false;
		}
		
		List<WebElement> options = baseCommand.getOptions(select);
		int actual = options.size();
		
		boolean hasMore = (expected < actual); 
		if(!hasMore) {
			env.info(false, String.format("expected:%d < actual:%d at hasMoreOptionThan", expected, actual));
		}
		
		return hasMore;
	}
	
	private int getOptionIdx(OptionAttribute optionAttribute, String expected) {
		Select select = getSelect();
		if(select == null) {
			return -1;
		}		
		expected = expected.trim();
		List<WebElement> options = baseCommand.getOptions(select);
		
		baseCommand.info(false, String.format("'%s' at Dropdown.getOptionIdx()", optionAttribute.name()));
		baseCommand.info(false, String.format("expected:'%s' at Dropdown.getOptionIdx()", expected));
		
		for (int i = 0; i < options.size(); i++) {
			
			WebElement option = options.get(i);
			String actual = "";
			
			if(optionAttribute == OptionAttribute.VALUE) {
				actual = option.getAttribute("value");
			} else if(optionAttribute == OptionAttribute.VISIBLE_TEXT) {
				actual = option.getText();
			}
			
			baseCommand.info(false, String.format("actual:'%s' at Dropdown.getOptionIdx()", actual));
						
			if(actual.equalsIgnoreCase(expected)) {
				return i;
			}
		}
		
		return -1;
	}
	
	private String getOptionTextByIdx(int idx) {
		Select select = getSelect();
		if(select == null) {
			return null;
		}		
		List<WebElement> options = select.getOptions();
		if((options.size() - 1) < idx) {
			return null;
		}
		
		WebElement option = options.get(idx);
		return option.getText();
	}
	
	private String getOptionByValue(String value) {
		int optionIdx = getOptionIdx(OptionAttribute.VALUE, value.trim());
		baseCommand.info(false, String.format("optionIdx:'%d' at getOptionByValue", optionIdx));
		
		Assert.assertTrue(optionIdx > -1, String.format("value:'%s' optionIdx < 0 at getOptionVisibleText", value));
		return getOptionTextByIdx(optionIdx);
	}
	
	public void selectByValue(int value) {
		selectByValue("" + value);
	}
	
	public void selectByValue(String value) {
		waitUntilPresented();
		
		String actualVisibleText = getSelectedVisibleText();
		String expectedVisibleText = getOptionByValue(value);
		baseCommand.info(false, String.format("actualVisibleText:%s at selectByValue", actualVisibleText));
		baseCommand.info(false, String.format("value:%s at selectByValue", value));
		baseCommand.info(false, String.format("expectedVisibleText:%s at selectByValue", expectedVisibleText));
		
		if(expectedVisibleText.equalsIgnoreCase(actualVisibleText)) {
			// 선택하려는 값이 이미 선택되었다면 중단.
			baseCommand.info(false, String.format("Execution stopped. expectedVisibleText:%s.equalsIgnoreCase(actualVisibleText:%s) at selectByValue", expectedVisibleText, actualVisibleText));
			return;
		}
		
		Select select = getSelect();
		if(select == null) {
			baseCommand.info(false, String.format("select == null at selectByValue", expectedVisibleText, actualVisibleText));
			return;
		}
		
		select.selectByValue(value);
		waitUntilSelectedOptionVisibleText(expectedVisibleText);
	}
	
	public void selectByContainingVisibleText(String visibleText) {
		waitUntilPresented(Timeout.SHORT);
		waitUntilHasMoreOptionThan(0, Timeout.SHORT);
		
		// 2. Containing comparison
		String actualVisibleText = getSelectedVisibleText();
		if(actualVisibleText.contains(visibleText)) {
			return;
		}
		
		List<String> options = getOptionVisisbleTexts();
		for (int i = 0; i < options.size(); i++) {
			String option = options.get(i);
			if(option.contains(visibleText)) {
				selectByIndex(i);
			}
		}
	}
	
	public void selectByVisibleText(String visibleText) {
		waitUntilPresented(Timeout.SHORT);
		waitUntilHasMoreOptionThan(0, Timeout.SHORT);
		
		// 1. Strict comparison
		String log;
		String actualVisibleText = getSelectedVisibleText();
		if(visibleText.equalsIgnoreCase(actualVisibleText)) {
			// 선택하려는 값이 이미 선택되었다면 중단.
			log = String.format("Execution stopped. visibleText:%s is already selected. at selectByVisibleText", visibleText);
			baseCommand.info(true, log);
			return;
		}
		
		Select select = getSelect();
		if(select == null) {
			return;
		}
		log = String.format("visibleText:\"%s\" at selectByVisibleText", visibleText);
		baseCommand.info(false, log);
		
		try {
			select.selectByVisibleText(visibleText);
			waitUntilSelectedOptionVisibleText(visibleText);
		} catch (NoSuchElementException e) {
			log = String.format("NoSuchElementException occurs of '%s' at selectByVisibleText", visibleText);
			baseCommand.info(true, log);
		} catch (Exception e) {
			log = String.format("Exception occurs of '%s' at selectByVisibleText", visibleText);
			baseCommand.info(true, log);
		}
	}
	
	public void selectByIndex(int idx) {
		waitUntilPresented();
		waitUntilHasMoreOptionThan(0);
		
		int selectedIdx = getSelectedIdx();
		if(selectedIdx == idx) {
			// 선택하려는 값이 이미 선택되었다면 중단.
			boolean isDebug = false;
			if(isDebug) {
				env.getLogger().info(String.format("Execution stopped. selectedIdx:%d == idx:%d at selectByIndex", selectedIdx, idx));
			}
			return;
		}
		
		Select select = getSelect();
		Assert.assertTrue(select != null);
		baseCommand.select(select, idx);
	}

	public void selectRandomly(int min) {
		waitUntilPresented();
		waitUntilHasMoreOptionThan(0);
		
		Select select = getSelect();
		if(select == null) {
			return;
		}
		List<WebElement> options = select.getOptions();
		
		int max = options.size() - 1;
		Assert.assertTrue(min <= max, String.format("min:%d <= max:%d", min, max));
		
		int randomIdx = NumberUtil.getRandom(min, max);
		select.selectByIndex(randomIdx);
	}
	
	private void waitUntilSelectedOptionVisibleText(String expected) {
		int validOptionCnt = getValidOptionCnt();
		if(validOptionCnt < 2) {
			// 선택가능한 항목이 2개 이상이어야 Dropdown의 업데이트를 기다린다.
			return;
		}
		
		baseCommand.waitUntil(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {		
				String actual = getSelectedVisibleText();
				env.info(false, String.format("expected:\"%s\" == actual:\"%s\" at waitUntilSelectedOption ", expected, actual));
				return expected.equals(actual);
			}
		}, Timeout.SHORT);		
	}	
	
	public List<String> getOptionVisisbleTexts() {
		Select select = getSelect();
		List<String> staleOptions = new ArrayList<String>();
		if(select == null) {
			return staleOptions;
		}
		
		try {
			List<WebElement> options = select.getOptions();
			for (WebElement option : options) {
				staleOptions.add(option.getText());
			}
		} catch (Exception e) {
			baseCommand.info(false, String.format("Exception e at Dropdown.getOptionVisisbleTexts"));
			return staleOptions;
		}
		
		return staleOptions;
	}
	@Deprecated
	public void waitUntilHasMoreOptionThan(int expected) {
		baseCommand.waitUntil(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return hasMoreOptionThan(expected);
			}
		});
	}
	
	public void waitUntilHasMoreOptionThan(int expected, Timeout timeout) {
		baseCommand.waitUntil(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return hasMoreOptionThan(expected);
			}
		}, timeout);
	}
	
	public void waitUntilOptionsChanged(List<String> staleOptions) {
		baseCommand.waitUntil(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				Select select = getSelect();
				if(select == null) {
					return false;
				}
				
				List<WebElement> options = select.getOptions();
				if(staleOptions.size() != options.size()) {
					// Option의 갯수가 바뀌었다.
					return true;
				}
				
				for (int i = 0; i < options.size(); i++) {
					WebElement element = options.get(i);
					String visibleText = baseCommand.getText(element);
					String staleVisibleText = staleOptions.get(i);
					
					if(!visibleText.equals(staleVisibleText)) {
						// Option의 갯수는 같으나 내용이 바뀌었다.
						return true;
					}
				}
				
				return false;
			}
		});		
	}
	
	public void expectValue(String expected) {
		String actual = getSelectedValue();
		String errMsg = String.format("actual:%s != expected:%s at expectValue", actual, expected);
		Assert.assertTrue(actual.trim().equals(expected.trim()),errMsg);
	}
	
	public boolean hasValue(String expected) {
		String actual = getSelectedValue();
		return actual.equalsIgnoreCase(expected);
	}
	
	public boolean hasValue(int expected) {
		String actual = getSelectedValue();
		return actual.equalsIgnoreCase("" + expected);
	}
	
	public boolean hasSelectedVisibleText(String expected) {
		String actual = getSelectedVisibleText();
		return actual.trim().equals(expected.trim());
	}
	
	public void expectVisibleText(String expected) {
		String actual = getSelectedVisibleText();
		String errMsg = String.format("actual:'%s' != expected:'%s' at expect", actual, expected);
		boolean hasSelectedVisibleText = hasSelectedVisibleText(expected);
		Assert.assertTrue(hasSelectedVisibleText,errMsg);
	}
	
	private int getValidOptionCnt() {
		Select select = getSelect();
		int validOptionCnt = 0;
		if(select == null) {
			return validOptionCnt;
		}
		
		List<WebElement> options = select.getOptions();
		
		for (WebElement option : options) {
			try {
				String value = option.getAttribute("value");
				if(StringUtil.isNotEmpty(value)) {
					validOptionCnt++;
				}
			} catch (Exception e) {

			}
		}
		
		return validOptionCnt;
	}
	
	public String getSelectedVisibleText() {
		Select select = getSelect();
		if(select == null) {
			return "";
		}
		
		Assert.assertTrue(select != null, String.format("select == null at getSelectedVisibleText"));
		
		try {
			return select.getFirstSelectedOption().getText();
		} catch (Exception e) {
			return "";
		}
	}
	
	public String getSelectedValue() {
		Select select = getSelect();
		if(select == null) {
			return null;
		}
		return select.getFirstSelectedOption().getAttribute("value");
	}
	
	public int getSelectedIdx() {
		Select select = getSelect();
		if(select == null) {
			return -1;
		}
		
		List<WebElement> options = baseCommand.getOptions(select);
		WebElement selectedOption = baseCommand.getFirstSelectedOption(select);
		if(selectedOption == null) {
			return -1;
		}
		
		String selectedOptionValue = selectedOption.getAttribute("value");
		for (int i = 0; i < options.size(); i++) {
			WebElement option = options.get(i);
			String curOptionValue = option.getAttribute("value");
			if(selectedOptionValue.equalsIgnoreCase(curOptionValue)) {
				return i;
			}
		}
		
		return -1;
	}
	
	public void click() {
		Select select = getSelect();
		baseCommand.click(select.getWrappedElement());
	}
}
