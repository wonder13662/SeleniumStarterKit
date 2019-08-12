package com.base;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.monte.screenrecorder.ScreenRecorder;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.base.browser.BrowserDriverFactory;
import com.base.constant.BaseConstant.Browser;
import com.base.enums.EnumUtil;
import com.base.log.AutomationLog;
import com.base.log.TestLogList;
import com.base.testing.TestingConstant.TestResult;
import com.base.util.StringUtil;
import com.cloud.testing.constant.BrowserStackConstant;
import com.cloud.testing.constant.CloudTestingBrowser;
import com.cloud.testing.constant.CloudTestingPlatform;
import com.cloud.testing.constant.CloudTestingServiceConstant.CloudTestingService;
import com.cloud.testing.constant.LambdaTestConstant;
import com.cloud.testing.constant.SauceLabsConstant;
import com.cloud.testing.data.CloudTestingConfiguration;
import com.cloud.testing.util.BrowserStackUtil;
import com.cloud.testing.util.LambdaTestUtil;
import com.cloud.testing.util.SauceLabsUtil;
import com.service.constant.ServiceConstant.ServerStage;

public class BaseTest {
	private WebDriver driver;
	private Logger logger;
	private Browser browser;
	private ScreenRecorder screenRecorder;
	protected Environment env;
	
	@Parameters({ 
		"browser", 
		"service", 
		"stage", 
		"platform", 
		"cloudTesting", 
		"isSwingvyLogging" })
	@BeforeMethod(alwaysRun = true)
	protected void setUp(
			@Optional("chrome") String browserName, 
			@Optional("hris") String service, 
			@Optional("alpha") String stageStr, 
			@Optional("platform") String platform, 
			@Optional("") String cloudTestingStr,
			@Optional("false") boolean isSwingvyLogging,  
			ITestContext ctx, 
			Method method) {
		String methodName = method.getName();		
		String className = this.getClass().getSimpleName();
		logger = LogManager.getLogger(String.format("%s|%s", className, methodName));
		this.browser = EnumUtil.fromNameOnView(Browser.class, browserName).get();
		
		CloudTestingService ctService = CloudTestingService.get(cloudTestingStr);
		CloudTestingConfiguration conf = getCloudTestingConfiguration(ctService, className, methodName, stageStr, platform, browserName);
		if(CloudTestingService.SAUCE_LABS == ctService) {
			driver = SauceLabsUtil.getDriver(conf);
		} else if(CloudTestingService.LAMBDA_TEST == ctService) {
			driver = LambdaTestUtil.getDriver(conf);
		} else if(CloudTestingService.BROWSER_STACK == ctService) {
			driver = BrowserStackUtil.getDriver(conf);
		} else {
			driver = getLocalDriver(browserName, logger);
		}
		
		ServerStage serverStage = EnumUtil.fromNameOnView(ServerStage.class, stageStr).get();		
		
		this.env = new Environment(driver, logger, serverStage, browser);
		if(isSwingvyLogging) {
			TestLogList logList = new TestLogList(className, browser);
			logList.start();
			this.env.setLogList(logList);
		}
		
//		screenRecorder = VideoUtil.startRecord();
	}
	
	private CloudTestingConfiguration getCloudTestingConfiguration(
			CloudTestingService ctService, 
			String className, 
			String methodName, 
			String stage, 
			String platformName, 
			String browserName) {
		
    	String testName = getTestTitle(className, methodName);
    	String buildName = getTestTitle(stage, className);
    	List<String> tags = Arrays.asList(stage, className, methodName);
    	
    	CloudTestingPlatform platform = null;
    	CloudTestingBrowser browser = null;
    	if(ctService == CloudTestingService.SAUCE_LABS) {
    		browser = SauceLabsConstant.Browser.get(browserName);
    		platform = SauceLabsConstant.Platform.get(platformName);
    	} else if(ctService == CloudTestingService.LAMBDA_TEST) {
    		browser = LambdaTestConstant.Browser.get(browserName);
    		platform = LambdaTestConstant.Platform.get(platformName);
    	} else if(ctService == CloudTestingService.BROWSER_STACK) {
    		browser = BrowserStackConstant.Browser.get(browserName);
    		platform = BrowserStackConstant.Platform.get(platformName);
    	} else {
    		return null;
    	}
    	
    	return new CloudTestingConfiguration(testName, buildName, tags, platform, browser);		
	}
	
	private String getTestTitle(String title, String subTitle) {
		return String.format("[%s][%s]", title, subTitle);
	}
	
	private WebDriver getLocalDriver(String browserName, Logger logger) {
		BrowserDriverFactory factory = new BrowserDriverFactory(browserName, logger);
		WebDriver driver = factory.createDriver();
		driver.manage().window().maximize();
		
		return driver;
	}
	
	@Parameters({ "cloudTesting", "isSwingvyLogging" })
	@AfterMethod(alwaysRun = true)
	protected void tearDown(
			@Optional("") String cloudTestingStr,  
			@Optional("false") boolean isSwingvyLogging, 
			ITestResult result) {
		
		CloudTestingService ctService = CloudTestingService.get(cloudTestingStr);
		
		if(ctService == CloudTestingService.SAUCE_LABS) {
			((JavascriptExecutor)driver).executeScript("sauce:job-result=" + (result.isSuccess() ? "passed" : "failed"));
		} else if(ctService == CloudTestingService.LAMBDA_TEST) {
			((JavascriptExecutor) driver).executeScript("lambda-status=" + result.isSuccess());
		} else if(ctService == CloudTestingService.BROWSER_STACK) {
			
		} else {
			if(ITestResult.FAILURE==result.getStatus()){
				env.getBaseCommand().takeScreenshot(result.getName());
			}		
			logger.info("Close driver");
		}
		
		if(isSwingvyLogging) {
			if(ITestResult.FAILURE==result.getStatus()){
				AutomationLog log = env.getLogList().getWorkingLog();
				if(log != null) {
					env.getLogList().getWorkingLog().setTestResult(TestResult.FAILED);
					env.getLogList().setTestResult(TestResult.FAILED);
					// 실패 원인을 스크린샷으로 기록
//					SwingvyTestLogExcelLogger.takeScreenshot(env, env.getLogList());
				}
			} else {
				env.getLogList().setTestResult(TestResult.PASS);
			}
			
			// 테스트 스위트 종료. 결과를 엑셀에 기록
			env.getLogList().suspend();
			Assert.assertTrue(StringUtil.isNotEmpty(env.getLogList().getXlsxFileName()));
//			SwingvyTestLogExcelLogger.write(env.getLogList());
		}
		
		
//		VideoUtil.stopRecording(screenRecorder);
		
		driver.quit();
	}
}
