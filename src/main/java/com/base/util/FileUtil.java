package com.base.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.testng.Assert;

import com.base.Environment;
import com.base.constant.BaseConstant.Browser;
import com.base.constant.BaseConstant.Timeout;

public class FileUtil {
	
	private static final String DOWNLOAD_DIR_PATH = Paths.get("src/main/resources/files/download").toString();
	private static final String RESOURCE_DIR_PATH = Paths.get("src/main/resources/files").toString();
	private static final String SCREENSHOT_DIR_PATH = Paths.get(RESOURCE_DIR_PATH + "/screenshot/").toString();
	
	public static boolean existAtResourceFile(String fileName) {
		File file = new File(String.format(RESOURCE_DIR_PATH + "%s", fileName));
		return file.exists();
	}
	
	public static void copyFile(File source, File dest) throws IOException {
		Files.copy(source.toPath(), dest.toPath());
	}
	
	public static void deleteDownloadedFiles(Environment env) {
		File file = new File(DOWNLOAD_DIR_PATH);
		File[] files = file.listFiles();
		
		int fileCnt = FileUtil.getFileCntAtDownloadDir();
		int deletedFileCnt = 0;
		int fileCntExpected = 0;
		
		env.log(String.format("Delete %d files at deleteDownloadedFiles", files.length));
		
		for (File fileToDelete : files) {
			String fileName = fileToDelete.getName();
			boolean isNotAllowedToDelete = FileUtil.isNotAllowedToDeleteFileAtChrome(fileName);
			if(isNotAllowedToDelete) {
				continue;
			}
			
			env.log(String.format("Delete file:'%s' at deleteDownloadedFiles", fileToDelete.getName()));
			fileToDelete.delete();
			deletedFileCnt++;
			fileCntExpected = fileCnt - deletedFileCnt;
			
			FileUtil.waitUntilFileCntChanged(env, fileCntExpected, Timeout.MID);
		}
	}
	
	public static boolean isNotAllowedToDeleteFileAtChrome(String fileName) {
		String[] notAllowToDeleteAtChrome = new String[] {"readme.md", ".crdownload", ".com.google.Chrome"};
		for (String keyword : notAllowToDeleteAtChrome) {
			if(StringUtil.contains(fileName, keyword)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isDownloadingFileAtChrome(String fileName) {
		String[] downloadingFilesAtChrome = new String[] {".crdownload", ".com.google.Chrome"};
		for (String keyword : downloadingFilesAtChrome) {
			if(StringUtil.contains(fileName, keyword)) {
				return true;
			}
		}
		return false;
	}
	
	public static void logFilesAtDownloadFilePath(Environment env) {
		File file = new File(DOWNLOAD_DIR_PATH);
		File[] files = file.listFiles();
		String fileName = "";
		for (File fileToDelete : files) {
			fileName = fileToDelete.getName();
			env.log(fileName);
		}
	}
	
	public static void waitUntilFileDownloadCompleted(Environment env, Timeout timeout) {
		env.getBaseCommand().waitUntil(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				File file = new File(DOWNLOAD_DIR_PATH);
				File[] files = file.listFiles();
				for (File fileToDelete : files) {
					String fileName = fileToDelete.getName();
					boolean isDownloadingFileAtChrome = isDownloadingFileAtChrome(fileName);
					if(isDownloadingFileAtChrome) {
						return false;
					}
				}
				
				return true;
			}
		}, timeout);
	}
	public static void waitUntilFileCntChanged(Environment env, int expectedFileCnt, Timeout timeout) {
		int currentFileCnt = FileUtil.getFileCntAtDownloadDir();
		String event = expectedFileCnt > currentFileCnt?"Downloading file":"Deleting file";
		env.log(String.format("%s / currentFileCnt:'%d' -> expectedFileCnt:'%d'  at waitUntilFileDownloaded", event, currentFileCnt, expectedFileCnt));
		
		env.getBaseCommand().waitUntil(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				int currentFileCnt = FileUtil.getFileCntAtDownloadDir();
				boolean hasExpectedFileCnt = currentFileCnt == expectedFileCnt;
				if(hasExpectedFileCnt) {
					env.log(String.format("%s / successfully done! %d files\n", event, currentFileCnt));
				}
				
				return hasExpectedFileCnt;
			}
		}, timeout);
	}
	public static File getDownloadDir() {
		File file = new File(DOWNLOAD_DIR_PATH);
		Assert.assertTrue(file.exists());
		return file;
	}
	public static String getAbsoluteDownloadPath() {
		File file = getDownloadDir();
		return file.getAbsolutePath();
	}
	public static int getFileCntAtDownloadDir() {
		File file = getDownloadDir();
		return file.list().length;
	}
	public static void waitFileDownloadNDelete(Environment env, int fileCntAtDownloadDir, Timeout timeout) {
		if(env.getBrowser() == Browser.CHROME) {
			FileUtil.waitUntilFileDownloaded(env, fileCntAtDownloadDir, timeout);
			deleteDownloadedFiles(env);
		}
	}
	public static void waitUntilFileDownloaded(Environment env, int fileCntAtDownloadDir, Timeout timeout) {
		waitUntilFileDownloadCompleted(env, timeout);
		waitUntilFileCntChanged(env, fileCntAtDownloadDir + 1, timeout);
	}
	public static String getAbsAttachmentFilePath(String fileName) {
		return FileUtil.getAbsResourceFilePath(String.format("attachment/%s", fileName));		
	}
	public static String getAbsResourceFilePath(String fileName) {
		String path = String.format(RESOURCE_DIR_PATH + "/%s", fileName);
		File file = new File(path);
		Assert.assertTrue(file.exists(), String.format("path does not exist at:'%s'", path));
		return file.getAbsolutePath();
	}
	public static boolean doesFileExist(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}
	public static String getScreenShotDirPath() {
		File file = new File(SCREENSHOT_DIR_PATH);
		Assert.assertTrue(file.exists());
		return file.getAbsolutePath();
	}
	public static void writeFile(String path, String text) {
		// https://www.mkyong.com/java/json-simple-example-read-and-write-json/
	    try (FileWriter file = new FileWriter(path)) {
	
	        file.write(text);
	        file.flush();
	
	    } catch (IOException e) {
	        e.printStackTrace();
	    }		
	}
	public static String readFile(String filePath) {
	    StringBuilder sb = new StringBuilder();
	    try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
	        String line;
	        while ((line = br.readLine()) != null) {
	            sb.append(line);
	        }
	        return sb.toString(); 
	    } catch (IOException e) {
	    	System.err.format("IOException: %s%n", e);
	    	return "";
	    }		
	}
	public static String getJsonFilePath(String dirPath, String fileName) {
		String prefix = File.separator + dirPath + File.separator + fileName;
		String suffix = "json";
		String filePath = String.format("%s.%s", prefix, suffix); 
		return getAbsResourceFilePath(filePath);
	}

}
