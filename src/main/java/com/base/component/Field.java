package com.base.component;

import java.math.BigDecimal;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.testng.Assert;

import com.base.Environment;
import com.base.command.BaseCommand;
import com.base.component.Button.WaitType;
import com.base.constant.BaseConstant.Timeout;
import com.base.util.ElementUtil;
import com.base.util.StringUtil;

public class Field {
	
	private CheckBox checkBox;
	private Dropdown dropdown;
	private Input input;
	private SearchStep searchStep;
	private SearchStep container;
	private BaseCommand baseCommand;
	private Button button;
	
	public Field(CheckBox checkBox) {
		this.checkBox = checkBox;
	}	
	public Field(Dropdown dropdown) {
		this.dropdown = dropdown;
	}
	
	public Field(Dropdown dropdown, SearchStep container) {
		this.dropdown = dropdown;
		this.baseCommand = dropdown.getBaseCommand();
		this.container = container;
	}
	
	public Field(Input input) {
		this.input = input;
	}
	
	public Field(Input input, SearchStep container) {
		this.input = input;
		this.baseCommand = input.getBaseCommand();
		this.container = container;		
	}
	
	public Field(Environment env, SearchStep searchStep) {
		// 화면에 표시만 되는 경우, 값을 읽어오는 selector의 역할.
		this.searchStep = searchStep;
		this.baseCommand = env.getBaseCommand();
	}
	
	public Field(Button button) {
		this.button = button;
	}
	
	public void hide() {
		if(searchStep != null) {
			baseCommand.setDisplayHidden(searchStep);
			return;
		}
		
		Assert.assertTrue(container != null,"container should be defined at Field.hide()");
		Assert.assertTrue(baseCommand != null,"baseCommand should be defined at Field.hide()");
		
		baseCommand.info(true, String.format("Field.hide() to '%s'", container.getQueryChain()));
		
		baseCommand.setDisplayHidden(container);
	}
	
	public void show() {
		Assert.assertTrue(container != null,"container should be defined at Field.hide()");
		Assert.assertTrue(baseCommand != null,"baseCommand should be defined at Field.hide()");
		
		baseCommand.setDisplayHidden(container);
	}

	public Field checkNoWaitUpdated(boolean isChecked) {
		if(checkBox != null) {
			checkBox.waitUntilPresented(Timeout.SHORT);
			if(checkBox.isEnabled()) {
				checkBox.clickNoWaitUpdated(isChecked);
			}
		} else {
			Assert.assertTrue(false, String.format("Failed at Field.check(boolean isChecked:'%b'",isChecked));
		}
		
		return this;
	}
	
	public Field check(boolean isChecked) {
		if(checkBox != null) {
			checkBox.waitUntilPresented(Timeout.SHORT);
			if(checkBox.isEnabled()) {
				checkBox.click(isChecked);
			}
		} else {
			Assert.assertTrue(false, String.format("Failed at Field.check(boolean isChecked:'%b'",isChecked));
		}
		
		return this;
	}
	
	public Field setRandomly(int minIdx) {
		if(dropdown != null) {
			dropdown.waitUntilPresented(Timeout.SHORT);
			if(dropdown.isEnabled()) {
				dropdown.selectRandomly(minIdx);
			}
		} else {
			Assert.assertTrue(false, "setRandomly is only available to dropdown");
		}
		return this;
	}
	
	public Field set(int idx) {
		if(dropdown != null) {
			dropdown.waitUntilPresented(Timeout.SHORT);
			if(dropdown.isEnabled()) {
				dropdown.selectByIndex(idx);
			}
		} else {
			Assert.assertTrue(false, "set is only available to dropdown");
		}
		return this;
	}	
	
	public Field clear() {
		if(input != null) {
			input.waitUntilInputHasText();
			input.clearByKeyboard();
		} else {
			Assert.assertTrue(false, String.format("This field doesn't have clear method at Field.clear()"));
		}
		
		return this;	
	}
	
	public Field set(String visibleText, String expected) {
		if(input != null) {
			input.waitUntilPresented(Timeout.SHORT);
			if(input.isEnabled()) {
				input.typeNExpect(visibleText, expected);
			}
		}
		
		return this;
	}
	
	public Field setAdditionally(String visibleText, String expected) {
		if(input != null) {
			input.waitUntilPresented(Timeout.SHORT);
			if(input.isEnabled()) {
				input.typeAdditionally(visibleText, expected);
			}
		}
		
		return this;
	}
	
	public Field selectTextContains(String visibleText) {
		if(dropdown != null) {
			dropdown.waitUntilPresented(Timeout.SHORT);
			if(dropdown.isEnabled()) {
				dropdown.selectByContainingVisibleText(visibleText);
			}
		}
		
		return this;
	}
	
