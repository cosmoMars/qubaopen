package com.qubaopen.survey;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomStringTest {
	
	public static void main(String[] args) {
		for (int i = 1; i <= 10; i++) {
			String randomNumericStr = RandomStringUtils.randomNumeric(6);
			System.out.println("randomNumericStr" + i + " ============ " + randomNumericStr);
		}
	}
	
}
