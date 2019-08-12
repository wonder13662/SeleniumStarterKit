package com.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;

import com.base.data.excel.ExcelSelector;
import com.base.log.AutomationLog;
import com.base.log.TestLogList;
import com.opencsv.CSVReader;

public class ExcelUtil {
	
	public static List<Map<String, String>> readCSV(String fileName) {
		String absPath = FileUtil.getAbsResourceFilePath(fileName);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		File file = new File(absPath);
		try {
			CSVReader reader = new CSVReader(new FileReader(file));
			String[] keys = reader.readNext();
			if (keys != null) {
				String[] dataParts;
				while ((dataParts = reader.readNext()) != null) {
					Map<String, String> row = new HashMap<String, String>();
					for (int i = 0; i < keys.length; i++) {
						row.put(keys[i], dataParts[i]);
					}
					list.add(row);
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File " + absPath + " was not found.\n" + e.getStackTrace().toString());
		} catch (IOException e) {
			throw new RuntimeException("Could not read " + absPath + " file.\n" + e.getStackTrace().toString());
		}

		return list;
	}
	
	public static void addRow(ExcelSelector excelSelector) {
		Assert.assertTrue(StringUtil.isNotEmpty(excelSelector.getFilePath()));
		Assert.assertTrue(excelSelector.getSheetIdx() > -1);
		
		try {
			FileInputStream inputStream = new FileInputStream(excelSelector.getFilePath());
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbook.getSheetAt(excelSelector.getSheetIdx());
			
			int rowCnt = sheet.getPhysicalNumberOfRows();
			int rowIdxOfCell = excelSelector.getRowIdxOfCell();
			Assert.assertTrue(rowIdxOfCell < rowCnt);
			
			sheet.createRow(sheet.getLastRowNum() + 1);
			
			FileOutputStream outputStream = new FileOutputStream(excelSelector.getFilePath());
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}			
	}
	
	public static void addLog(ExcelSelector excelSelector, TestLogList logList) {
		Assert.assertTrue(StringUtil.isNotEmpty(excelSelector.getFilePath()));
		Assert.assertTrue(excelSelector.getSheetIdx() > -1);
		
		try {
			FileInputStream inputStream = new FileInputStream(excelSelector.getFilePath());
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbook.getSheetAt(excelSelector.getSheetIdx());
			
			int rowCnt = sheet.getPhysicalNumberOfRows();
			int rowIdxOfCell = excelSelector.getRowIdxOfCell();
			Assert.assertTrue(rowIdxOfCell < rowCnt);
			
			XSSFRow row = null;
			XSSFCell cell = null;
			
			List<AutomationLog> list = logList.getLogList();
			AutomationLog log = null;
			for (int i = 0; i < list.size(); i++) {
				log = list.get(i);
				row = sheet.getRow(i + 1);
				if(row != null) {
					sheet.removeRow(row);
				}
				row = sheet.createRow(i + 1);
				
				// 1. Test Suite
				cell = row.createCell(1);
				cell.setCellValue(log.getTestSuite());
				
				// 2. Account info
				cell = row.createCell(2);
				cell.setCellValue(log.getAccount());
				
				// 3. Action
				cell = row.createCell(3);
				cell.setCellValue(log.getAction());
				
				// 4. Expected
				cell = row.createCell(4);
				cell.setCellValue(log.getExpected());
				
				// 5. Actual
				cell = row.createCell(5);
				cell.setCellValue(log.getActual());
				
				// 6. Test Result
				cell = row.createCell(6);
				cell.setCellValue(log.getTestResult().getValue());
				
				// 7. Time
				cell = row.createCell(7);
				cell.setCellValue(log.getDuration());
			}			
			
			FileOutputStream outputStream = new FileOutputStream(excelSelector.getFilePath());
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}			
	}	
	
	public static void editCell(ExcelSelector excelSelector) {
		Assert.assertTrue(StringUtil.isNotEmpty(excelSelector.getFilePath()));
		Assert.assertTrue(excelSelector.getSheetIdx() > -1);
		Assert.assertTrue(excelSelector.getColumnIdxOfCell() > -1);
		Assert.assertTrue(excelSelector.getRowIdxOfCell() > -1);
		Assert.assertTrue(StringUtil.isNotEmpty(excelSelector.getValueExpected()));
		
		// *.xlsx 확장자만 처리 가능
		// 1개의 cell만 변경한다
		// TODO REFACTOR ME
		try {
			FileInputStream inputStream = new FileInputStream(excelSelector.getFilePath());
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbook.getSheetAt(excelSelector.getSheetIdx());
			
			int rowCnt = sheet.getPhysicalNumberOfRows();
			int rowIdxOfCell = excelSelector.getRowIdxOfCell();
			Assert.assertTrue(rowIdxOfCell < rowCnt);
			
			XSSFRow row = sheet.getRow(rowIdxOfCell);
			XSSFCell cellHasValue = row.getCell(excelSelector.getColumnIdxOfCell());
			Assert.assertTrue(cellHasValue != null);
			
			cellHasValue = row.getCell(excelSelector.getColumnIdxOfCell());
			cellHasValue.setCellValue(excelSelector.getValueExpected());
			
			// TODO REFACTOR ME
			FileOutputStream outputStream = new FileOutputStream(excelSelector.getFilePath());
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public static void edit(ExcelSelector excelSelector) {
		// *.xlsx 확장자만 처리 가능
		// TODO REFACTOR ME
		try {
			FileInputStream inputStream = new FileInputStream(excelSelector.getFilePath());
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbook.getSheetAt(excelSelector.getSheetIdx());
			DataFormatter dataFormatter = new DataFormatter();
			
			int rowCnt = sheet.getPhysicalNumberOfRows();
			for (int i = 0; i < rowCnt; i++) {
				XSSFRow row = sheet.getRow(i);				
				XSSFCell cellHasKey = row.getCell(excelSelector.getColumnIdxOfMapKey());
				XSSFCell cellHasValue = null;
				
				// key에 해당하는 value를 excelSelector가 가지고 있다면, value의 값으로 table의 cell값을 업데이트해준다.
				// 1. key로 rowIdx를 검색
				// 2. rowIdx에 위치한 cell을 value 값으로 업데이트
				String key = dataFormatter.formatCellValue(cellHasKey);
				String value = excelSelector.get(key);
				if(StringUtil.isNotEmpty(value)) {
					cellHasValue = row.getCell(excelSelector.getColumnIdxOfMapValue());
					cellHasValue.setCellValue(value);
				}
			}
			
			FileOutputStream outputStream = new FileOutputStream(excelSelector.getFilePath());
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static int readRow(ExcelSelector excelSelector) {
		Assert.assertTrue(StringUtil.isNotEmpty(excelSelector.getFilePath()));
		Assert.assertTrue(excelSelector.getSheetIdx() > -1);
		Assert.assertTrue(excelSelector.getColumnIdxOfCell() > -1);
		Assert.assertTrue(StringUtil.isNotEmpty(excelSelector.getKeyExpected()));
		
		// *.xlsx 확장자만 처리 가능
		// TODO REFACTOR ME
		int rowIdxToLookUp = -1;
		try {
			FileInputStream inputStream = new FileInputStream(excelSelector.getFilePath());
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbook.getSheetAt(excelSelector.getSheetIdx());
			DataFormatter dataFormatter = new DataFormatter();
			
			int rowCnt = sheet.getPhysicalNumberOfRows();
			for (int i = 0; i < rowCnt; i++) {
				XSSFRow row = sheet.getRow(i);			
				XSSFCell cellHasValue = row.getCell(excelSelector.getColumnIdxOfCell());
				String value = dataFormatter.formatCellValue(cellHasValue);
				if(StringUtil.isNotEmpty(value) && value.equalsIgnoreCase(excelSelector.getKeyExpected())) {
					rowIdxToLookUp = i;
					break;
				}
			}
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
			return rowIdxToLookUp;
		}
		return rowIdxToLookUp;		
	}

	public static List<String> readColumn(ExcelSelector excelSelector) {
		Assert.assertTrue(StringUtil.isNotEmpty(excelSelector.getFilePath()));
		Assert.assertTrue(excelSelector.getSheetIdx() > -1);
		Assert.assertTrue(excelSelector.getColumnIdxOfCell() > -1);
		
		// *.xlsx 확장자만 처리 가능
		// TODO REFACTOR ME
		List<String> valueList = new ArrayList<String>();
		try {
			FileInputStream inputStream = new FileInputStream(excelSelector.getFilePath());
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbook.getSheetAt(excelSelector.getSheetIdx());
			DataFormatter dataFormatter = new DataFormatter();
			
			int rowCnt = sheet.getPhysicalNumberOfRows();
			for (int i = 0; i < rowCnt; i++) {
				XSSFRow row = sheet.getRow(i);			
				XSSFCell cellHasValue = row.getCell(excelSelector.getColumnIdxOfCell());
				String value = dataFormatter.formatCellValue(cellHasValue);
				if(StringUtil.isNotEmpty(value)) {
					valueList.add(value);
				}
			}
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
			return valueList;
		}
		return valueList;
	}
	
	public static String readCell(ExcelSelector excelSelector) {
		Assert.assertTrue(StringUtil.isNotEmpty(excelSelector.getFilePath()));
		Assert.assertTrue(excelSelector.getSheetIdx() > -1);
		Assert.assertTrue(excelSelector.getRowIdxOfCell() > -1);
		Assert.assertTrue(excelSelector.getColumnIdxOfCell() > -1);
		
		// *.xlsx 확장자만 처리 가능
		// TODO REFACTOR ME
		String valueInCell = "";
		try { // TODO 공통 로직 부분 추출하기
			FileInputStream inputStream = new FileInputStream(excelSelector.getFilePath());
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbook.getSheetAt(excelSelector.getSheetIdx());
			DataFormatter dataFormatter = new DataFormatter();
			
			int rowCnt = sheet.getPhysicalNumberOfRows();
			for (int i = 0; i < rowCnt; i++) {
				if(excelSelector.getRowIdxOfCell() != i) continue;
				
				XSSFRow row = sheet.getRow(i);
				XSSFCell cellHasKey = row.getCell(excelSelector.getColumnIdxOfCell());
				valueInCell = dataFormatter.formatCellValue(cellHasKey);
				break;
			}
			
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
			return valueInCell;
		}
		return valueInCell;
	}
	
	@Deprecated
	public static List<String> getColumn(String filePath, int sheetIdx, int titleRowIdx, int colunmIdx)
			throws IOException, InvalidFormatException {
		// https://www.callicoder.com/java-read-excel-file-apache-poi/
		// Creating a Workbook from an Excel file (.xls or .xlsx)
		Workbook workbook = WorkbookFactory.create(new File(filePath));

		// Getting the Sheet at index zero
		Sheet sheet = workbook.getSheetAt(sheetIdx);

		// Create a DataFormatter to format and get each cell's value as String
		DataFormatter dataFormatter = new DataFormatter();

		// 실제로 타이틀이 표시된 인덱스를 구합니다.
		Row firstRow = sheet.getRow(titleRowIdx);
		final int validColumnlength = firstRow.getPhysicalNumberOfCells();
		List<String> arrayList = new ArrayList<String>();
		for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
			Row row = sheet.getRow(i);
			String firstCellValue = dataFormatter.formatCellValue(row.getCell(0));
			// 타이틀 행은 제외
			// 첫번째 셀에 값이 있어야만 유효합니다.
			if (titleRowIdx < i && firstCellValue.length() > 1) {
				for (int j = 0; j < validColumnlength; j++) {
					// 파라미터로 전달받은 컬럼 idx의 값들만 뽑아냅니다.
					if (colunmIdx == j) {
						Cell cell = row.getCell(j);
						String cellValue = dataFormatter.formatCellValue(cell);
						arrayList.add(cellValue);
					}
				}
			}
		}

		// Closing the workbook
		workbook.close();

		return arrayList;
	}
}
