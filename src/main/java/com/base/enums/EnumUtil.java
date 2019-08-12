package com.base.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.base.util.RandomUtil;

public class EnumUtil {

	private EnumUtil() {}
	
	public static <E extends Enum<E> & EnumNameOnViewMap> Optional<E> fromNameOnView(Class<E> enumData, String symbol) {
		Map<String, E> map = new HashMap<>();
		Arrays.asList(enumData.getEnumConstants()).forEach(e -> map.put(e.getNameOnView(), e));
		
		// TODO Why doesn't this work?
//		Map<String, E> map = Stream.of(enumData.getEnumConstants()).collect(Collectors.toMap(EnumInSelenium::getNameOnView, e -> e));
		return Optional.ofNullable(map.get(symbol));
	}
	
	public static <E extends Enum<E>> Optional<E> fromName(Class<E> enumData, String symbol) {
		Map<String, E> map = Stream.of(enumData.getEnumConstants()).collect(Collectors.toMap(Object::toString, e -> e));
		return Optional.ofNullable(map.get(symbol));
    }
	
	public static <E extends Enum<E>> E getRandomItem(Class<E> enumData) {
		List<E> list = Stream.of(enumData.getEnumConstants()).collect(Collectors.toList());
		return RandomUtil.getItem(list);
	}
}
