package com.qubaopen.survey.controller.booking;

public class dddd {
	public static void main(String[] args) {
		def time = 24
		
		def str = '123456789012345678901234'
		for (i in 0..time - 1) {
			println str.substring(i, i+1)
			println str.substring(0, i) + 'a' + str.substring(i + 1)
		}
	}
}
