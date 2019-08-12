package com.service.test;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.base.BaseTest;
import com.service.action.RakutenHomeAction;

public class RakutenHomeTest extends BaseTest {
	
	@Test
	@Parameters({ "email", "password" })
	public void logInTest(String email, String password) {
		RakutenHomeAction rakutenHomeAction = env.getActionFactory().getAction(RakutenHomeAction.class);
		rakutenHomeAction.getRakutenHomePage();
	}
}
