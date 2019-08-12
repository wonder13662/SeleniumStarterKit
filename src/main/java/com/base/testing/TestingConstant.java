package com.base.testing;

public class TestingConstant {
	
	public static enum TestResult {
		READY("Ready"), PASS("Pass"), FAILED("Failed"), BLOCKED("Blocked"), NA("N/A"), NOT_IMPLEMENTED("Not implemented");
		
		private String value;
		public String getValue() {
			return value;
		}
		
		TestResult(String value) {
			this.value = value;
		}
		
		public static TestResult get(String value) {
			for (TestResult os : TestResult.values()) {
				if(value.toLowerCase().contains(os.getValue().toLowerCase())) {
					return os;
				}
			}
			return null;
		}		
	}	

}
