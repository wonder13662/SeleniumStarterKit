package com.base.component;

import java.math.BigDecimal;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.testng.Assert;

import com.base.Environment;
import com.base.constant.BaseConstant.Timeout;
import com.base.util.NumberUtil;
import com.base.util.StringUtil;

public class Input extends BaseComponent {
	
	public Input(Environment env, SearchStep searchStep) { 
		super(env, searchStep);
	}
	
	public boolean doesExist() {
		WebElement input = get();
		return (input != null)?true:false;
	}
	
	public boolean isEmpty() {
		return StringUtil.isEmpty(getValue());
	} 
	@Deprecated
	public float getFloat() {
		String actual = getValue();
		return Float.parseFloat(actual);
	}
	
	public BigDecimal getNumber() {
		String value = getValue();
		return new BigDecimal(value);
	}
	
	public void expect(BigDecimal expect) {
		BigDecimal actual = new BigDecimal(getValue());
		Assert.assertTrue(actual.compareTo(expect) == 0, String.format("actual:'%s' != expect:'%s' at expect", actual.toString(), expect.toString()));
	}
	
	public void expect(String expected) {
		String actual = getValue();
		
		baseCommand.info(false, String.format("actual:'%s' == expected:'%s' at Input.expect", actual, expected));
		String errMsg = String.format("actual:%s != expected:%s at Input.expect", actual, expected);
		Assert.assertEquals(actual, expected, errMsg);
	}
	
	public void expect(float number) {
		String value = new BigDecimal(number).setScale(2).toString();
		expect(value);
	}
	
	public void click() {
		baseCommand.click(get());
	}
	
	public void clickAndUnfocus() {
		baseCommand.click(get());
		baseCommand.sleep(Timeout.TINY);
		baseCommand.click("body");
	}
	
	public void type(BigDecimal number) {
		String value = number.setScale(2).toString();
		typeNoWait(value);
		waitUntilInputUpdated(searchStep, number);
	}
	
	private void typeNoWait(String value) {
		Assert.assertTrue(StringUtil.isNotEmpty(value));
		
		if(hasText(value)) {
			return;
		}
		
		clear();
		
		baseCommand.click(searchStep);
		baseCommand.sendKeys(searchStep, value);
	}
	
	public void typeAdditionally(String value, String expected) {
		Assert.assertTrue(StringUtil.isNotEmpty(value));
		
		baseCommand.click(searchStep);
		baseCommand.sendKeys(searchStep, value);
		
		waitUntilInputUpdated(searchStep, expected);
	}	
	
	@Deprecated
	public void type(float number) {
		String value = new BigDecimal(number).setScale(2).toString();
		type(value);
	}
	
	public void type(int number) {
		String value = "" + number;
		type(value);
	}
	
	private boolean hasText(String expected) {
		String actual = getText();
		return StringUtil.isNotEmpty(actual) && actual.equals(expected);
	}
	
	public void typeNExpect(String value, String expected) {
		typeNoWait(value);
		waitUntilInputUpdated(searchStep, expected);	
	}	

	public void type(String value) {
		if(StringUtil.isEmpty(value)) {
			return;
		}
		
		typeNoWait(value);		
		waitUntilInputUpdated(searchStep, value);	
	}
	
	public void type(String value, String placeHolder) {
		if(hasText(value)) {
			return;
		}
		
		clear(placeHolder);
		searchStep.get().sendKeys(value);
		waitUntilInputUpdated(searchStep, value);
	}
	
	public void clearByKeyboard() {
		searchStep.get().click();
		waitUntilInputEmpty(searchStep, "");
	}
	
