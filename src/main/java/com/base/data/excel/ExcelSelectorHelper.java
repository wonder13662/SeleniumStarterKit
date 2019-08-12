package com.base.data.excel;

import java.util.HashMap;
import java.util.Map;

public class ExcelSelectorHelper {
	public static ExcelSelector getReadingColumn(String filePath, int sheetIdx, int columnIdxOfValue) {
		
		int columnIdxOfMapKey = -1;
		int columnIdxOfMapValue = -1;
		int rowIdxOfCell = -1;
		Map<String, String> keyValueMap = new HashMap<String, String>();
		String keyExpected = "";
		String valueExpected = "";
		
		return new ExcelSelector(filePath, sheetIdx, columnIdxOfMapKey, columnIdxOfMapValue, columnIdxOfValue, rowIdxOfCell, keyValueMap, keyExpected, valueExpected);
	}
	
	public static ExcelSelector getReadingRowIdx(String filePath, int sheetIdx, int columnIdxOfValue, String keyExpected) {
		
		int columnIdxOfMapKey = -1;
		int columnIdxOfMapValue = -1;
		int rowIdxOfCell = -1;
		Map<String, String> keyValueMap = new HashMap<String, String>();
		String valueExpected = "";
		
		return new ExcelSelector(filePath, sheetIdx, columnIdxOfMapKey, columnIdxOfMapValue, columnIdxOfValue, rowIdxOfCell, keyValueMap, keyExpected, valueExpected);
	}	
	
	public static ExcelSelector getReadingCell(String filePath, int sheetIdx, int columnIdxOfCell, int rowIdxOfCell) {
		
		int columnIdxOfMapKey = -1;
		int columnIdxOfMapValue = -1;
		Map<String, String> keyValueMap = new HashMap<String, String>();
		String keyExpected = "";
		String valueExpected = "";
		
		return new ExcelSelector(filePath, sheetIdx, columnIdxOfMapKey, columnIdxOfMapValue, columnIdxOfCell, rowIdxOfCell, keyValueMap, keyExpected, valueExpected);
	}
	
	public static ExcelSelector getEditingColumn(String filePath, int sheetIdx, int columnIdxOfMapKey, int columnIdxOfMapValue, Map<String, String> keyValueMap) {
		
		int columnIdxOfCell = -1;
		int rowIdxOfCell = -1;
		String keyExpected = "";
		String valueExpected = "";
		
		return new ExcelSelector(filePath, sheetIdx, columnIdxOfMapKey, columnIdxOfMapValue, columnIdxOfCell, rowIdxOfCell, keyValueMap, keyExpected, valueExpected);
	}
 
	public static ExcelSelector getEditingCell(String filePath, int sheetIdx, int columnIdxOfCell, int rowIdxOfCell, String valueExpected) {
		int columnIdxOfMapKey = -1;
		int columnIdxOfMapValue = -1; 
		Map<String, String> keyValueMap = new HashMap<String, String>();
		String keyExpected = "";
		
		return new ExcelSelector(filePath, sheetIdx, columnIdxOfMapKey, columnIdxOfMapValue, columnIdxOfCell, rowIdxOfCell, keyValueMap, keyExpected, valueExpected);
	}	
}
