# Benefits of Selenium Starter Kit
* Test results by Allure and Excel sheet
* Easily maintainable structures
	* Action - Page - Component hierarchy
* Google API Wrappers - Gmail
* Helpful libraries 
	* Handling DOM element
	* Static file management
	* Calculation of date, time, money, currency
	* Verifying file download(Chrome)
	* Excel Utility
* Cloud testing platform examples (Saucelabs, Lambda test, Browser stack)

# How to setup Selenium(Required)
1. Download Java SE [Click](https://www.oracle.com/technetwork/java/javase/downloads/index.html)
2. Download Eclipse [Click](https://www.eclipse.org/downloads/)
3. Download Apache maven [Click](https://maven.apache.org/download.cgi)
4. Install Maven on Mac OSX [Click](https://www.mkyong.com/maven/install-maven-on-mac-osx/)
5. Install Maven on Windows [Click](https://www.mkyong.com/maven/how-to-install-maven-in-windows/)
6. Configure Maven Compiler plugin [Click](https://maven.apache.org/plugins/maven-compiler-plugin/)
7. Eclipse > Help > Install new software and install TestNG(http://beust.com/eclipse)

# How to install WebDriver
1. Chrome, Firefox, Edge[Click](https://www.seleniumhq.org/download/)

# How to use TestNG
Official documents[Click](https://testng.org/doc/documentation-main.html)

# How to run your first test
1. Run test file - "rakuten.home.test.xml" by right clicking and Run as "TestNG Suite"

# How to setup Selenium(Optional)
1. Testing with WebDriver in Safari [Click](https://developer.apple.com/documentation/webkit/testing_with_webdriver_in_safari)
2. Microsoft IE11 [Click](https://www.seleniumhq.org/download/)
3. Microsoft WebDriver [Click](https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/)
4. Microsoft IE11 Setup from Selenium HQ github [Click](https://github.com/SeleniumHQ/selenium/wiki/InternetExplorerDriver#required-configuration)
5. Multiple IE11 Setup from Selenium HQ github [Click](https://github.com/SeleniumHQ/selenium/wiki/InternetExplorerDriver#multiple-instances-of-internetexplorerdriver)
5. Microsoft IE11 Setup [Click](https://youtu.be/GxTHU_91Z1Q?t=529)
6. (*Required) Microsoft IE11 needs to turn off "Enable protected mode" at Internet option > Security
![Alt text](http://seleniumquery.github.io/images/ie-driver-protected-mode-disable.png)
7. Selenium grid for the parallel executions in mutiple machines. [Click](https://github.com/SeleniumHQ/selenium/wiki/Grid2)

# How to setup Allure - Test result visualisation
0. Warning: pom.xml in this project has been modifed already. So please leave it intact.  
1. Install and Set up [Click](https://docs.qameta.io/allure/#_get_started)

# How to see your test result by Allure
```
## Command "servce" creates report from test result
$ allure serve ./allure-results
```

# How to generate Allure reports
```
$ allure generate allure-results -o allure-reports
``` 

# How to setup Webdriver of EdgeHTML18(or Higher)
```
You can choose one of them
1. Search “Manage optional features” from Start, then select “Add a Feature,” “WebDriver.”
2. Install via DISM by running the following command in an elevated command prompt: DISM.exe /Online /Add-Capability /CapabilityName:Microsoft.WebDriver~~~~0.0.1.0
```
[Click](https://blogs.windows.com/msedgedev/2018/06/14/webdriver-w3c-recommendation-feature-on-demand/#Rg8g2hRfjBQQVRXy.97)

# Package structures
1. src/main/java
- It contains Page component object, Page object, Action object
2. src/main/resources
- It contains Static files. Webdriver, Images, Excel, Json
3. src/test/java
- It contains actual test runners.
4. src/test/resource
- It contains test execution script files.

# Class definition
1. Command: Atomic event which user can trigger on the web ex) Click, Select, Move to a specific page, etc.
2. Component: Recognizable distinctive elements on the page ex) Rakuten home navigation top (Panel, Table, Dropdown, Button) 
3. Page: The area user can see and control on the browser ex) Rakuten home page.
4. Action: The tasks user want to complete ex) Open top navition at Rakuten home page and verify its menu names

# Bug history
1. Nov 9, 2018 Safari element.getLocation throws a nullPointerException [Click](https://github.com/SeleniumHQ/selenium/issues/6637)
2. Dec 2, 2017 Using Safari Technology Preview with Selenium WebDriver [Click](https://macops.ca/using-safari-technology-preview-with-selenium-webdriver/)
3. May 21, 2016 The getCurrentUrl() method for IE WebDriver in Selenium only returning localhost [Click](https://stackoverflow.com/questions/37197291/the-getcurrenturl-method-for-ie-webdriver-in-selenium-only-returning-localhost)
4. Nov 16, 2016 How to fix the slow sendkeys on IE 11 with Selenium Webdriver 3.0.0? [Click](https://stackoverflow.com/questions/40626810/how-to-fix-the-slow-sendkeys-on-ie-11-with-selenium-webdriver-3-0-0/40627587)

# Browser performance
1. Chrome(Linux):28mins
2. IE11(Win10):65mins

# ETC
1. How to initialize Win10 [Click](https://gbworld.tistory.com/1238)
2. How not to allowed file updated which already has been registered to repository ex) Gmail API access token [Click](https://wildlyinaccurate.com/git-ignore-changes-in-already-tracked-files/)
```
$ git update-index --assume-unchanged <file>
$ git update-index --no-assume-unchanged <file>
```
