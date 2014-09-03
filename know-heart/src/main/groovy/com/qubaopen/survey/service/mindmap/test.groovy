package com.qubaopen.survey.service.mindmap;

import org.joda.time.DateTime


public class test {

	public static void main(String[] args) {
		def map = [:]
		map.put('a', 1)
		map.put('a', 2)
		
		println map
		
		CalculateT t = new CalculateT()
		println t.calT(18, 7.44, 2.79)
		
		def str = '310226198812215319'
		println str.substring(6, 10)
		println Integer.valueOf(DateTime.now().year)
		println Integer.valueOf(DateTime.now().year) - Integer.valueOf(str.substring(6, 10))
	}
}
