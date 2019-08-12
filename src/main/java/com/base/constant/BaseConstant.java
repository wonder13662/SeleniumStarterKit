package com.base.constant;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.base.enums.EnumNameOnViewMap;

public class BaseConstant {
	
	public final static String COOKIE_KEY_REGRESSION_TEST_SESSION_TIME = "REGRESSION_TEST_SESSION_TIME";
	
	public static enum OS implements EnumNameOnViewMap {
		MAC("Mac OS X"), WINDOWS("windows"), LINUX("linux");
		
		private String nameOnView;
		public String getNameOnView() {
			return nameOnView;
		}
		
		OS(String nameOnView) {
			this.nameOnView = nameOnView;
		}
	}	
	
	public static enum Browser implements EnumNameOnViewMap {
		CHROME("chrome", true, true), 
		FIREFOX("firefox", true, false), 
		SAFARI("safari", true, true), 
		IE11("ie11", true, true), 
		EDGE("edge", false, true)
		;
		
		private String nameOnView;
		public String getNameOnView() {
			return nameOnView;
		}
		
		private boolean isEnableFileUpload=false;
		public boolean isEnableFileUpload() {
			return isEnableFileUpload;
		}
		
		private boolean hasAlertControl=false;
		public boolean hasAlertControl() {
			return hasAlertControl;
		}
		
		Browser(	String value, 
					boolean isEnableFileUpload, 
					boolean hasAlertControl
					) {
			this.nameOnView = value;
			this.isEnableFileUpload = isEnableFileUpload;
			this.hasAlertControl = hasAlertControl;
		}
	}
	
	public static enum Month implements EnumNameOnViewMap {
		JAN(1,"January"), 
		FEB(2,"February"), 
		MAR(3,"March"), 
		APR(4,"April"), 
		MAY(5,"May"), 
		JUN(6,"June"), 
		JUL(7,"July"), 
		AUG(8,"August"), 
		SEP(9,"September"), 
		OCT(10,"October"), 
		NOV(11,"November"), 
		DEC(12,"December");
		
		private int value;
		public int getValue() {
			return value;
		}
		
		private String nameOnView;
		public String getNameOnView() {
			return nameOnView;
		}

		Month(int value, String nameOnView) {
			this.value = value;
			this.nameOnView = nameOnView;
		}
		
		private static final Map<Integer, Month> intToEnum = Stream.of(values()).collect(Collectors.toMap(Month::getValue, e -> e));
		public static Optional<Month> fromInt(int value) {
			return Optional.ofNullable(intToEnum.get(value));
		}
	}

	public static enum Timeout {
		AUTH_EXPIRE_WAIT(7 * 60),
		SESSION_EXPIRE_WAIT(2 * 60),
		SESSION_EXPIRE_WAIT_COOKIE(1 * 60),
		SESSION_EXPIRE_WAIT_FOREVER(1 * 60 * 60 * 24 * 365), // Year
		TEST_API_INIT_ACCOUNT(240),
		
		LONG(60), 
		MID(30), 
		SHORT(5), 
		TINY(1), 
		NONE(0);
		
		private int second;
		public int getSecond() {
			return this.second;
		}
		
		public int getMillis() {
			return this.second * 1000;
		}		
		
		public int getMinutes() {
			return this.second/60;
		}
		
		Timeout(int second) {
			this.second = second;
		}
	}
}
