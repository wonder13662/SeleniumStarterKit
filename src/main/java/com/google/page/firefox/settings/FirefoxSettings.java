package com.google.page.firefox.settings;

import org.openqa.selenium.Alert;

import com.base.Environment;
import com.base.constant.BaseConstant.Browser;
import com.base.constant.BaseConstant.Timeout;
import com.base.page.BasePage;

public class FirefoxSettings extends BasePage {	
	public FirefoxSettings(Environment env) {
		super(env, "");
	}
	
	public void clearBrowserData() {
		// https://intoli.com/blog/clear-the-firefox-browser-cache/
		if(Browser.FIREFOX != env.getBrowser()) return;
		
		baseCommand.openNewTab();
		baseCommand.switchTab(1);
		
		baseCommand.openUrl("about:preferences#privacy", Timeout.SHORT);
		baseCommand.sleep(Timeout.SHORT);
		
		// "데이터 삭제" 모달을 띄웁니다.
		baseCommand.click("button#clearSiteDataButton");
		
		// 모달안의 "삭제" 버튼을 누릅니다.
		String script = "document.querySelector('#dialogOverlay-0 > .dialogBox > .dialogFrame').contentDocument.documentElement.querySelector('#clearButton').click();";
		baseCommand.runJS(script);		
		baseCommand.sleep(Timeout.SHORT);
		
        Alert alert = env.getDriver().switchTo().alert();
        alert.accept();
        
        baseCommand.switchTab(0);
	}
}
