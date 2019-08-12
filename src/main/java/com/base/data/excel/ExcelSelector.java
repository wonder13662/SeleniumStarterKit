package com.base.data.excel;

import java.util.HashMap;
import java.util.Map;

public class ExcelSelector {
	
	private String filePath = "";
	private int sheetIdx = -1;
	// 1. Map 객체의 값을 매칭해서 변경할 때 사용하는 idx
	private int columnIdxOfMapKey = -1;
	private int columnIdxOfMapValue = -1;
	// 2. 특정 cell을 지정할 때 사용하는 idx
	private int columnIdxOfCell = -1;
	private int rowIdxOfCell = -1;
	
	private String keyExpected = "";
	private String valueExpected = "";
	private Map<String, String> keyValueMap = new HashMap<String, String>();
	
	public ExcelSelector(String filePath, int sheetIdx, int keyIdxOfMapKey, int columnIdxOfMapValue, int columnIdxOfCell, int rowIdxOfCell, Map<String, String> keyValueMap, String keyExpected, String valueExpected) {
		this.filePath = filePath;
		this.sheetIdx = sheetIdx;
		
		this.columnIdxOfMapKey = keyIdxOfMapKey;
		this.columnIdxOfMapValue = columnIdxOfMapValue;
		
		this.columnIdxOfCell = columnIdxOfCell;
		this.rowIdxOfCell = rowIdxOfCell;
		
		this.keyValueMap = keyValueMap;
		
		this.keyExpected = keyExpected;
		this.valueExpected = valueExpected;
	}
	
	public String get(String key) {
		return keyValueMap.get(key);
	}

	public String getFilePath() {
		return filePath;
	}

	public int getSheetIdx() {
		return sheetIdx;
	}

	public int getColumnIdxOfMapKey() {
		return columnIdxOfMapKey;
	}

	public int getColumnIdxOfMapValue() {
		return columnIdxOfMapValue;
	}

	public int getColumnIdxOfCell() {
		return columnIdxOfCell;
	}

	public int getRowIdxOfCell() {
		return rowIdxOfCell;
	}

	public Map<String, String> getKeyValueMap() {
		return keyValueMap;
	}

	public String getKeyExpected() {
		return keyExpected;
	}

	public String getValueExpected() {
		return valueExpected;
	}	
}
