package com.base.command;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.base.Environment;
import com.base.component.ComponentHelper;
import com.base.component.SearchStep;
import com.base.constant.BaseConstant.Browser;
import com.base.constant.BaseConstant.Timeout;
import com.base.util.FileUtil;
import com.base.util.StringUtil;

public class BaseCommand {
	
	protected Environment env;
	
	public BaseCommand(Environment env) {
		this.env = env;		
	}
	
	public Environment getEnv() {
		return env;
	}
	
	public void openUrl(String url, String expected, Timeout timeout) {
		env.getDriver().get(url);
		waitUntilUrlToBe(expected, timeout);
	}
	@Deprecated
	public void openUrl(String url, String expected) {
		env.getDriver().get(url);
		waitUntilUrlToBe(expected);
	}
	
	public void openUrl(String url, Timeout timeout) {
		if(hasUrl(url)) return;
		
		env.getDriver().get(url);
		waitUntilUrlToBe(url, timeout);
	}	
	@Deprecated
	public void openUrl(String url) {
		if(hasUrl(url)) return;
		
		env.getDriver().get(url);
		waitUntilUrlToBe(url);
	}
	
	public void openUrlNoWait(String url) {
		if(hasUrl(url)) return;
		env.getDriver().get(url);
	}
	
	public boolean doesExist(SearchStep searchStep) {
		if(searchStep == null) return false;
		
		WebElement element = searchStep.get();
		return element != null;
	}

	public boolean doesExist(String query) {
		return findAll(query).size() > 0;
	}
	
	public Select getSelect(WebElement element) {
		try {
			return new Select(element);
		} catch (Exception e) {
			return null;
		}
	}
	
	public WebElement getFirstSelectedOption(Select select) {
		try {
			return select.getFirstSelectedOption();
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<WebElement> getOptions(Select select) {
		try {
			return select.getOptions();
		} catch (Exception e) {
			return new ArrayList<WebElement>();
		}		
	}
	
	public WebElement find(String query) {
		try {
			return env.getDriver().findElement(By.cssSelector(query));
		} catch (Exception e) {
			return null;
		}
		
	}
	
	private WebElement find(SearchStep searchStep, String query) {
		SearchStep clone = searchStep.clone();
		SearchStep searchStepNext = ComponentHelper.getSearchStep(env, query);
		clone.push(searchStepNext);
		
		return clone.get();
	}
	
	public int count(String query) {
		return findAll(query).size();
	}
	
	public List<WebElement> findAll(String query) {
		try {
			return env.getDriver().findElements(By.cssSelector(query));
		} catch (NoSuchSessionException e) {
			return new ArrayList<WebElement>();
		} catch (Exception e) {
			return new ArrayList<WebElement>();
		}
	}
	
	public List<WebElement> findAll(WebElement parent, String query) {
		List<WebElement> list = new ArrayList<WebElement>();
		try {
			list = parent.findElements(By.cssSelector(query));
			if(list.size() == 0) {
				info(false, String.format("list.size() == 0 at baseCommand.findAll by query:'%s'", query));
			}
			return list;
		} catch (Exception e) {
			info(false, String.format("Exception e has occurred at baseCommand.findAll by query:'%s'", query));
			return list;
		}
	}
	
	public void waitUntilListHasMoreThan(String query, int count) {
		waitUntil(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				List<WebElement> elements = findAll(query);
				int size = elements.size();
				info(false, String.format("size:%d at waitUntilListHasMoreThan()", size));
				return elements.size() > 0;
			}
		});
	}
	
