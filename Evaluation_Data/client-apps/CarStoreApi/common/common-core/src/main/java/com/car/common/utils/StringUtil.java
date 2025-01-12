package com.car.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串工具类
 *
 * @author shb
 * @date 2018-11-10 上午11:03:50
 */
public class StringUtil {

	private static Pattern p = Pattern.compile("\\s*|\t|\r|\n");

	/**
	 * 是否为空
	 *
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		return str == null || str.isEmpty() || str.replaceAll(" ", "").isEmpty();
	}

	public static boolean isBlank(String... strs) {
		for (int i = 0; i < strs.length; i++) {
			if (isBlank(strs[i])) {
				return true;
			}
		}
		return false;
	}

	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	public static boolean contains(String str, String key) {
		return str != null && str.contains(key);
	}

	public static boolean contains(String... strs) {
		for (int i = 0; i < strs.length - 1; i++) {
			if (contains(strs[i], strs[strs.length - 1])) {
				return true;
			}
		}
		return false;
	}

	public static boolean contains(String[] strs, String key) {
		for (int i = 0; i < strs.length; i++) {
			if (contains(strs[i], key)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 首字母大写
	 *
	 * @param in
	 * @return
	 */
	public static String upperHeadChar(String in) {
		String head = in.substring(0, 1);
		String out = head.toUpperCase() + in.substring(1);
		return out;
	}

	/**
	 * 替换空格、回车、换行等特殊字符
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * 使用逗号切分字符串并返回切分后获得的数据集合:最少返回size=0的集合，不会返回null值;祛除空串，重复的串
	 * @param s
	 * @return
	 */
	public static List<String> splitDefDistinctNotBlank(String s) {
		return splitDef(s).stream().distinct().filter(o -> StringUtils.isNotBlank(o)).collect(Collectors.toList());
	}

	/**
	 * 使用逗号切分字符串并返回切分后获得的数据集合:最少返回size=0的集合，不会返回null值
	 * @param s
	 * @return
	 */
	public static List<String> splitDef(String s) {
		List<String> arr = new ArrayList<>();
		if (StringUtils.isNotBlank(s)) {
			arr.addAll(Arrays.asList(s.split(",")));
		}
		return arr;
	}

}
