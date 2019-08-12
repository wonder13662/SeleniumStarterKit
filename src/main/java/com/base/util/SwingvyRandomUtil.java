package com.base.util;

import java.util.Arrays;
import java.util.List;

public class SwingvyRandomUtil<T> {
	
	private List<T> list;
	private T[] arr;

	public SwingvyRandomUtil(List<T> list) {
		this.list = list;
	}
	
	public SwingvyRandomUtil(T[] array) {
		this.arr = array;
	}
	
	private int getRandomIdx() {
		if(list != null && list.size() > 0) {
			return NumberUtil.getRandom(0, list.size() - 1);
		} else if(arr != null && arr.length > 0) {
			return NumberUtil.getRandom(0, arr.length - 1);
		}		
		return 0;
	}
	
	public T get() {
		if(list != null && list.size() > 0) {
			return list.get(getRandomIdx());
		} else if(arr != null && arr.length > 0) {
			return arr[getRandomIdx()];
		}
		return null;
	}
	
	public List<T> getList() {
		int endIdx = getRandomIdx() + 1;
		int beginIdx = NumberUtil.getRandom(0, endIdx);
		
		if(list != null && list.size() > 0) {
			return list.subList(beginIdx, endIdx);
		} else if(arr != null && arr.length > 0) {
			return Arrays.asList(Arrays.copyOfRange(arr, beginIdx, endIdx));
		}
		
		return null;
	} 
}
