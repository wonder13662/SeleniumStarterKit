package com.base.component;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.testng.Assert;

import com.base.Environment;
import com.base.constant.BaseConstant.Timeout;
import com.base.util.StringUtil;

public class Button extends BaseComponent {
	public static enum WaitType {
		LOADING_BAR(), ROLLBACK_TITLE(), DETACH(), NONE();
	}
	
	public Button(Environment env, SearchStep searchStep) { 
		super(env, searchStep);
	}
	
	// safari에서는 오동작 할 수 있음.
	public void waitUntilHasTitle(String expected, Timeout timeout) {
		baseCommand.waitUntil(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				String actual = getTitle();
				env.info(String.format("actual:'%s' == expected:'%s' at waitUntilHasTitle", actual, expected));
				return actual.equalsIgnoreCase(expected);
			}
		}, timeout);			
	}
	
	private String getTitle() {
		if(!hasTitle()) {
			return "";
		}
		
		if(!doesExist()) {
			return "";
		}
		
		SearchStep btnInnerTitleSearchStep = searchStep.cloneNpush(".inner-content-box");
		if(baseCommand.isEnabled(btnInnerTitleSearchStep)) {
			return baseCommand.getText(btnInnerTitleSearchStep);
		}
		
		return baseCommand.getText(searchStep.clone()).trim();
	}
	
	private boolean hasTitle() {
		// 0. 화면에 버튼 엘리먼트가 없다면 타이틀이 없는 것으로 판단.
		if(!doesExist()) {
			return false;
		}
		
		// 1. title box에 타이틀 텍스트가 있는 경우.
		SearchStep innerContentBox = searchStep.clone().cloneNpush(".inner-content-box");
		if(baseCommand.doesExist(innerContentBox)) {
			return true;
		}
		
		// 2. 버튼 내부에 타이틀이 있는 경우.
		String title = baseCommand.getText(searchStep.clone());
		if(StringUtil.isNotEmpty(title)) {
			return true;
		}
		
		return false;
	}
	
	public void click() {
		click(WaitType.NONE);
	}
	
	public void click(WaitType waitType) {
		boolean isEnabled = isEnabled();
		Assert.assertTrue(isEnabled);
		
		String btnTitleBeforeClick = baseCommand.getText(searchStep);
		boolean hasClicked = baseCommand.click(searchStep);
		Assert.assertTrue(hasClicked);
		
		waitAfterClicked(waitType, btnTitleBeforeClick);
	}
	
	private void waitAfterClicked(WaitType waitType, String btnTitleBeforeClick) {
		boolean doesExist = doesExist();
		boolean isDebug = false;
		
		// TODO DOM에 Element가 없다는 것의 의미
		// DOM에서 1초 동안 연속적으로 검색 결과가 없다는 것을 확인해야 함
		
		// TODO DOM에 Element가 있다는 의미
		// DOM에서 1초 동안 연속적으로 검색 결과가 있다는 것을 확인해야 함
		
		// React 등의 JS Framework에서는 연속적으로 그리고 지우기를 반복하기 때문
		
		if(doesExist) {
			if(waitType == WaitType.LOADING_BAR) {
				baseCommand.info(isDebug,String.format("1. 클릭 직후 버튼에 로딩 애니메이션 혹은 로딩중을 의미하는 텍스트로 변경."));
				String curTitle = getTitle();
				if(StringUtil.isNotEmpty(curTitle)) {
					baseCommand.info(isDebug,String.format("1-1. 버튼의 Text가 있는 경우 curTitle:'%s'",curTitle));
					baseCommand.info(isDebug,String.format("1-1-1. 로딩바가 사라진 이후에 이전 버튼의 텍스트로 돌아오는 경우. 클릭 이후의 버튼의 텍스트가 달라진다면 실패가 발생."));
					waitUntilBtnHasTitle();
				}
			} else if(waitType == WaitType.ROLLBACK_TITLE) {
				if(hasTitle()) {
					baseCommand.info(isDebug,String.format("1-3. 버튼의 Text가 있는 경우"));
					baseCommand.info(isDebug,String.format("1-3-1. 로딩바가 사라진 이후에 이전 버튼의 텍스트로 돌아오는 경우. 클릭 이후의 버튼의 텍스트가 달라진다면 실패가 발생."));
					
					waitUntilBtnTitleRestored(btnTitleBeforeClick);
				}
			}
		} else if(waitType == WaitType.DETACH) {
			// 2. 클릭 직후에 화면 전환으로 버튼이 DOM에서 사라지는 경우.
			baseCommand.info(isDebug,String.format("2. 클릭 직후에 화면 전환으로 버튼이 DOM에서 사라지는 경우."));
			waitUntilDetached(Timeout.MID);
		}		
		baseCommand.info(isDebug,String.format("3. 버튼이 DOM에 없습니다."));
	}
	
	private void waitUntilBtnHasTitle() {
		baseCommand.waitLongUntil(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return doesExist() && hasTitle();
			}
		});
	}
	
	private void waitUntilBtnTitleRestored(String expected) {
		baseCommand.waitLongUntil(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				if(doesExist()) {
					String actual = getTitle();
					env.info(false, String.format("expected:'%s' == actual:'%s' at waitUntilBtnTitleRestored", expected,actual));
					return StringUtil.equalsAfterTrim(actual, expected);
				}
				// 1. 클릭 직후, 처리가 완료되어서 버튼이 DOM에서 사라진 상황의 처리
				return true;
			}
		});		
	}
}