	public void waitUntilListHas(String queryList, int count) {
		waitUntil(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				
				List<WebElement> elements = findAll(queryList);
				int size = elements.size();
				info(false, String.format("size:%d at waitUntilListHas()", size));
				return size == count;
			}
		});
	}
	

	public String getText(SearchStep searchStep) {
		return getText(searchStep.get());
	}	
	
	public String getText(SearchStep searchStep, String query) {
		return getText(find(searchStep, query));
	}
	
	public String getText(String query) {
		return getText(find(query));
	}
	
	public String getText(WebElement element) {
		boolean isDebug = false;
		if(element == null) {
			info(isDebug, "element == null at BaseCommand.getText");
			return "";
		}
		
		try {
			return element.getText();
		} catch (Exception e) {
			info(isDebug, "Exception e at BaseCommand.getText");
			return "";
		}		
	}
	
	public String getInputValue(SearchStep searchStep) {
		try {
			return searchStep.get().getAttribute("value");
		} catch (Exception e) {
			return "";
		}
	}
	
	public String getAttrHref(SearchStep searchStep) {
		try {
			return searchStep.get().getAttribute("href");
		} catch (Exception e) {
			return "";
		}
	}
	
	public boolean hasAttrType(SearchStep searchStep, String expected) {
		String actual = getAttrType(searchStep);
		return StringUtil.equalsAfterTrim(actual, expected);
	}
	
	public String getAttrType(SearchStep searchStep) {
		try {
			return searchStep.get().getAttribute("type");
		} catch (Exception e) {
			return "";
		}
	}
	
	
	@Deprecated
	public void waitUntilInputValuePresented(SearchStep inputSearchStep, String expected) {
		getLongWait().until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				
				String actual = getInputValue(inputSearchStep);
				boolean isDebug = false;
				if(isDebug) {
					 env.getLogger().info(String.format("expected:%s at waitUntilTextPresented", expected));
					 env.getLogger().info(String.format("actual:%s at waitUntilTextPresented", actual));					
				}
				return StringUtil.isNotEmpty(actual) && actual.equals(expected);
			}
		});		
	}
	@Deprecated
	public void waitUntilTextPresenceOfElementLocated(String query, String text) {
		By locator = By.cssSelector(query);
		getLongWait().until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
	}

	public void waitLongUntil(ExpectedCondition<Boolean> condition) {
		getLongWait().until(condition);
	}
	
	public void waitUntil(ExpectedCondition<Boolean> condition, Timeout timeout) {
		getWait(timeout).until(condition);
	}
	
	public void waitUntil(ExpectedCondition<Boolean> condition) {
		getMidWait().until(condition);
	}	
	
	public void waitShortlyUntil(ExpectedCondition<Boolean> condition) {
		getShortWait().until(condition);
	}
	
	private WebDriverWait getWait(Timeout timeout) {
		return new WebDriverWait(env.getDriver(), timeout.getSecond(), 300);
	}
	
	public WebDriverWait getSessionExpirationWait() {
		return getWait(Timeout.SESSION_EXPIRE_WAIT_COOKIE);
	}
	
	private WebDriverWait getLongWait() {
		return getWait(Timeout.LONG);
	}
	
	private WebDriverWait getMidWait() {
		return getWait(Timeout.MID);
	}

	private WebDriverWait getShortWait() {
		return getWait(Timeout.SHORT);
	}
	
	public void waitAndGo(Function<Void, Boolean> action, Timeout timeout) {
		waitAndGo(action, timeout.getSecond());
	}
	
	public void waitAndGo(Function<Void, Boolean> action, int secs) {
		// 일정시간 동안 조건이 맞으면 특정 동작을 수행. 조건이 시간까지 발생하지 않으면 무시하고 다음으로 진행.
		boolean isConditionMet = true;
		boolean isDebug = false;
		int timer = 0;
		while (isConditionMet) {
			sleep(Timeout.TINY);
			if(timer > secs) {
				isConditionMet = false;
			} else {
				isConditionMet = action.apply(null);
			}
			
			info(isDebug, String.format("isConditionMet:%b at BaseCommand.waitAndGo()", isConditionMet));
			info(isDebug, String.format("timer:'%d' / sec:'%d' at BaseCommand.waitAndGo()", timer, secs));
			
			timer++;
		}
	}
	
	public boolean hasUrl(String expected) {
		String actual = env.getDriver().getCurrentUrl();
		info(false, String.format("actual:%s != expected:%s at BaseCommand.hasUrl()", actual, expected));					
		return StringUtil.isNotEmpty(actual) && actual.equals(expected);
	}
	
	public void waitUntilUrlToBe(String expected) {
		waitUntil(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return hasUrl(expected);
			}
		}, Timeout.LONG);
	}
	
	public void waitUntilUrlToBe(String expected, Timeout timeout) {
		waitUntil(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return hasUrl(expected);
			}
		}, timeout);
	}	
	
	public boolean hasClass(SearchStep searchStep, String className) {
		WebElement element = searchStep.get();
		return hasClass(element, className);
	}
	
	public boolean hasClass(WebElement element, String className) {
	    String classes = element.getAttribute("class");
	    for (String c : classes.split(" ")) {
	        if (c.toLowerCase().trim().equals(className.toLowerCase().trim())) {
	            return true;
	        }
	    }

	    return false;
	}
	
	public void waitUntilExist(String query) {
		SearchStep searchStep = ComponentHelper.getSearchStep(env, query);
		waitUntilExist(searchStep);
	}
	
	private void waitUntilExist(SearchStep searchStep) {
		getLongWait().until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				WebElement element = searchStep.get();
				return element != null;
			}
		});
	}
	
	public void waitUntilInvisible(String query) {
		SearchStep searchStep = ComponentHelper.getSearchStep(env, query);
		waitUntilInvisible(searchStep);
	}
	@Deprecated
	private void waitUntilInvisible(SearchStep searchStep) {
		getLongWait().until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				boolean isDisplayed = isDisplayed(searchStep);
				return !isDisplayed;
			}
		});
	}
	
	public void waitShortlyUntilVisible(String query) {
		SearchStep searchStep = ComponentHelper.getSearchStep(env, query);
		waitShortlyUntilVisible(searchStep);
	}
	
	public void waitUntilVisible(String query, Timeout timeout) {
		SearchStep searchStep = ComponentHelper.getSearchStep(env, query);
		waitUntilVisible(searchStep, timeout);
	}
	
	public void waitShortlyUntilVisible(SearchStep searchStep) {
		getShortWait().until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return isDisplayed(searchStep) && isBiggerThan(searchStep, 5, 5);
			}
		});
	}
	
	public void waitUntilVisible(SearchStep searchStep, Timeout timeout) {
		waitUntil(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return isDisplayed(searchStep) && isBiggerThan(searchStep, 5, 5);
			}
		}, timeout);
	}
	
	public void waitUntilInvisible(SearchStep searchStep, Timeout timeout) {
		waitUntil(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return !isDisplayed(searchStep);
			}
		}, timeout);
	}	
	
	public boolean isDisplayed(String query) {
		return isDisplayed(ComponentHelper.getSearchStep(env, query));
	}
	
	public boolean isDisplayed(SearchStep searchStep) {
		WebElement element = searchStep.get();
		if(element == null) {
			return false;
		}
		
		try {
			return element.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isEnabled(SearchStep searchStep) {
		WebElement element = searchStep.get();
		if(element == null) {
			return false;
		}
		
		try {
			return element.isEnabled();
		} catch (Exception e) {
			return false;
		}
	}
	
	@Deprecated
	public void waitUntilClickable(String query) {
		By locator = By.cssSelector(query);
		getLongWait().until(ExpectedConditions.elementToBeClickable(locator));
	}
	
	public void click(String query) {
		SearchStep searchStep = ComponentHelper.getSearchStep(env, query);
		click(searchStep);
	}
	
	public boolean click(SearchStep searchStep) {
		return click(searchStep, false);
	}
	
	private boolean click(SearchStep searchStep, boolean ignoreEnabled) {
		try {
			if(ignoreEnabled || isEnabled(searchStep)) {
				WebElement btn = searchStep.get();
				btn.click();
				return true;
			}
			return false;
		} catch (ElementNotInteractableException e) {
			e.printStackTrace();
			info(true, String.format("ElementNotInteractableException at BaseCommand.click"));
			return false;
		} catch (WebDriverException e) {
			e.printStackTrace();
			info(true, String.format("WebDriverException at BaseCommand.click"));
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			info(true, String.format("Exception at BaseCommand.click"));
			return false;
		}
	}	
	

	public void click(WebElement element) {
		try {
			if(element.isEnabled()) {
				element.click();
			} else {
				env.getLogger().error(String.format("clicking failed at click() :%s", element));
			}
		} catch (Exception e) {
			e.printStackTrace();
			// Ignore exception
		}
	}	
	
	public void clickByJs(String query) {
		runJS(String.format("document.querySelector('%s').click();", query));
	}
	
	public void sendKeys(SearchStep searchStep, String text) {
		try {
			searchStep.get().sendKeys(text);
		} catch (Exception e) {
			// Ignore exception
		}
	}
	
	public boolean isSelected(SearchStep target) {
		if(target == null) return false;
		return target.get().isSelected();
	}
	
	public void selectAllTextInInputFieldByJs(WebElement element) {
		runJS("arguments[0].setSelectionRange(0, arguments[0].value.length);", element);
	}
	
	public void select(Select select, int idx) {
		try {
			if(select != null) {
				select.selectByIndex(idx);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public String getCurrentUrl() {
		return env.getDriver().getCurrentUrl();
	}
	
	public int getIFrameCnt() {
		return env.getDriver().findElements(By.tagName("iframe")).size();
	}
	
	public String getCurrentWindowHandles() {
		return env.getDriver().getWindowHandle();
	}
	
	public List<String> getWindowHandles() {  
		return new ArrayList<String> (env.getDriver().getWindowHandles());
	}
	
	public String getWindowHandle(int idx) {  
		return new ArrayList<String> (env.getDriver().getWindowHandles()).get(idx);
	}
	
	public void switchToWindow(String handle) {
		env.getDriver().switchTo().window(handle);
	}
	
	public void closeWindow(int idx) {
		String handleToClose = getWindowHandle(idx);
		env.getDriver().switchTo().window(handleToClose);
		env.getDriver().close();
	}
	
	public void closeWindow(String handle) {
		env.getDriver().switchTo().window(handle);
		env.getDriver().close();
	}
	
	public void switchToFrame(int idx) {
		switchBackToParentFrame();
		env.getDriver().switchTo().frame(idx);
	}

	public void switchToFrame(String queryIframe) {
		switchBackToParentFrame();
		switchToFrame(By.cssSelector(queryIframe));
	}
	
	public void switchBackToParentFrame() {
		env.getDriver().switchTo().defaultContent();
	}
	
	private void switchToFrame(By locator) {
		WebElement element = env.getDriver().findElement(locator);
		env.getDriver().switchTo().frame(element);
	}
	
	public void sleep(Timeout timeout) {
		sleep(timeout.getSecond());
	}
	
	public void sleep(int secs) {
		try {
			Thread.sleep(secs * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}	

	public void waitUntil(SearchStep searchStep) {
		getMidWait().until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return doesExist(searchStep);
			}
		});
	}
	
	public void waitLongUntil(SearchStep searchStep) {
		getLongWait().until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return doesExist(searchStep);
			}
		});
	}	
	
	public WebElement findChild(WebElement parent, String query) {
		return findChild(parent, By.cssSelector(query));
	}
	
	private WebElement findChild(WebElement parent, By locator) {
		try {
			Assert.assertTrue(parent != null, String.format("parent is null at findChild()"));
			Assert.assertTrue(locator != null, String.format("locator is null at findChild()"));
			List<WebElement> elements = null;
			
			elements = parent.findElements(locator);
			if(elements.size() == 0) {
				return null;
			}
			
			return parent.findElement(locator);
		} catch (StaleElementReferenceException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	
	public String runJS(String script) {
		Object result = ((JavascriptExecutor) env.getDriver()).executeScript(script);
		if(result != null) {
			return result.toString(); 
		}
		return "";
	}
	
	private String runJS(String script, WebElement element) {
		Object result = ((JavascriptExecutor) env.getDriver()).executeScript(script, element);
		if(result != null) {
			return result.toString(); 
		}
		return "";
	}
	
	public void scrollTop(String query) {
		((JavascriptExecutor) env.getDriver()).executeScript(String.format("document.querySelector('%s').scrollIntoView();", query));
	}
	
	public void scrollTop(SearchStep searchStep) {
		if(!doesExist(searchStep)) {
			return;
		}
		WebElement element = searchStep.get();
		((JavascriptExecutor) env.getDriver()).executeScript("arguments[0].scrollIntoView();", element);
	}
	
	public void setVisibility(String query, boolean isVisible) {
		if(isDisplayed(query)) return;
		SearchStep target = ComponentHelper.getSearchStep(env, query);
		setVisiblity(target, isVisible);
	}
	
	public void setVisiblity(SearchStep searchStep, boolean shouldBeVisible) {
		if(!doesExist(searchStep)) return;
		
		String queryVisible = "arguments[0].style.cssText += ';visibility:visible !important;'";
		String queryHidden = "arguments[0].style.cssText += ';visibility:hidden !important;'";
		String query = (shouldBeVisible)?queryVisible:queryHidden;
		
		WebElement element = searchStep.get();
		((JavascriptExecutor) env.getDriver()).executeScript(query, element);
		
		if(shouldBeVisible) {
			waitUntilVisible(searchStep, Timeout.SHORT);
		} else {
			waitUntilInvisible(searchStep, Timeout.SHORT);
		}
	}

	public void setDisplayBlocked(String query) {
		setDisplayBlockedNoWait(query);
		waitUntilVisible(query, Timeout.SHORT);
	}
	
	public void setDisplayBlockedNoWait(String query) {
		if(isDisplayed(query)) return;
		((JavascriptExecutor) env.getDriver()).executeScript(String.format("document.querySelector('%s').style.cssText += ';display:block !important;'", query));
	}
	
	public void setDisplayBlocked(SearchStep searchStep) {
		if(!doesExist(searchStep)) {
			return;
		}
		WebElement element = searchStep.get();
		((JavascriptExecutor) env.getDriver()).executeScript("arguments[0].style.cssText += ';display:block !important;'", element);
		waitUntilVisible(searchStep, Timeout.SHORT);
	}
	
	public void setDisplayHidden(String query) {
		if(!isDisplayed(query)) return;
		((JavascriptExecutor) env.getDriver()).executeScript(String.format("document.querySelector('%s').style.cssText += ';display:none !important;'", query));
		waitUntilInvisible(ComponentHelper.getSearchStep(env, query), Timeout.SHORT);
	}
	
	public void setDisplayHiddenNoWait(String query) {
		if(!isDisplayed(query)) return;
		((JavascriptExecutor) env.getDriver()).executeScript(String.format("document.querySelector('%s').style.cssText += ';display:none !important;'", query));
	}
	
	public void setDisplayHidden(SearchStep searchStep) {
		if(!doesExist(searchStep)) {
			return;
		}
		WebElement element = searchStep.get();
		((JavascriptExecutor) env.getDriver()).executeScript("arguments[0].style.cssText += ';display:none !important;'", element);
		waitUntilInvisible(searchStep);
	}
	
	public String getInnerHtml(SearchStep searchStep) {
		if(!doesExist(searchStep)) {
			return "";
		}
		WebElement element = searchStep.get();
		return (String)((JavascriptExecutor) env.getDriver()).executeScript("return arguments[0].parentElement.innerHTML", element);
	}
	
	public void addCookie(String name, String value, String domain, String path, Date date) {
		env.getDriver().manage().addCookie(new Cookie(name, value, domain, path, date));
	}
	
	public String getCookie(String name) {
		Cookie cookie = env.getDriver().manage().getCookieNamed(name);
		if(cookie == null) {
			return "";
		}
		return cookie.getValue();
	}
	
	public void deleteCookie(String name) {
		env.getDriver().manage().deleteCookieNamed(name);
	}
	
	public void deleteAllCookies() {
		env.getDriver().manage().deleteAllCookies();
		sleep(Timeout.SHORT);
	}
	
	public void takeScreenshot(String fileName) {
		String filePath = FileUtil.getScreenShotDirPath() + File.separator + fileName+".png";
		takeScreenshotToPath(filePath);
	}
	
	public void takeScreenshotToPath(String filePath) {
		try {
			TakesScreenshot screenshot=(TakesScreenshot)env.getDriver();
			File src=screenshot.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(src, new File(filePath));
			System.out.println(String.format("Successfully captured a screenshot at %s", filePath));
		} catch (Exception e) {
			System.out.println("Exception while taking screenshot "+e.getMessage());
		}
	}
	
	
	public void setAlertAutoCloseByJS() {
		runJS("window.alert = function(){ return true;}");
	}	
	
	public void waitUntilAlertPresented(Timeout timeout) {
		waitUntil(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return hasAlert();
			}
		}, timeout);
	}	
	
	private boolean hasAlert() {
		boolean isDebug = false;
		try {
			Alert alert = env.getDriver().switchTo().alert();
			String text = alert.getText();
			boolean isNotEmpty = StringUtil.isNotEmpty(text);
			info(isDebug, String.format("text:'%s' at BaseCommand.hasAlert()", text));
			info(isDebug, String.format("isNotEmpty:'%b' at BaseCommand.hasAlert()", isNotEmpty));
			
			return isNotEmpty;
		} catch (Exception e) {
			info(isDebug, String.format("Exception e at BaseCommand.hasAlert()"));
			return false;
		}
	}
	
	public void acceptAlert() {
		try {
			Alert alert = env.getDriver().switchTo().alert();
			alert.accept();
		} catch (Exception e) {
			// TODO 예외처리
		}		
	}
	
	public boolean isRadioBtnCheckedByJs(String query) {
		String script = String.format("document.querySelector('%s').checked", query);
		String result = runJS(script);
		
		return Boolean.valueOf(result);
	}
	
	public void reloadPage() {
		env.getDriver().navigate().refresh();
	}
	
	public void info(boolean isDebug, String msg) {
		if(isDebug) {
			env.getLogger().info(msg);
		}
	}
	
	public void error(String msg) {
		env.getLogger().error(msg);
	}
	
	public void openNewTab(String url) {
		String script = String.format("window.open('%s','_blank');", url);
		runJS(script);
	}
	
	public void openNewTab() {
		String script = "window.open('about:blank','_blank');";
		runJS(script);
	}
	
	public void openUrlByJS(String url) {
		String script = String.format("window.open('%s','_self');", url);
		runJS(script);
	}
	
	public void waitUntilHasMoreTabThan(int cnt, Timeout timeout) {
		waitUntil(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				ArrayList<String> tabs = new ArrayList<String> (env.getDriver().getWindowHandles());
				return tabs.size() > cnt;
			}
		}, timeout);
	}
	
	public void switchTab(int idx) {
		if(env.getBrowser() == Browser.SAFARI) return;
		
		ArrayList<String> tabs = new ArrayList<String> (env.getDriver().getWindowHandles());
		if(idx < tabs.size() ) {
			env.getDriver().switchTo().window(tabs.get(idx));			
		}
	}
	
	public void switchToLastTab() {
		if(env.getBrowser() == Browser.SAFARI) return;
		
		ArrayList<String> tabs = new ArrayList<String> (env.getDriver().getWindowHandles());
		env.getDriver().switchTo().window(tabs.get(tabs.size() - 1));
	}	
	
	public void closeCurrentTab() {
		env.getDriver().close();
	}
	
	public SearchStep getItem(String queryList, String queryTitle, String expected) {
		return getItem(queryList, queryTitle, expected, false);
	}
	
	public SearchStep getItem(String queryList, String queryTitle, String expected, boolean isStrict) {
		Function<WebElement, Boolean> filterOne = 
				(WebElement item)-> {
						if(item == null) {
							return false;
						}
						WebElement element = findChild(item, queryTitle);
						if(element == null) {
							return false;
						}
						String actual = getText(element);
						String msg = String.format("actual:'%s' == expected:'%s' at getItem", actual, expected);
						info(false, msg);
						
						if(isStrict) {
							boolean isSame = StringUtil.equalsAfterTrim(actual, expected);
							return isSame;
						} else {
							boolean doesContain = StringUtil.contains(actual, expected);
							return doesContain;
						}
					};
					
		return ComponentHelper.getSearchStepByFilterOne(env, queryList, filterOne);
	}
	
	public HashMap<String, String> getMap(String queryList, String queryTitle, String queryValue) {
		HashMap<String, String> map = new HashMap<String, String>(); 
		List<WebElement> elementList = findAll(queryList);
		for (WebElement element : elementList) {
			WebElement keyEl = findChild(element, queryTitle);
			if(keyEl == null) continue;
			String key = getText(keyEl);
			if(StringUtil.isEmpty(key)) continue;
			
			WebElement valueEl = findChild(element, queryValue);
			if(valueEl == null) continue;
			String value = getText(valueEl);
			if(StringUtil.isEmpty(value)) continue;
			
			map.put(key, value);
		}
		
		return map;
	}
	
	public Map<String, SearchStep> getSearchStepMap(String queryList, String queryTitle, int idxBegin) {
		Map<String, SearchStep> map = new HashMap<String, SearchStep>();		
		ArrayList<String> keyList = getList(queryList, queryTitle);
		for (int i = 0; i < keyList.size(); i++) {
			String key = keyList.get(i);
			SearchStep target = ComponentHelper.getSearchStep(env, queryList, i);
			map.put(key, target);
		}
		
		return map;
	}
	
	public Map<String, SearchStep> getSearchStepMap(String queryList, String queryTitle, boolean removeDecoration) {
		return getSearchStepMap(queryList, queryTitle, 0, removeDecoration);
	}
	
	public Map<String, SearchStep> getSearchStepMap(String queryList, String queryTitle, int idxBegin, boolean removeDecoration) {
		Map<String, SearchStep> map = new HashMap<String, SearchStep>();		
		List<String> keyList = getList(queryList, queryTitle, removeDecoration);
		List<SearchStep> rowList = getSearchStepList(queryList, queryTitle);
		if(keyList.size() != rowList.size()) {
			return map;
		}
		
		for (int i = 0; i < rowList.size(); i++) {
			if(i < idxBegin) {
				continue;
			}
			map.put(keyList.get(i).trim(), rowList.get(i));
		}
		return map;
	}
	
	private List<SearchStep> getSearchStepList(String queryList, String queryTitle) {
		List<SearchStep> searchStepList = new ArrayList<SearchStep>(); 
		List<WebElement> elementList = findAll(queryList);
		WebElement element = null;
		WebElement titleElement = null;
		String titleText = null;
		for (int i = 0; i < elementList.size(); i++) {
			element = elementList.get(i);
			titleElement = findChild(element, queryTitle);
			titleText = getText(titleElement);
			if(titleElement != null && !StringUtil.isEmpty(titleText)) {
				searchStepList.add(ComponentHelper.getSearchStep(env, queryList, i));
			}
		}
		
		return searchStepList;
	}
	
	
	public Map<String, SearchStep> getSearchStepMap(String queryList, String queryTitle, int idxBegin, Function<String, String> customizeKey) {
		Map<String, SearchStep> map = new HashMap<String, SearchStep>();		
		ArrayList<String> keyList = getList(queryList, queryTitle);
		for (int i = 0; i < keyList.size(); i++) {
			String key = keyList.get(i);
			key = customizeKey.apply(key);
			SearchStep target = ComponentHelper.getSearchStep(env, queryList, i);
			map.put(key, target);
		}
		
		return map;
	}	
	
	public ArrayList<String> getList(String queryList, String queryTitle) {
		return getList(queryList, queryTitle, true);
	}
	
	public ArrayList<String> getList(String queryList, String queryTitle, boolean removeDecoration) {
		ArrayList<String> list = new ArrayList<String>(); 
		List<WebElement> elementList = findAll(queryList);
		for (WebElement element : elementList) {
			String key = "";
			if(StringUtil.isEmpty(queryTitle)) {
				key = getText(element);
			} else {
				WebElement keyEl = findChild(element, queryTitle);
				if(keyEl == null) continue;
				key = getText(keyEl);
			}
			if(StringUtil.isEmpty(key)) continue;
			list.add(key);
		}
		
		return list;
	}
	
	public Map<String, SearchStep> getSearchStepMap(SearchStep root, String queryList, String queryTitle) {
		return getSearchStepMap(root, queryList, queryTitle, 0);
	}
	
	public Map<String, SearchStep> getSearchStepMap(SearchStep root, String queryList, String queryTitle, int idxBegin) {
		return getSearchStepMap(root, queryList, queryTitle, idxBegin, true);
	}
	
	public Map<String, SearchStep> getSearchStepMap(SearchStep root, String queryList, String queryTitle, int idxBegin, boolean removeDecoration) {
		Map<String, SearchStep> map = new HashMap<String, SearchStep>();		
		ArrayList<String> keyList = getList(root, queryList, queryTitle, removeDecoration);
		for (int i = 0; i < keyList.size(); i++) {
			String key = keyList.get(i);
			SearchStep target = ComponentHelper.getSearchStep(env, queryList, i);
			map.put(key, target);
		}
		
		return map;
	}	
	
	public ArrayList<String> getList(SearchStep root, String queryList, String queryTitle, boolean removeDecoration) {
		ArrayList<String> list = new ArrayList<String>();
		List<WebElement> elementList = findAll(root.get(), queryList);
		for (WebElement element : elementList) {
			String key = "";
			if(StringUtil.isEmpty(queryTitle)) {
				key = getText(element);
			} else {
				WebElement keyEl = findChild(element, queryTitle);
				if(keyEl == null) continue;
				key = getText(keyEl);
			}
			if(StringUtil.isEmpty(key)) continue;
			
			list.add(key);
		}
		
		return list;
	}	
	
	public void waitUntilStopped(SearchStep searchStep, Timeout timeout) {
		boolean isDebug = false;
		
		ExpectedCondition<Boolean> expectedCondition = new ExpectedCondition<Boolean>() {
			private int targetYPos = Integer.MAX_VALUE;
			public Boolean apply(WebDriver driver) {
				WebElement element = searchStep.get();
				if(element == null) {
					return false;
				}
				Point point = searchStep.getPoint();
				if(point == null) {
					return false;
				}
				int curYPos = point.getY();
				
				info(isDebug, String.format("targetYPos:'%d' == curYPos:'%d' at waitUntilStopped()", targetYPos, curYPos));
				
				if(targetYPos != curYPos) {
					targetYPos = curYPos;
					return false;
				}
				
				return true;
			}
		};
		
		waitUntil(expectedCondition, timeout);
	}
	
	public int getWindowHandleIdx(Function<String, Boolean> action) {
		List<String> windowHandles = getWindowHandles();
		env.log(String.format("\nwindowHandles.size():'%d' at getWindowHandleIdx", windowHandles.size()));
		for (int i = 0; i < windowHandles.size(); i++) {
			String handle = windowHandles.get(i);
			env.log(String.format("\ni:'%d' at getWindowHandleIdx", i));
			boolean success = action.apply(handle);
			env.log(String.format("success:'%b' at getWindowHandleIdx", success));
			if(success) {
				return i;
			}
		}
		return -1;
	}
	
	public void sendGetRequestByJS(String url) {
		String script = String.format("var xhttp = new XMLHttpRequest();xhttp.open('GET', '%s', false);xhttp.send();", url);
		runJS(script);
	}
	
	public boolean isBiggerThan(SearchStep searchStep, int width, int height) {
		int heightFromView = getHeight(searchStep);
		int widthFromView = getWidth(searchStep);
		
		return height < heightFromView && width < widthFromView;
	}
	
	private Rectangle getRect(SearchStep searchStep) {
		try {
			WebElement element = searchStep.get();
			if(element != null) {
				return element.getRect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private int getWidth(SearchStep searchStep) {
		try {
			Rectangle rect = getRect(searchStep);
			if(rect != null) {
				return rect.height;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	private int getHeight(SearchStep searchStep) {
		try {
			Rectangle rect = getRect(searchStep);
			if(rect != null) {
				return rect.width;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return -1;
	}
}
