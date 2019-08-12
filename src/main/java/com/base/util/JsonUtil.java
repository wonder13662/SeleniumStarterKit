package com.base.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonUtil {
	
	public static void writeJson(JSONObject obj, String path) {
		// https://www.mkyong.com/java/json-simple-example-read-and-write-json/
        try (FileWriter file = new FileWriter(path)) {

            file.write(obj.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }		
	}

	public static JSONObject readJson(String path) {
		JSONParser parser = new JSONParser();
		
        try {
            Object obj = parser.parse(new FileReader(path));
            return (JSONObject) obj;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }	
        
        return null;
	}
	
	
	public static String getString(JSONObject json, String key) {
		if(isValid(json, key)) {
			return (String)json.get(key);
		}
		return null;
	}
	
	private static boolean isValid(JSONObject json, String key) {
		return json != null && json.containsKey(key);
	} 
}
