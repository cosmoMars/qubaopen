package com.qubaopen.survey.controller.booking;

import java.util.Calendar;
import java.util.Date;

public class ddd {
	public static void main(String[] args) {
		Calendar  c = Calendar.getInstance();
		c.setTime(new Date());
		
//		System.out.println(c.get(Calendar.DAY_OF_MONTH));
//		System.out.println(c.getMaximum(Calendar.DAY_OF_MONTH));
		
		String str = "123456789012345678901234";
		System.out.println(str.length());
		System.out.println(new Date().getDay());
		System.out.println(str.substring(3, 4));
		System.out.println(str.replace(str.substring(3, 4), "e"));
		System.out.println(str);
		
	}
}