	public Field set(String visibleText) {
		if(dropdown != null) {
			dropdown.waitUntilPresented(Timeout.SHORT);
			if(dropdown.isEnabled()) {
				dropdown.selectByVisibleText(visibleText);
			}
		} else if(input != null) {
			input.waitUntilPresented(Timeout.SHORT);
			if(input.isEnabled()) {
				input.clearByKeyboard();
				input.type(visibleText);
			}
		} else {
			Assert.assertTrue(false, String.format("Failed at Field.set(String visibleText:'%s')",visibleText));
		}
		
		return this;
	}
	
	public Field setNumber(BigDecimal value) {
		if(input == null) {
			Assert.assertTrue(false, String.format("Failed at Field.setPrice(BigDecimal value:'%s')",value.toString()));
		}
		
		input.waitUntilPresented(Timeout.SHORT);
		if(input.isEnabled()) {
			input.clearByKeyboard();
			input.type(value.toString());
		}
		
		return this;
	}
	
	public Field setNumber(BigDecimal value, String expected) {
		if(input == null) {
			Assert.assertTrue(false, String.format("Failed at Field.setPrice(BigDecimal value:'%s')",value.toString()));
		}
		
		input.waitUntilPresented(Timeout.SHORT);
		if(input.isEnabled()) {
			input.clearByKeyboard();
			input.typeNExpect(value.toString(), expected);
		}
		
		return this;
	}
	
	public boolean isEnabled() {
		if(checkBox != null) {
			return checkBox.isEnabled();
		} else if(dropdown != null) {
			return dropdown.isEnabled();
		} else if(input != null) {
			return input.isEnabled();
		} else if(button != null) {
			return button.isEnabled();
		} else {
			Assert.assertTrue(false, String.format("Failed at Field.isEnabled()"));
		}
		return false;
	}
	
	public boolean isChecked() {
		if(checkBox != null) {
			checkBox.waitUntilPresented();
			if(checkBox.isEnabled()) {
				return checkBox.isChecked();
			}
		} else {
			Assert.assertTrue(false, String.format("Failed at Field.isChecked()"));
		}
		return false;
	}
	
	public String get() {
		if(dropdown != null) {
			dropdown.waitUntilPresented(Timeout.SHORT);
			return dropdown.getSelectedVisibleText();
		} else if(input != null) {
			input.waitUntilPresented(Timeout.SHORT);
			return input.getValue();			
		} else if(searchStep != null) {
			try {
				return searchStep.get().getText();
			} catch (Exception e) {
				return "";
			}
		} else {
			Assert.assertTrue(false, String.format("Failed at Field.get()"));
		}
		return "";
	}
	
