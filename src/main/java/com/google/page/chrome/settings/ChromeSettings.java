package com.google.page.chrome.settings;

import com.base.Environment;
import com.base.constant.BaseConstant.Browser;
import com.base.constant.BaseConstant.Timeout;
import com.base.page.BasePage;

public class ChromeSettings extends BasePage {	
	public ChromeSettings(Environment env) {
		super(env, "");
	}
	
	public void clearBrowserData() {
		if(Browser.CHROME != env.getBrowser()) return;
		
		baseCommand.openNewTab();
		baseCommand.switchTab(1);
		
		baseCommand.openUrl("chrome://settings/clearBrowserData", Timeout.SHORT);
		baseCommand.sleep(Timeout.SHORT);
		
		String script = "document.querySelector('body > settings-ui').shadowRoot.querySelector('div#container #main').shadowRoot.querySelector('settings-basic-page').shadowRoot.querySelector('div#advancedPage settings-section settings-privacy-page').shadowRoot.querySelector('settings-clear-browsing-data-dialog').shadowRoot.querySelector('cr-dialog > div:nth-child(4) #clearBrowsingDataConfirm').click();";
		baseCommand.runJS(script);
		baseCommand.sleep(Timeout.SHORT);
		
		baseCommand.switchTab(0);
	}
}
