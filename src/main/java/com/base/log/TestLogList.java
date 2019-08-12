package com.base.log;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.StopWatch;

import com.base.constant.BaseConstant.Browser;
import com.base.testing.TestStep;
import com.base.testing.TestingConstant.TestResult;
import com.base.util.StringUtil;
import com.base.util.TimeUtil;
import com.base.util.TimeUtil.TimeFormat;

public class TestLogList {
	
	private int idx = 0;
	private final String title;
	private String subTitle;
	private final Browser browser;
	private String startAt;
	private StopWatch stopWatch;
	private TestResult testResult = TestResult.NOT_IMPLEMENTED;
	private List<AutomationLog> logList = new ArrayList<AutomationLog>();
	private AutomationLog lastLog;

	public TestLogList(String title, Browser browser) {
		this.title = title;
		this.browser = browser;
		this.stopWatch = new StopWatch();
	}
	
	public void start() {
		startAt = TimeUtil.getNow(TimeFormat.YMDHMS);
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
	
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	
	public String getTitles() {
		String titleWithUnderscore = title.replaceAll(" ", "_");
		if(StringUtil.isNotEmpty(subTitle)) {
			String subTitleWithUnderscore = subTitle.replaceAll(" ", "_");
			return String.format("%s_%s", titleWithUnderscore, subTitleWithUnderscore);
		}
		return titleWithUnderscore;
	}

	public String getNameForScreenshot() {
		return String.format("%s_", startAt);
	}
	
	public String getName() {
		return String.format("%s_%s_%s_%s", startAt, testResult, getTitles(), browser.getNameOnView());
	}
	
	public String getXlsxFileName() {
		return String.format("%s.xlsx", getName());
	}
	
	public AutomationLog createLog(String testStep, String action, String account, String expected) {
		AutomationLog log = new AutomationLog(idx++, testStep, action, account, expected);
		logList.add(log);
		return log;
	}
	
	public AutomationLog createLog(TestStep testStep, String account, String expected) {
		AutomationLog log = new AutomationLog(idx++, testStep, account, expected);
		logList.add(log);
		return log;
	}
	
	public void addLog(AutomationLog log) {
		logList.add(log);
	}
	
	public AutomationLog getLog(TestStep testStep) {
		for (AutomationLog log : logList) {
			if(log.isSame(testStep)) {
				lastLog = log; 
				return log;
			}
		}
		
		return null;
	}
	
	public void updateLog(AutomationLog logNext) {
		logList = logList.stream().map(log -> log.isSame(logNext)?logNext:log).collect(Collectors.toList());
	}

	public List<AutomationLog> getLogList() {
		return logList;
	}

	public void setTestResult(TestResult testResult) {
		this.testResult = testResult;
	}

	public AutomationLog getWorkingLog() {
		return lastLog;
	}
	
	public void setWorkingLog(AutomationLog log) {
		lastLog = log;
	}
}