	public BigDecimal getNumber() {
		if(input != null) {
			String raw = input.getValue();
			return ElementUtil.getAmount(raw);
		} else if(searchStep != null) {
			try {
				String raw = searchStep.get().getText();
				return ElementUtil.getAmount(raw);
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}
	
	public Field expect(String expected) {
		if(dropdown != null) {
			dropdown.waitUntilPresented(Timeout.SHORT);
			if(dropdown.isEnabled()) {
				dropdown.expectVisibleText(expected);
			}
		} else if(input != null) {
			input.waitUntilPresented(Timeout.SHORT);
			if(input.isPresented()) {
				input.expect(expected);
			}
		} else if(searchStep != null) {
			try {
				String actual = searchStep.get().getText();
				actual = ElementUtil.removeDecoration(actual);
				Assert.assertEquals(actual, expected);
			} catch (Exception e) {
				Assert.assertTrue(false, String.format("expected:'%s' / Exception e occurred at Field.expect()", expected));
			}
		} else {
			Assert.assertTrue(false, String.format("Failed at Field.expect(String expected:'%s')",expected));
		}
		
		return this;
	}
	
	public void expectNumber(String expected) {
		expectNumber(new BigDecimal(expected));
	}
	
	public Field expectNumber(BigDecimal expected) {
		BigDecimal actual = getNumber();
		Assert.assertEquals(actual.setScale(2), expected.setScale(2));
		return this;
	}
	
	public void expect(boolean expected) {
		if(checkBox != null) {
			checkBox.waitUntilPresented();
			if(checkBox.isEnabled()) {
				checkBox.expect(expected);
			}
		} else if(searchStep != null) {
			String actual = get();
			Assert.assertEquals(actual, expected);
		} else {
			Assert.assertTrue(false, String.format("Failed at Field.expect()"));
		}
	}
	
	public void click() {
		click(WaitType.NONE);
	}
	
	public void click(WaitType waitType) {
		if(button != null) {
			button.waitUntilPresented(Timeout.SHORT);
			if(button.isEnabled()) {
				button.click(waitType);
			}
		} else if(input != null) {
			input.waitUntilPresented(Timeout.SHORT);
			if(input.isEnabled()) {
				input.click();
			}
		} else if(dropdown != null) {
			dropdown.waitUntilPresented(Timeout.SHORT);
			dropdown.click();
		} else if(searchStep != null) {
			baseCommand.waitUntilVisible(searchStep, Timeout.MID);
			baseCommand.click(searchStep);
		} else {
			Assert.assertTrue(false, String.format("Failed at Field.click()"));
		}
	}
	
	public void expectPresented(boolean expected) {
		boolean actual = false;
		if(checkBox != null) {
			actual = checkBox.isPresented();
		} else if(dropdown != null) {
			actual = dropdown.isPresented();
		} else if(input != null) {
			actual = input.isPresented();
		} else if(searchStep != null) {
			actual = baseCommand.isDisplayed(searchStep);
		} else if(button != null) {
			actual = button.isPresented();
		}	
		
		Assert.assertEquals(actual, expected);
	}
	
	public boolean isPresented() {
		if(checkBox != null) {
			return checkBox.isPresented();
		} else if(dropdown != null) {
			return dropdown.isPresented();
		} else if(input != null) {
			return input.isPresented();
		} else if(searchStep != null) {
			boolean isDisplayed = baseCommand.isDisplayed(searchStep);
			boolean isBigger = baseCommand.isBiggerThan(searchStep, 5, 5);
			return isDisplayed && isBigger;
		} else if(button != null) {
			return button.isPresented();
		}		
		return false;
	}
	@Deprecated
	public void waitUntilPresented() {
		if(checkBox != null) {
			checkBox.waitUntilPresented();
		} else if(dropdown != null) {
			dropdown.waitUntilPresented();
		} else if(input != null) {
			input.waitUntilPresented();
		} else if(searchStep != null) {
			baseCommand.waitUntilVisible(searchStep, Timeout.MID);
		} else if(button != null) {
			button.waitUntilPresented();
		}
	}
	
	public void waitUntilPresented(Timeout timeout) {
		if(checkBox != null) {
			checkBox.waitUntilPresented(timeout);
		} else if(dropdown != null) {
			dropdown.waitUntilPresented(timeout);
		} else if(input != null) {
			input.waitUntilPresented(timeout);
		} else if(searchStep != null) {
			baseCommand.waitUntilVisible(searchStep, timeout);
		} else if(button != null) {
			button.waitUntilPresented(timeout);
		}
	}	
	@Deprecated
	public void waitUntilEnabled() {
		// TODO Refactor me
		if(checkBox != null) {
			checkBox.waitUntilEnabled();
		} else if(dropdown != null) {
			dropdown.waitUntilEnabled();
		} else if(input != null) {
			input.waitUntilEnabled();
		} else if(button != null) {
			button.waitUntilEnabled();
		}
	}
	
	public void waitUntilDetached(Timeout timeout) {
		// TODO Refactor me
		if(checkBox != null) {
			checkBox.waitUntilDetached(timeout);
		} else if(dropdown != null) {
			dropdown.waitUntilDetached(timeout);
		} else if(input != null) {
			input.waitUntilDetached(timeout);
		} else if(searchStep != null) {
			baseCommand.waitUntilInvisible(searchStep, timeout);
		} else if(button != null) {
			button.waitUntilDetached(timeout);
		}
	}		
	
	public void waitUntilEnabled(Timeout timeout) {
		// TODO Refactor me
		if(checkBox != null) {
			checkBox.waitUntilEnabled(timeout);
		} else if(dropdown != null) {
			dropdown.waitUntilEnabled(timeout);
		} else if(input != null) {
			input.waitUntilEnabled(timeout);
		} else if(button != null) {
			button.waitUntilEnabled(timeout);
		}
	}
	
	public void waitUntilHasMoreOptionThan(int optionCnt, Timeout timeout) {
		if(dropdown != null) {
			dropdown.waitUntilHasMoreOptionThan(optionCnt, timeout);
		}		
	}
	
	public SearchStep getSearchStep() {
		if(button != null) {
			return button.getSearchStep();
		} else {
			Assert.assertTrue(false, String.format("Failed at Field.getSearchStep()"));
		}		
		return null;
	}
	
	public void waitUntilHasText(Timeout timeout) {
		if(input == null) {
			baseCommand.waitUntil(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					String valueStr = input.getValue();
					return StringUtil.isNotEmpty(valueStr);
				}
			}, timeout);
		}
	}
}
