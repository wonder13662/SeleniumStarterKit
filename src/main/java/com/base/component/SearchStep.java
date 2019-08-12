package com.base.component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.base.command.BaseCommand;
import com.base.util.StringUtil;

public class SearchStep {
	private BaseCommand baseCommand;
	private SearchStep next;
	private SearchStep prev;
	private String query;
	private Function<List<WebElement>, Integer> findNthChild;
	private int foundIdx=-1;
	
	public SearchStep(BaseCommand baseCommand, String query, Function<List<WebElement>, Integer> findNthChild, int foundIdx) {
		
		Assert.assertTrue(baseCommand != null, String.format("baseCommand is null at SearchStep()"));
		Assert.assertTrue(StringUtil.isNotEmpty(query), String.format("query is null at SearchStep()"));
		
		setMemberVariables(baseCommand, query, findNthChild, foundIdx);
	}

	public SearchStep(BaseCommand baseCommand, String query, Function<List<WebElement>, Integer> findNthChild) {
		
		Assert.assertTrue(baseCommand != null, String.format("baseCommand is null at SearchStep()"));
		Assert.assertTrue(StringUtil.isNotEmpty(query), String.format("query is null at SearchStep()"));
		
		setMemberVariables(baseCommand, query, findNthChild, -1);
	}
	
	public SearchStep(BaseCommand baseCommand, String query, int foundIdx) {
		
		Assert.assertTrue(baseCommand != null, String.format("baseCommand is null at SearchStep()"));
		Assert.assertTrue(StringUtil.isNotEmpty(query), String.format("query is null at SearchStep()"));
		
		setMemberVariables(baseCommand, query, null, foundIdx);
	}
	
	private void setMemberVariables(BaseCommand baseCommand, String query, Function<List<WebElement>, Integer> findNthChild, int foundIdx) {
		this.baseCommand = baseCommand;
		this.query = query;
		this.findNthChild = findNthChild;
		this.foundIdx = foundIdx;		
	}
	
	public WebElement get() {
		SearchStep curSearchStep = this;
		WebElement found = curSearchStep.search();
		if(found == null) {
			baseCommand.info(false, String.format("Failed test due to found == null(1) at\n%s\nin SearchStep.get()", getQueryChain()));
			return null;
		}
		
		while (curSearchStep.hasNext()) {
			// 다음 SearchStep이 있다면 element를 검색한다.
			curSearchStep = curSearchStep.getNext();
			Assert.assertTrue(curSearchStep != null, String.format("curSearchStep != null at get()"));
			found = curSearchStep.search(found);
			if(found == null) {
				// 모달 내의 버튼등이 클릭 이후 모달이 사라짐으로써  없는 element를 검색하는 경우도 있음. 
				// 검색 결과가 없다는 것을 알려줘야 함.
				baseCommand.info(false, String.format("Failed test due to found == null(2) at\n%s\nin SearchStep.get()", getQueryChain()));
				return null;
			}
		}
		return found; 		
	}
	
	private WebElement search() {
		boolean isDebug = false;
		
		Assert.assertTrue(StringUtil.isNotEmpty(query));
		List<WebElement> elements = new ArrayList<WebElement>();
		
		try {
			elements = baseCommand.findAll(query);
		} catch (Exception e) {
			baseCommand.info(isDebug, String.format("Failed at\n%s\nin SearchStep.search()", getQueryChain()));
			return null;
		}
		
		baseCommand.info(isDebug, String.format("query:'%s' found elements.size():'%d' at Search()", query, elements.size()));
				
		return (elements.size() > 0)?findElement(elements):null;
	}
	
	private WebElement search(WebElement element) {
		if(element == null) {
			baseCommand.info(false, String.format("element == null at Search(WebElement element)"));
		}
		Assert.assertTrue(element != null, String.format("element is null at SearchStep.search()"));
		List<WebElement> elements = baseCommand.findAll(element, query);
		if(elements == null || elements.size() == 0) {
			baseCommand.info(false, String.format("elements == null || elements.size() == 0 at Search(WebElement element)"));
			return null;
		}
		
		WebElement found = findElement(elements);
		if(found == null) {
			baseCommand.info(false, String.format("found == null at Search(WebElement element)"));
			return null;
		}
		
		// 최상위 부모 SearchStep에서 
		
		return found;
	}
	
