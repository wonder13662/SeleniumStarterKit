package com.base.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class NumberUtil {
	
	public static String getDecimalStr(BigDecimal bd, int decimalPlaces) {
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(decimalPlaces);
		return String.format("%s", df.format(bd));
	}
	
	public static int getRandom(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}
	
	private static int getLength(long number) {
		// https://www.baeldung.com/java-number-of-digits-in-int
		int length = 0;
		long temp = 1;
		while (temp <= number) {
		    length++;
		    temp *= 10;
		}
		return length;		
	}
	
	public static BigDecimal getRandom(String min, String max, int scale) {
		BigDecimal minBd = new BigDecimal(min);
		BigDecimal maxBd = new BigDecimal(max);
		
	    BigDecimal randomBigDecimal = minBd.add(new BigDecimal(Math.random()).multiply(maxBd.subtract(minBd)));
	    return randomBigDecimal.setScale(scale,BigDecimal.ROUND_HALF_UP);		
	}
	
	public static String getRandomNumberDigits(long min, long max) {
		int length = getLength(max);
		long randomNum = ThreadLocalRandom.current().nextLong(0, max + 1);
		return getNumberDigitsStr(randomNum, length);		
		
	}
	
	public static String getNumberDigitsStr(long number, int digits) {
		// 원하는 자리수만큼 앞에 '0'을 덧붙여 줍니다. (scale)
		String result = "" + number;
		long place = 1;
		int length = digits - 1;
		for (int i = 0; i < length; i++) {
			place *= 10;
			long quotient = number/place;
			if(quotient < 1) {
				result = "0" + result;
			}
		}
		
		return result;
	}
	
	public static boolean getRandomBoolean() {
	    Random random = new Random();
	    return random.nextBoolean();
	}
	
	public static BigDecimal getNegativeBigDecimal() {
		return new BigDecimal("-1.00").setScale(2);
	}
	
	public static BigDecimal getZeroBigDecimal() {
		return new BigDecimal("0.00").setScale(2);
	}
	
	public static boolean isEqual(BigDecimal a, BigDecimal b) {
		return a.setScale(2).compareTo(b.setScale(2)) == 0;
	}
	
	public static boolean isGreaterThan(BigDecimal a, BigDecimal b) {
		return a.setScale(2).compareTo(b.setScale(2)) == 1;
	}
	
	public static boolean isGreaterThanZero(BigDecimal a) {
		return a.setScale(2).compareTo(getZeroBigDecimal()) == 1;
	}
	
	public static boolean isGreaterThanOrEqualZero(BigDecimal a) {
		boolean isGreaterThanZero = isGreaterThanZero(a);
		boolean isEqaulToZero = isEqual(a, getZeroBigDecimal());
		return isGreaterThanZero || isEqaulToZero;
	}
	
	public static boolean isLessThan(BigDecimal a, BigDecimal b) {
		return a.setScale(2).compareTo(b.setScale(2)) == -1;
	}
	
	public static boolean isLessThanZero(BigDecimal a) {
		return a.setScale(2).compareTo(getZeroBigDecimal()) == -1;
	}
	
	public static String getDecimalFormatNumStr(int digitCnt, int num) {
		String digitCntStr = "";
		for (int i = 0; i < digitCnt; i++) {
			digitCntStr += "0";
		}
		
		DecimalFormat myFormatter = new DecimalFormat(digitCntStr);
		return myFormatter.format(num);		
	}
}
