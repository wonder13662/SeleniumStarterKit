package com.base.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.base.constant.BaseConstant.Browser;
import com.vdurmont.emoji.EmojiParser;

public class StringUtil {
	public static boolean isNotEmpty(String a) {
		return !isEmpty(a);
	}
	
	public static boolean isEmpty(String a) {
		if(a == null || a.equals("")) {
			return true;
		}
		String trucatedStr = a.replaceAll(" ", "");
		if(trucatedStr.equals("")) {
			return true;
		}
		return false;
	}
	
	public static String substring(String text, int max) {
		int maxLength = text.length() > max?max:text.length();
		return text.substring(0, maxLength);
	}
	
	public static boolean contains(String a, String b) {
		return a.toLowerCase().trim().contains(b.toLowerCase().trim());
	}
	
	public static boolean equalsAfterTrim(String a, String b) {
		if(a == null || b == null) {
			return false;
		}
		return a.toLowerCase().trim().equals(b.toLowerCase().trim());
	}
	
	public static String getTextWithEmoji(Browser browser) {
		if(browser == Browser.CHROME) {
			return "Chrome doesn't support emoji at Selenium.";
		} else if(browser == Browser.IE11) {
			return "Internet Explorer doesn't support emoji at Selenium.";
		}
		
		String str = "An :grinning:awesome :smiley:string &#128516;with a few :wink:emojis!";
		return EmojiParser.parseToUnicode(str);		
	}
	
	public static String getComplexText(int max) {
		String complextText = getComplexText();
		return substring(complextText, max);
	}
	
	public static String getComplexText() {
		// Long text + Special characters + Numbers + Non-Alphabet characters
		// TODO 이런제약의 관련 문서가 있었는데 찾아야 함!
		
		return String.format("%s%s%s%s", getNonAlphabetCharacters(), getLongText(), getSpecialCahracters(), getNumbers());
	}
	
	private static String getLongText() {
		// http://www.blindtextgenerator.com/lorem-ipsum
		return "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. ";
	}
	
	public static String getSpecialCharaters(int length) {
		List<String> list = RandomUtil.getList(getSpecialCharacterList(), length);		
		return String.join("", list);
	}
	
	public static List<String> getSpecialCharacterList() {
		return Arrays.asList("¤","¶","§","!","\"","#","$","%","&","'","(",")","*","+",",","-",".","/","0","1","2","3","4","5","6","7","8","9",":",";","<","=",">","?","@","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","[","\\","]","^","_","`","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","{","|","}","~","Ç","ü","é","â","ä","à","å","ç","ê","ë","è","ï","î","ì","æ","Æ","ô","ö","ò","û","ù","ÿ","¢","£","¥","P","ƒ","á","í","ó","ú","ñ","Ñ","¿","¬","½","¼","¡","«","»","¦","ß","µ","±","°","•","·","²","€","„","…","†","‡","ˆ","‰","Š","‹","Œ","‘","’","“","”","–","—","˜","™","š","›","œ","Ÿ","¨","©","®","¯","³","´","¸","¹","¾","À","Á","Â","Ã","Ä","Å","È","É","Ê","Ë","Ì","Í","Î","Ï","Ð","Ò","Ó","Ô","Õ","Ö","×","Ø","Ù","Ú","Û","Ü","Ý","Þ","ã","ð","õ","÷","ø","ü","ý","þ");
	}
	
	private static String getSpecialCahracters() {
		// https://medium.com/@jack_21924/sample-text-to-practice-special-characters-4b78c4335d68
		List<String> list = new ArrayList<String>();
		list.add("//// 1111 ^^^^ 2222 — — 3333 !!!! 4444 &&&& 5555 %%%% 6666 ???? 7777");
		list.add("— — 3333 ~~~~ 9999 :::: 8888 ;;;; 6776 ```` 2332 ‘’’’ 3323 “””” 4343 @@@@");
		list.add("<<<< >>>> {{{{ }}}} (((( )))) [[[[ ]]]]");
		list.add("#### 7.7 E 2");
		list.add("==== 1.2 E 3");
		list.add("%%%% 3.3 E 7");
		list.add("???? %%%% &&&& !!!! ^^^^ — — **** ++++ ====");
		
		return list.get(NumberUtil.getRandom(0, list.size()-1));
	}
	
	private static String getNumbers() {
		return "1234567890.1234567890";
	}
	
	private static String getNonAlphabetCharacters() {
		// https://wiki.collectionspace.org/display/collectionspace/Special+Characters+and+Formatting+-+QA+Test+Plan
		// 1. Chinese
		// 2. Korean
		// 3. Vietnamese
		// 4. Thai
		
		return "ñóǹ äŝçíì 汉语/漢語  华语/華語 Huáyǔ; 中文 Zhōngwén 漢字仮名交じり文 Lech Wałęsa æøå";
	}
	
	public static boolean isEmail(String email) {
		String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
		 
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	
	public static String join(List<String> list) {
		return list.stream().map(Object::toString).collect(Collectors.joining(","));
	}
	
	public static int extractNumbers(String raw) {
		String numberStr = raw.replaceAll("[^\\d]+", ""); 
		return Integer.parseInt(numberStr);
	}	
}
