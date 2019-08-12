# How to setup(Required)
1. Download Java SE [Click](https://www.oracle.com/technetwork/java/javase/downloads/index.html)
2. Download Eclipse [Click](https://www.eclipse.org/downloads/)
3. Download Apache maven [Click](https://maven.apache.org/download.cgi)
4. Install Maven on Mac OSX [Click](https://www.mkyong.com/maven/install-maven-on-mac-osx/)
5. Install Maven on Windows [Click](https://www.mkyong.com/maven/how-to-install-maven-in-windows/)
6. Configure Maven Compiler plugin [Click](https://maven.apache.org/plugins/maven-compiler-plugin/)
7. Run test on Intelli-J [Click](https://www.guru99.com/intellij-selenium-webdriver.html)

# How to setup(Optional)
1. Testing with WebDriver in Safari [Click](https://developer.apple.com/documentation/webkit/testing_with_webdriver_in_safari)
2. Microsoft IE11 [Click](https://www.seleniumhq.org/download/)
3. Microsoft WebDriver [Click](https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/)
4. Microsoft IE11 Setup from Selenium HQ github [Click](https://github.com/SeleniumHQ/selenium/wiki/InternetExplorerDriver#required-configuration)
5. Multiple IE11 Setup from Selenium HQ github [Click](https://github.com/SeleniumHQ/selenium/wiki/InternetExplorerDriver#multiple-instances-of-internetexplorerdriver)
5. Microsoft IE11 Setup [Click](https://youtu.be/GxTHU_91Z1Q?t=529)
6. (*Required) Microsoft IE11 needs to turn off "Enable protected mode" at Internet option > Security
![Alt text](http://seleniumquery.github.io/images/ie-driver-protected-mode-disable.png)
7. Selenium grid for the parallel executions in mutiple machines. [Click](https://github.com/SeleniumHQ/selenium/wiki/Grid2)

# Directory
1. selenium is at '/auto-testing/selenium'

# Class definition
1. Command: 웹 DOM 위에서 이루어지는 가장 작은 단위의 행위. ex) Click, Select, Move to a specific page, etc.
2. Page: 실제 사용자가 보고 있는 페이지에서 이루어질 수 있는 행위들의 묶음. ex) 휴가 신청시 날짜 선택, 휴가 신청에서 Request 버튼 클릭, 휴가 신청 이후 화면에 표시된 잔여 휴가일수 확인, etc.
3. Action: Page에서 정의된 동작으로 유저가 완료하려는 개별의 Task를 정의. ex) 휴가 신청.

# Bug history
1. Nov 9, 2018 Safari element.getLocation throws a nullPointerException [Click](https://github.com/SeleniumHQ/selenium/issues/6637)
2. Dec 2, 2017 Using Safari Technology Preview with Selenium WebDriver [Click](https://macops.ca/using-safari-technology-preview-with-selenium-webdriver/)
3. May 21, 2016 The getCurrentUrl() method for IE WebDriver in Selenium only returning localhost [Click](https://stackoverflow.com/questions/37197291/the-getcurrenturl-method-for-ie-webdriver-in-selenium-only-returning-localhost)
4. Nov 16, 2016 How to fix the slow sendkeys on IE 11 with Selenium Webdriver 3.0.0? [Click](https://stackoverflow.com/questions/40626810/how-to-fix-the-slow-sendkeys-on-ie-11-with-selenium-webdriver-3-0-0/40627587)

# Browser performance
1. Chrome(Linux):28mins
2. IE11(Win10):65mins

# ETC
1. Win10 테스트 PC를 초기화하는 방법 [Click](https://gbworld.tistory.com/1238)
2. Git repository에는 등록되어야 하지만, 추가적인 변화는 업데이트가 되지 말아야 할 파일설정 ex) Gmail API access token [Click](https://wildlyinaccurate.com/git-ignore-changes-in-already-tracked-files/)
```
$ git update-index --assume-unchanged <file>
$ git update-index --no-assume-unchanged <file>

```