	private WebElement findElement(List<WebElement> elements) {
		Assert.assertTrue(elements.size() > 0);
		
		boolean isDebug = false;
		if(findNthChild != null) {
			baseCommand.info(isDebug, "1. 필터링 메서드가 있는 경우. at SearchStep.findElement");
			if(foundIdx > -1) {
				if(elements.size() == 0 || elements.size() <= foundIdx) {
					baseCommand.info(isDebug, String.format("1-1-2. foundIdx:'%d'는 있지만, DOM에서 button element가 사라진 경우 at SearchStep.findElement", foundIdx));
					return null;
				}
				baseCommand.info(isDebug, String.format("1-1-1. foundIdx:'%d'를 이전에 찾은 경우 at SearchStep.findElement", foundIdx));
				return elements.get(foundIdx);
			} else {
				baseCommand.info(isDebug, "1-2-1. idx를 찾는 Function 객체가 있는 경우. at SearchStep.findElement");
				foundIdx = findNthChild.apply(elements);
				if(foundIdx < 0) {
					baseCommand.info(isDebug, String.format("1-2-2. foundIdx:'%d' at BaseCommand.findElement", foundIdx));
					return null;
				}
				// SearchStep에서는 대상이 위치한 column, row의 순서가 변할수 있는 가능성있는 대상을 다룸. 그러므로 검색한 위치를 저장하지 않음.
				
				baseCommand.info(isDebug, String.format("1-2-3. 해당 인덱스:'%d'를 찾았습니다. at SearchStep.findElement", foundIdx));
				return elements.get(foundIdx);
			}
		} else if(foundIdx > -1) {
			baseCommand.info(isDebug, "2. 외부에서 foundIdx를 지정해준 경우 at SearchStep.findElement");
			return elements.get(foundIdx);
		}
		
		baseCommand.info(isDebug, "3. 필터링 메서드가 없다면 첫번째 element를 돌려준다. at SearchStep.findElement");
		return elements.get(0);
	}
	
	private SearchStep getNext() {
		return next;
	}
	
	public SearchStep getPrev() {
		return prev;
	}

	public SearchStep clone() {
		SearchStep clone = new SearchStep(baseCommand, query, findNthChild, foundIdx);
		if(next != null) {
			clone.push(next.clone());
		}
		return clone; 
	}
	
	private void setNext(SearchStep next) {
		this.next = next;
		next.prev = this;
	}
	
	public SearchStep cloneNpush(SearchStep next) {
		SearchStep clone = clone();
		clone.push(next);
		
		return clone;
	}
	
	public SearchStep cloneNpush(String query) {
		SearchStep clone = clone();
		clone.push(query);
		
		return clone;
	}
	
	public SearchStep cloneNpush(String query, int foundIdx) {
		SearchStep clone = clone();
		clone.push(query, foundIdx);
		
		return clone;
	}
	
	private SearchStep push(String query, int foundIdx) {
		SearchStep searchStep = new SearchStep(baseCommand, query, foundIdx);
		this.push(searchStep);
		
		return this;
	}
	
	public SearchStep push(String query) {
		SearchStep searchStep = com.base.component.ComponentHelper.getSearchStep(baseCommand, query);
		this.push(searchStep);
		
		return this;
	}
	
	public SearchStep push(SearchStep last) {
		SearchStep cursor = this;
		while (cursor.hasNext()) {
			cursor = cursor.getNext();
		}
		
		cursor.setNext(last);
		
		return this;
	}
	
	public boolean hasNext() {
		return getNext() != null;
	}
	
	public String getQueryChain() {
		if(next != null) {
			return String.format("%s %s", query, next.getQueryChain());
		} else {
			return query;
		}
	}
	
	public Point getPoint() {
		WebElement element = get();
		if(element == null) {
			return null;
		}
		
		Point point;
		try {
			point = element.getLocation();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return point;
	}	
	
	public String toString() {
		return getQueryChain();
	}
}