	private void waitUntilInputEmpty(SearchStep searchStep, String expected) {
		baseCommand.waitUntil(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				// input email type은 지원하지 않음.
				if(!baseCommand.hasAttrType(searchStep, "email")) {
					baseCommand.selectAllTextInInputFieldByJs(searchStep.get());
				}
				
				typeKeyboard(Keys.BACK_SPACE);
				
				if(baseCommand.doesExist(searchStep)) {
					WebElement element = searchStep.get();
					if(element == null) {
						return false;
					}
					
					String actual = getValueFromElement(element);
					baseCommand.info(false, String.format("e:%s at Input.waitUntilInputEmpty()", expected));
					baseCommand.info(false, String.format("a:%s at Input.waitUntilInputEmpty()", actual));
					return actual.equalsIgnoreCase(expected);
				}
				return false;
			}
		}, Timeout.MID);
	}	
	
	private void typeKeyboard(Keys key) {
		try {
			searchStep.get().sendKeys(key);
		} catch (Exception e) {

		}
	}
	
	public void typeCountinously(String value) {
		searchStep.get().sendKeys(value);
	}
	
	public void noClearAndType(float value) {
		noClearAndType("" + value);
	}
	
	public void noClearAndType(String value) {
		baseCommand.info(false, String.format("value:'%s' at noClearAndType()", value));
		baseCommand.info(false, String.format("before:'%s' at noClearAndType()", getText()));
		
		searchStep.get().sendKeys(value);
		
		baseCommand.info(false, String.format("after:'%s' at noClearAndType()", getText()));
		
		waitUntilInputUpdated(searchStep, value);
	}
	
	private String getText() {
		baseCommand.info(false, String.format("searchStep:'%s' at Input.getText()", searchStep.getQueryChain()));
		return baseCommand.getInputValue(searchStep);
	}
	
	public void clear() {
		clear("");
	}
	
	public void clear(String placeHolder) {
		boolean isDebug = false;
		
		baseCommand.waitUntilVisible(searchStep, Timeout.MID);
		if(!baseCommand.isEnabled(searchStep)) {
			baseCommand.info(isDebug,String.format("!baseCommand.isEnabled(searchStep) at Input.clear()"));
			return;
		}
		
		String text = getText();
		if(StringUtil.isEmpty(text)) {
			baseCommand.info(isDebug,String.format("StringUtil.isEmpty(text) at Input.clear()"));
			return;
		}
		
		baseCommand.info(isDebug,String.format("placeHolder:'%s' at Input.clear()", placeHolder));
		baseCommand.info(isDebug,String.format("text:'%s' at Input.clear()", text));

		WebElement element = searchStep.get();
		element.click();
		element.clear();
		
		baseCommand.sleep(Timeout.TINY);
		
		text = getText();
		baseCommand.info(isDebug,String.format("After / text:'%s' at clear()", text));
		waitUntilInputUpdated(searchStep, placeHolder);
	}

	public String getValue() {
		return getValueFromElement(get());
	}
	
	public float getFloatValue() {
		String value = getValueFromElement(get()); 
		return Float.parseFloat(value);
	}	

	public void waitUntilInputHasText() {
		boolean isDebug = false;
		baseCommand.waitUntil(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				String text = getValue();
				baseCommand.info(isDebug, String.format("text:%s at waitUntilInputHasText()", text));
				
				boolean hasText = StringUtil.isNotEmpty(text);
				baseCommand.info(isDebug, String.format("hasText:%b at waitUntilInputHasText()", hasText));
				
				return hasText;
			}
		});		
	}

	private void waitUntilInputUpdated(SearchStep searchStep, String expected) {
		boolean isDebug = true;
		baseCommand.waitUntil(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				if(baseCommand.doesExist(searchStep)) {
					WebElement element = searchStep.get();
					if(element == null) {
						return false;
					}
					
					String actual = getValueFromElement(element);
					baseCommand.info(isDebug, String.format("e:%s at Input.waitUntilInputUpdated()", expected.trim()));
					baseCommand.info(isDebug, String.format("a:%s at Input.waitUntilInputUpdated()", actual.trim()));
					return actual.trim().equalsIgnoreCase(expected.trim());
				}
				return false;
			}
		}, Timeout.SHORT);
	}
	
	private void waitUntilInputUpdated(SearchStep searchStep, BigDecimal expected) {
		boolean isDebug = false;
		baseCommand.waitUntil(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				if(baseCommand.doesExist(searchStep)) {
					WebElement element = searchStep.get();
					if(element == null) {
						return false;
					}
					
					String inputStr = getValueFromElement(element);
					BigDecimal actual = new BigDecimal(inputStr);
					
					baseCommand.info(isDebug, String.format("e:%s at Input.waitUntilInputUpdated()", expected.setScale(2).toString()));
					baseCommand.info(isDebug, String.format("a:%s at Input.waitUntilInputUpdated()", actual.setScale(2).toString()));
					
					return NumberUtil.isEqual(actual, expected);
				}
				return false;
			}
		}, Timeout.SHORT);
	}	
	
	private String getValueFromElement(WebElement element) {
		try {
			return element.getAttribute("value");
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}
	}
}
