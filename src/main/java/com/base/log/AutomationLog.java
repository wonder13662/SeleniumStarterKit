package com.base.log;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;

import com.base.testing.TestStep;
import com.base.testing.TestingConstant.TestResult;

public class AutomationLog {
	
	private int idx;
	private String testSuite;
	private String action;
	private String expected;
	private String account;
	private String actual;
	private TestResult testResult = TestResult.READY;
	private TestStep testStep;
	private StopWatch stopWatch;

	public AutomationLog(int idx, String testSuite, String action, String account, String expected) {
		this.idx = idx;
		this.testSuite = testSuite;
		this.action = action;
		this.account = account;
		this.expected = expected;
		this.stopWatch = new StopWatch();
	}
	
	public AutomationLog(int idx, TestStep testStep, String account, String expected) {
		this.idx = idx;
		this.testSuite = testStep.getTestSuite();
		this.action = testStep.getAction();
		this.account = account;
		this.expected = expected;
		this.stopWatch = new StopWatch();
		this.testStep = testStep;
	}
	
	public int getIdx() {
		return idx;
	}

	public void start() {
		stopWatch.start();
	}
	
	public void resume() {
		stopWatch.resume();
	}
	
	public void suspend() {
		stopWatch.suspend();
	}
	
	public void stop() {
		stopWatch.stop();
	}

	public String getTestSuite() {
		return testSuite;
	}

	public String getAction() {
		return action;
	}

	public String getAccount() {
		return account;
	}

	public String getExpected() {
		return expected;
	}

	public String getActual() {
		return actual;
	}

	public TestResult getTestResult() {
		return testResult;
	}

	public void setActual(String actual) {
		this.actual = actual;
		if(expected.equals(actual)) {
			testResult = TestResult.PASS;
		} else {
			testResult = TestResult.FAILED;
		}
	}

	public void setTestResult(TestResult testResult) {
		this.testResult = testResult;
	}

	public TestStep getTestStep() {
		return testStep;
	}

	public String getDuration() {
		long millisec = stopWatch.getTime();
		return String.format("%d min %d sec", 
			    TimeUnit.MILLISECONDS.toMinutes(millisec),
			    TimeUnit.MILLISECONDS.toSeconds(millisec) - 
			    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisec))
			);
	}

	public boolean isSame(AutomationLog log) {
		return idx == log.getIdx();
	}
	
	public boolean isSame(TestStep testStep) {
		return testSuite.equals(testStep.getTestSuite()) && action.equals(testStep.getAction());
	}
}
