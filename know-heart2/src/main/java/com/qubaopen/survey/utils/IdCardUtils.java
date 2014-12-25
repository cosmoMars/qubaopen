package com.qubaopen.survey.utils;

import org.joda.time.DateTime;

public class IdCardUtils {

	@SuppressWarnings("unused")
	private static int calculateAgeByIdCard(String idCard) {
		return Integer.valueOf(DateTime.now().getYear()) - Integer.valueOf(idCard.substring(6, 10));
	}
	
	private static boolean conformAge(String idCard) {
		int age = Integer.valueOf(DateTime.now().getYear()) - Integer.valueOf(idCard.substring(6, 10));
		return false;
	}
}
