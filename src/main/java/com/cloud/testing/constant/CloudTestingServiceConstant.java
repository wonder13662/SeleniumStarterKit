package com.cloud.testing.constant;

public class CloudTestingServiceConstant {
	public static enum CloudTestingService {
		SAUCE_LABS(), 
		LAMBDA_TEST(), 
		BROWSER_STACK()
		;
		
		public static CloudTestingService get(String value) {
			for (CloudTestingService os : CloudTestingService.values()) {
				if(value.toLowerCase().contains(os.name().toLowerCase())) {
					return os;
				}
			}
			return null;
		}
	}
}
