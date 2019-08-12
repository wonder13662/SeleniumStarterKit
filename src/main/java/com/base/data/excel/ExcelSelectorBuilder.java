package com.base.data.excel;

import java.util.HashMap;
import java.util.Map;

public class ExcelSelectorBuilder {
	
	private int keyIdxOfMapKey;
	
	private String filePath = "";
	private int sheetIdx = -1;
	// 1. Map 객체의 값을 매칭해서 변경할 때 사용하는 idx
	private int columnIdxOfMapValue = -1;
	// 2. 특정 cell을 지정할 때 사용하는 idx
	private int columnIdxOfCell = -1;
	private int rowIdxOfCell = -1;
	
	private String keyExpected = "";
	private String valueExpected = "";
	private Map<String, String> keyValueMap = new HashMap<String, String>();	

	public ExcelSelectorBuilder() {
		// TODO Auto-generated constructor stub
	}

	public ExcelSelectorBuilder setFilePath(String filePath) {
		this.filePath = filePath;
		return this;
	}

	public ExcelSelectorBuilder setSheetIdx(int sheetIdx) {
		this.sheetIdx = sheetIdx;
		return this;
	}

	public ExcelSelectorBuilder setColumnIdxOfMapValue(int columnIdxOfMapValue) {
		this.columnIdxOfMapValue = columnIdxOfMapValue;
		return this;
	}

	public ExcelSelectorBuilder setColumnIdxOfCell(int columnIdxOfCell) {
		this.columnIdxOfCell = columnIdxOfCell;
		return this;
	}

	public ExcelSelectorBuilder setRowIdxOfCell(int rowIdxOfCell) {
		this.rowIdxOfCell = rowIdxOfCell;
		return this;
	}

	public ExcelSelectorBuilder setKeyExpected(String keyExpected) {
		this.keyExpected = keyExpected;
		return this;
	}

	public ExcelSelectorBuilder setValueExpected(String valueExpected) {
		this.valueExpected = valueExpected;
		return this;
	}

	public ExcelSelectorBuilder setKeyValueMap(Map<String, String> keyValueMap) {
		this.keyValueMap = keyValueMap;
		return this;
	}
	
	public ExcelSelectorBuilder setKeyIdxOfMapKey(int keyIdxOfMapKey) {
		this.keyIdxOfMapKey = keyIdxOfMapKey;
		return this;
	}

	public ExcelSelector build() {
		return new ExcelSelector(filePath, sheetIdx, keyIdxOfMapKey, columnIdxOfMapValue, columnIdxOfCell, rowIdxOfCell, keyValueMap, keyExpected, valueExpected);
	}
}
