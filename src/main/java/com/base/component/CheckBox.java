package com.base.component;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.testng.Assert;

import com.base.Environment;

public class CheckBox extends BaseComponent {
	
	private SearchStep childInputSearchStep;
	private String queryCheckMark = "span.checkmark";
	
	public CheckBox(Environment env, SearchStep parentSearchStep, SearchStep childInputSearchStep) { 
		super(env, parentSearchStep);
		this.childInputSearchStep = childInputSearchStep;
	}
	
	public boolean isCheckMark() { 
		WebElement parent = getClickableCheckbox();
		if(parent == null) {
			return false;
		}
		List<WebElement> checkmarks = parent.findElements(By.cssSelector(queryCheckMark));
		
		return checkmarks.size() > 0;
	}
	
	public boolean isChecked() {
		SearchStep target = getSearchStepInputCheckBox();
		Assert.assertTrue(target != null, "checkbox could not be null at isChecked");
		
		return baseCommand.isSelected(target);
	}
	
	private SearchStep getSearchStepInputCheckBox() {
		return (childInputSearchStep != null)?searchStep.cloneNpush(childInputSearchStep.clone()):null;
	}
	
	public void expect(boolean expected) {
		boolean actual = isChecked();
		String errMsg = String.format("actual:%b != expected:%b at expect", actual, expected);
		Assert.assertTrue(actual == expected, errMsg);
	}
	
	public void click(boolean expected) {
		clickNoWaitUpdated(expected);
		waitUntilUpdated(expected);
	}
	
	public void clickNoWaitUpdated(boolean expected) {
		boolean actual = isChecked();
		if(expected == actual) {
			return;
		}
		
		WebElement checkBox = null;
		if(isCheckMark()) {
			checkBox = getClickableCheckBoxInCheckMark();
		} else {
			checkBox = getClickableCheckbox();
		}
		Assert.assertTrue(checkBox != null, "checkBox could not be null at click");
		baseCommand.click(checkBox);
	}	
	
	private WebElement getClickableCheckbox() {
		return get();
	}
	
	private WebElement getClickableCheckBoxInCheckMark() {
		return searchStep.cloneNpush(queryCheckMark).get();
	}
	
	private void waitUntilUpdated(boolean expected) {
		baseCommand.waitUntil(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				boolean actual = isChecked();
				baseCommand.info(true, String.format("expected:%b == actual:%b at Checkbox.waitUntilUpdated()", expected, actual));
				return expected == actual;
			}
		});		
	}		
}
