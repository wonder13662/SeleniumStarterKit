package com.base.util;

import java.math.BigDecimal;

public class ElementUtil {
	public static BigDecimal getAmount(String raw) {
		return getAmount(raw, 2);
	}
	public static BigDecimal getAmount(String raw, int scale) {
		// 1. html 태그 제거
		raw = removeDecoration(raw);
		
		try {
			return new BigDecimal(raw.trim()).setScale(scale);
		} catch (NumberFormatException e) {
			// e.printStackTrace();
			return new BigDecimal("0.00");
		}
	}
	
	public static String removeDecoration(String raw) {
		// 줄바꿈 문자를 공백으로 바꿈
		raw = raw.replaceAll("\\n", " ");
		// Replace multiple empty spaces to the single empty space
		raw = raw.replaceAll("\\s{2,}", " ");
		// html 태그 제거
		raw = raw.replaceAll("\\<[\\w]+.+\\>[\\w]+\\<\\/[\\w]+\\>", "");
		// &nbsp; 제거
		raw = raw.replaceAll("&nbsp;", "");
		// 별표(asterisk) 제거
//		raw = raw.replaceAll("\\*", ""); // FIX ME
		// RM 제거
		raw = raw.replaceAll("RM", "");
		// S$ 제거
		raw = raw.replaceAll("S\\$", "");
		// /mo 제거
		raw = raw.replaceAll("/mo", "");
		// user 제거
		raw = raw.replaceAll("user[s]*", "");
		// /user 제거
		raw = raw.replaceAll("/user[s]*", "");
		// 쉼표(pause) 제거
		raw = raw.replaceAll(",", "");
		// 가격없음 표시 "—" 를 0으로 바꿈.
		raw = raw.replaceAll("—", "0");
		// 시작과 끝의 공백제거
		raw = raw.trim();
		// Remove non-breaking space (https://stackoverflow.com/questions/8501072/string-unicode-remove-char-from-the-string)
		raw = raw.replace("\u00A0","");
		
		return raw;
	}
}
