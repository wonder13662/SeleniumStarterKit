package com.base.command;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebElement;

import com.base.Environment;
import com.base.constant.BaseConstant.Timeout;
import com.base.util.ExcelUtil;
import com.base.util.FileUtil;
import com.base.util.JsonUtil;

public class FileCommand extends BaseCommand {
	
	BaseCommand baseCommand;
	
	public FileCommand(Environment env) {
		super(env);
		baseCommand = env.getBaseCommand();		
	}

	public void writeJson(JSONObject obj, String path) {
		JsonUtil.writeJson(obj, path);
	}
	
	public String getAbsoluteAttachmentFilePath(String fileName) {
		return FileUtil.getAbsResourceFilePath(String.format("attachment/%s", fileName));
	}
	
	public String getAbsoluteResourceFilePath(String fileName) {
		return FileUtil.getAbsResourceFilePath(fileName);
	}
	
	public List<Map<String, String>> readCSV(String fileName) {
		return ExcelUtil.readCSV(fileName);
	}
	
	private void uploadFileFromResources(String query, String fileName) {
		// src/main/resource/files에 있는 파일명을 검색, locator가 가리키는 input:file객체에 파일을 주어 업로드합니다.
		String absPath = getAbsoluteAttachmentFilePath(fileName);
		uploadFile(query, absPath);
		sleep(Timeout.TINY);		
	}
	
	private void uploadMultipleFileFromResources(String query, List<String> fileNames) {
		// https://tuyennta.com/upload-multiple-files-in-selenium/
		String filePathsWithDelimeter = "";
		for (String fileName : fileNames) {
			String absPath = getAbsoluteAttachmentFilePath(fileName);
			filePathsWithDelimeter += "\n" + absPath;
		}
		filePathsWithDelimeter = filePathsWithDelimeter.replaceFirst("\\n", "");
		uploadFile(query, filePathsWithDelimeter);
		sleep(Timeout.TINY);		
	}

	public void uploadFile(String query, String filePath) {
		WebElement fileInput = find(query);
		fileInput.sendKeys(filePath);
	}
	
	private void forceFileInputPresented(String query) {
		baseCommand.setDisplayBlocked(query);
		waitUntilClickable(query);
	}
	
	public void uploadFileByForcingFileInputPresented(String query, String fileName) {
		forceFileInputPresented(query);
		uploadFileFromResources(query, fileName);
    }
	
	public void uploadMultipleFilesByForcingFileInputPresented(String query, List<String> fileNames) {
		forceFileInputPresented(query);
		uploadMultipleFileFromResources(query, fileNames);
	}	

	public List<String> getColumnValuesFromExcelFile(String filePath, int sheetIdx, int titleRowIdx, int colunmIdx)
			throws IOException, InvalidFormatException {
		return ExcelUtil.getColumn(filePath, sheetIdx, titleRowIdx, colunmIdx);
	}
}
