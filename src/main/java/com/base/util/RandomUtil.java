package com.base.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.testng.Assert;

public class RandomUtil {

	public static <E> E getItem(List<E> list) {
		return getList(list, 1).get(0);
	}
	
	public static <E> List<E> getList(List<E> list, int rndCnt) {
		// Generic method
		int listSize = list.size();
		List<E> selectedList = new ArrayList<E>();
		List<E> srcList = new ArrayList<E>(list);
		
		E selectedItem = null;
		int rndIdx = -1;
		while (selectedList.size() != rndCnt) {
			rndIdx = NumberUtil.getRandom(0, srcList.size() - 1);
			selectedItem = srcList.get(rndIdx);
			srcList.remove(selectedItem);
			selectedList.add(selectedItem);
		}
		
		Assert.assertEquals(selectedList.size(), rndCnt);
		Assert.assertEquals(selectedList.size() + srcList.size(), listSize);
		
		return selectedList;
	}	
	
	public static String getRandom(String[] array) {
	    int rnd = new Random().nextInt(array.length);
	    return array[rnd];
	}
	
}
