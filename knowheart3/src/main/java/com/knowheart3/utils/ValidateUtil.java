package com.knowheart3.utils;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class ValidateUtil {

	/**
	 * 判断是否为合法的手机号码
	 */
	public static boolean validatePhone(String phone) {
		return isNotEmpty(phone) && phone.matches("^1[0-9]{10}$");
	}

	/**
	 * 判断是否符合标准密码
	 */
	public static boolean validatePwd(String pwd) {
		return isNotEmpty(pwd) && pwd.matches("^[a-zA-Z0-9_]{8,30}$");
	}

	/**
	 * 判断邮箱是否符合格式
	 */
	public static boolean validateEmail(String email) {
		return isNotEmpty(email) && email.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	}
	
    /**
     * 检查是否只包含 汉字数字字母_
     * @param string
     * @return
     */
    public static boolean validateNormalString(String string) {
    	return isNotEmpty(string) && string.matches("^[\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w]+$");
    }
	
	/**
	 * 检验身份证是否合法
	 * @param arrIdCard
	 * @return
	 */
	public static boolean isIdCard(String arrIdCard) {
		int sigma = 0;
		Integer[] a = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
		String[] w = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
		for (int i = 0; i < 17; i++) {
			int ai = Integer.parseInt(arrIdCard.substring(i, i + 1));
			int wi = a[i];
			sigma += ai * wi;
		}
		int number = sigma % 11;
		String check_number = w[number];
		if (!arrIdCard.substring(17).equalsIgnoreCase(check_number)) {
			return false;
		} else {
			return true;
		}
	}
	
}
