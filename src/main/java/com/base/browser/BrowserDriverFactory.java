package com.base.browser;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import com.base.constant.BaseConstant.Browser;
import com.base.constant.BaseConstant.OS;
import com.base.enums.EnumUtil;
import com.base.util.FileUtil;

public class BrowserDriverFactory {
	
	private ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();
	private Browser browser;
	private Logger log;
	
	public BrowserDriverFactory(String browserName, Logger log) {
		this.browser = EnumUtil.fromNameOnView(Browser.class, browserName).get();
		this.log = log;
	}
	
	public WebDriver createDriver() {
		// Create driver
		log.info("Create driver: " + browser);
		switch (browser) {
		case CHROME:
			driver.set(createChromeDriver());
			break;
		case FIREFOX:
			driver.set(createFirefoxDriver());
			break;
		case SAFARI:
			driver.set(createSafariDriver());
			break;
		case IE11:
			driver.set(createIE11Driver());
			break;
		case EDGE:
			driver.set(createEdgeDriver());
			break;
		default:
			System.out.println("Do not know how to start: " + browser + ", starting chrome.");
			driver.set(createChromeDriver());
			break;
		}
		
		return driver.get();
	}
	
	private ChromeOptions setDownloadPath(ChromeOptions options) {
		// http://makeseleniumeasy.com/2018/08/25/how-to-change-default-download-directory-for-chrome-browser-in-selenium-webdriver/
		Map<String, Object> prefs = new HashMap<String, Object>();
        String downloadPath = FileUtil.getAbsoluteDownloadPath();
        prefs.put("download.default_directory", downloadPath);
        options.setExperimentalOption("prefs", prefs);
		
		return options;
	}
	
	private WebDriver createChromeDriver() {
		// https://thefriendlytester.co.uk/2017/04/new-headless-chrome-with-selenium.html
		ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("disable-infobars");
        chromeOptions.addArguments("start-maximized");		
//		chromeOptions.addArguments("--headless");
        
        chromeOptions = setDownloadPath(chromeOptions);
		
		String property = "webdriver.chrome.driver";
		String path = "";
		String osName = System.getProperty("os.name");
		
		OS os = EnumUtil.fromNameOnView(OS.class, osName).get();
		switch (os) {
		case MAC:
			path = Paths.get("src/main/resources/webdriver/chrome/chromedriver_mac64/chromedriver").toString();
			System.setProperty(property, path);
			break;
		case WINDOWS:
			path = Paths.get("src/main/resources/webdriver/chrome/chromedriver_win32/chromedriver.exe").toString();
			log.info("path: " + path);
			System.setProperty(property, path);
			break;
		case LINUX:
			path = Paths.get("src/main/resources/webdriver/chrome/chromedriver_linux64/chromedriver").toString();
			System.setProperty(property, path);
			break;
		default:
			path = Paths.get("src/main/resources/webdriver/chrome/chromedriver_linux64/chromedriver").toString();
			System.setProperty(property, path);
			break;
		}
		
		return new ChromeDriver(chromeOptions);
	}	
	
	private WebDriver createFirefoxDriver() {
		
		String property = "webdriver.gecko.driver";
		String path = "";		
		String osName = System.getProperty("os.name");
		OS os = EnumUtil.fromNameOnView(OS.class, osName).get();
		switch (os) {
		case MAC:
			path = Paths.get("src/main/resources/webdriver/firefox/geckodriver-v0.24.0-macos/geckodriver").toString();
			System.setProperty(property, path);
			break;
		case WINDOWS:
			path = Paths.get("src/main/resources/webdriver/firefox/geckodriver-v0.24.0-win64/geckodriver.exe").toString();
			System.setProperty(property, path);
			break;
		case LINUX:
			path = Paths.get("src/main/resources/webdriver/firefox/geckodriver-v0.24.0-linux64/geckodriver").toString();
			System.setProperty(property, path);
			break;
		default:
			path = Paths.get("src/main/resources/webdriver/firefox/geckodriver-v0.24.0-macos/geckodriver").toString();
			System.setProperty(property, path);
			break;
		}
		
		return new FirefoxDriver();
	}	
	
	private WebDriver createSafariDriver() {
		SafariOptions so = new SafariOptions();
		so.setUseTechnologyPreview(true);
		
		return new SafariDriver(so);
	}
	
	private WebDriver createIE11Driver() {
		String property = "webdriver.ie.driver";		
		// IE - 32bit  cannot handle cookies
//		String path = Paths.get("src/main/resources/webdriver/ie/win32/IEDriverServer.exe").toString();
		String path = Paths.get("src/main/resources/webdriver/ie/x64/IEDriverServer.exe").toString();
		System.setProperty(property, path);
		
		// https://github.com/SeleniumHQ/selenium/wiki/DesiredCapabilities
		// https://github.com/SeleniumHQ/selenium/wiki/InternetExplorerDriver#multiple-instances-of-internetexplorerdriver
		DesiredCapabilities cap = DesiredCapabilities.internetExplorer();
		cap.setCapability("ie.forceCreateProcessApi", true);
		cap.setCapability("ie.ensureCleanSession", true);
		
		return new InternetExplorerDriver(new InternetExplorerOptions(cap));
	}
	
	private WebDriver createEdgeDriver() {
		String property = "webdriver.edge.driver";
		String path = "";
		
		String osName = System.getProperty("os.name");
		OS os = EnumUtil.fromNameOnView(OS.class, osName).get();
		switch (os) {
		case MAC:
			path = Paths.get("src/main/resources/webdriver/edge/mac/msedgedriver").toString();
			System.setProperty(property, path);
			break;
		case WINDOWS:
			path = Paths.get("src/main/resources/webdriver/edge/MicrosoftWebDriver.exe").toString();
			System.setProperty(property, path);
			break;
		default:
			path = Paths.get("src/main/resources/webdriver/edge/mac/msedgedriver").toString();
			System.setProperty(property, path);
			break;
		}
		System.setProperty(property, path);
		
		return new EdgeDriver();
	}
}
