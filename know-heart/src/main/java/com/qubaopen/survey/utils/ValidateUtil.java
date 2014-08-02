package com.qubaopen.survey.utils;

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
}
