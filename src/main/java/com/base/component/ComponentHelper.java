package com.base.component;

import java.util.List;
import java.util.function.Function;

import org.openqa.selenium.WebElement;

import com.base.Environment;
import com.base.command.BaseCommand;
import com.base.util.StringUtil;

public class ComponentHelper {

	public static SearchStep getSearchStep(BaseCommand baseCommand, String query) {
		return new SearchStepBuilder(baseCommand, query).build();
	}

	public static SearchStep getSearchStep(Environment env, String query) {
		return new SearchStepBuilder(env.getBaseCommand(), query).build();
	}

	public static SearchStep getSearchStep(Environment env, String query, int foundIdx) {
		return new SearchStepBuilder(env.getBaseCommand(), query, foundIdx).build();
	}

	public static SearchStep getSearchStep(Environment env, String queryList, String expected) {
		return ComponentHelper.getSearchStep(env, queryList, "", expected);
	}

	public static SearchStep getSearchStep(Environment env, String queryList, String queryItem, String expected) {
		Function<WebElement, Boolean> filterOne = 
				(WebElement item)-> {
						if(item == null) {
							return false;
						}
						
						boolean isDebug = false;
						if(isDebug) {
							env.getLogger().info(String.format("queryItem:'%s' at filterOne", queryItem));
							env.getLogger().info(String.format("expected:'%s' at filterOne", expected));
						}
						
						String actual = "";
						try {
							if(StringUtil.isNotEmpty(queryItem)) {
								WebElement titleBox = null;
								try {
									titleBox = env.getBaseCommand().findChild(item, queryItem);
								} catch (Exception e) {
									return false;
								}
								if(titleBox == null) {
									return false;
								}
								
								actual = titleBox.getText();		
							} else {
								actual = item.getText();
							}
						} catch (Exception e) {
							return false;
						}
						
						return StringUtil.contains(actual, expected);
					};
					
		return ComponentHelper.getSearchStepByFilterOne(env, queryList, filterOne);		
	}

	public static Function<List<WebElement>, Integer> getFindOne(Function<WebElement, Boolean> filterOne) {
		Function<List<WebElement>, Integer> findOne = 
				(List<WebElement> list)-> {
					for (int i = 0; i < list.size(); i++) {
						WebElement item = list.get(i);
						boolean isValid = filterOne.apply(item);
						if(isValid) {
							//System.out.println(String.format("isValid:'%b' at idx:'%d' getFindOne", isValid, i));
							return i;
						}
						continue;						
					}
					
					return -1;
					};
		return findOne;
	}

	public static SearchStep getSearchStepByFilterOne(Environment env, String query, Function<WebElement, Boolean> filterOne) {
		Function<List<WebElement>, Integer> findOne = getFindOne(filterOne);
		return new SearchStepBuilder(env.getBaseCommand(), query).setFindNthChild(findOne).build();
	}

	public static SearchStep getSearchStep(Environment env, String query, Function<List<WebElement>, Integer> findOne) {
		// 입력한 locator를 기준으로만 SearchStep을 만들어주는 메서드.
		return new SearchStepBuilder(env.getBaseCommand(), query).setFindNthChild(findOne).build();
	}

	public static SearchStep setNextSearchStep(Environment env, SearchStep searchStep, String query) {
		SearchStep searchStepClone = searchStep.clone();
		SearchStep searchStepNext = getSearchStep(env, query);		
		searchStepClone.push(searchStepNext);
		
		return searchStepClone;
	}
}
