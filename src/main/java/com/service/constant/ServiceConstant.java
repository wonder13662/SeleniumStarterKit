package com.service.constant;

import com.base.enums.EnumNameOnViewMap;
import com.base.enums.EnumServerStage;
import com.base.enums.EnumServiceDomain;
import com.base.util.StringUtil;

public class ServiceConstant {
	public enum ServerStage implements EnumNameOnViewMap, EnumServerStage {
		ALPHA("alpha", "alpha."), 
		BETA("beta", "beta."), 
		QA("qa", "qa."), 
		DEMO("demo", "demo."),
		REAL("real", "");

		private String nameOnView;
		private String serverStageName;

		ServerStage(String nameOnView, String stageName) {
			this.nameOnView = nameOnView;
			this.serverStageName = stageName;
		}

		public String getNameOnView() {
			return this.nameOnView;
		}

		public String getServerStageName() {
			return this.serverStageName;
		}
		
		public static ServerStage contains(String url) {
			for (ServerStage s : ServerStage.values()) {
				// TODO / Fix me: No condition in Real mode
				if (StringUtil.contains(url, s.getNameOnView())) {
					return s;
				}
			}
			return null;
		}		
	}

	public enum Service implements EnumNameOnViewMap, EnumServiceDomain {
		RAKUTEN("rakuten","https://www.rakuten.com");

		private String nameOnView;
		private String domainUrl;

		Service(String nameOnView, String domainUrl) {
			this.nameOnView = nameOnView;
			this.domainUrl = domainUrl;
		}

		public String getNameOnView() {
			return this.nameOnView;
		}
		
		public String getDomainUrl(EnumServerStage stage) {
			return String.format(this.domainUrl, stage.getServerStageName());
		}
	}
}
