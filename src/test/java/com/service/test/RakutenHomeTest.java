package com.service.test;

import org.testng.annotations.Test;

import com.base.BaseTest;
import com.base.constant.BaseConstant.Timeout;
import com.service.action.RakutenHomeAction;

public class RakutenHomeTest extends BaseTest {
	
	@Test
	public void testRakutenHome() {
		RakutenHomeAction rakutenHomeAction = env.getActionFactory().getAction(RakutenHomeAction.class);
		rakutenHomeAction.getRakutenHomePage().open(Timeout.SHORT);
		rakutenHomeAction.getRakutenHomePage().getTopNavBar().scrollWindowDownAndUp();
		rakutenHomeAction.getRakutenHomePage().getTopNavBar().waitUntilPresented(Timeout.SHORT);
	}
}
