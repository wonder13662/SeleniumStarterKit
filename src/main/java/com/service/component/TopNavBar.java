package com.service.component;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.testng.Assert;

import com.base.Environment;
import com.base.component.BaseComponent;
import com.base.component.Field;
import com.base.component.SearchStep;
import com.base.constant.BaseConstant.Timeout;

public class TopNavBar extends BaseComponent {
	public TopNavBar(Environment env) { 
		super(env, com.base.component.ComponentHelper.getSearchStep(env, "#header-main ul#main-nav"));
	}
	
	public static enum TopNavTab {
		ALL_STORE() {
			public Field getField(BaseComponent bc) {
				return getField(bc, "li#nav-tab1");
			}
		},
		DOUBLE_CASHBACK_STORE() {
			public Field getField(BaseComponent bc) {
				return getField(bc, "li#nav-tab8");
			}
		},
		HOT_DEALS() {
			public Field getField(BaseComponent bc) {
				return getField(bc, "li#nav-tab2");
			}
		},
		IN_STORE_CASHBACK() {
			public Field getField(BaseComponent bc) {
				return getField(bc, "li#nav-tab4");
			}
		},
		TRAVEL_N_VACATIONS() {
			public Field getField(BaseComponent bc) {
				return getField(bc, "li#nav-tab6");
			}
		},
		REFER_N_EARN() {
			public Field getField(BaseComponent bc) {
				return getField(bc, "li#nav-tab5");
			}
		},
		HELP() {
			public Field getField(BaseComponent bc) {
				return getField(bc, "li#nav-tab7");
			}
		};
		
		public abstract Field getField(BaseComponent bc);
		
		private static Field getField(BaseComponent bc, String query) {
			SearchStep target = bc.getSearchStep().cloneNpush(query);
			Field field = new Field(bc.getEnv(), target);
			Assert.assertTrue(field != null);
			
			return field;
		}
	}
	
	public void click(TopNavTab topNavTab) {
		Field field = topNavTab.getField(this);
		field.waitUntilPresented(Timeout.SHORT);
		field.click();
	}
	
	public void scrollWindowDownAndUp() {
		baseCommand.scrollWindowDownAndUp();
	}
	
	public void waitUntilPresented(Timeout timeout) {
		TopNavBar topNavBar = this;
		baseCommand.waitUntil(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return TopNavTab.ALL_STORE.getField(topNavBar).isPresented();
			}
		}, timeout);
	}
}
