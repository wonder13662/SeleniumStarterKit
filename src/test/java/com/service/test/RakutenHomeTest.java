package com.service.test;

import org.testng.annotations.Test;

import com.base.BaseTest;
import com.base.constant.BaseConstant.Timeout;
import com.service.action.RakutenHomeAction;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;

@Epic("Allure examples")
@Feature("Junit 4 support")
public class RakutenHomeTest extends BaseTest {
	
	@Test(description = "Test top navigation of Rakuten home")
	@Story("Base support for bdd annotations")
	@Severity(SeverityLevel.CRITICAL)
	public void testRakutenHome() {
		RakutenHomeAction rakutenHomeAction = env.getActionFactory().getAction(RakutenHomeAction.class);
		step1(rakutenHomeAction);
		step2(rakutenHomeAction);
		step3(rakutenHomeAction);
	}
	
	@Step("Open Rakuten Home")
	private void step1(RakutenHomeAction rakutenHomeAction) {
		rakutenHomeAction.getRakutenHomePage().open(Timeout.SHORT);
	}
	
	@Step("Scroll Window Down and Up to show Top navigation")
	private void step2(RakutenHomeAction rakutenHomeAction) {
		rakutenHomeAction.getRakutenHomePage().getTopNavBar().scrollWindowDownAndUp();
	}
	
	@Step("Check Top navigation is presented")
	private void step3(RakutenHomeAction rakutenHomeAction) {
		rakutenHomeAction.getRakutenHomePage().getTopNavBar().waitUntilPresented(Timeout.SHORT);		
	}
}
