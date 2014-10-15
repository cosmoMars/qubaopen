package com.qubaopen.survey.service.mindmap;

public class test {
	public static void main(String[] args) {
		def cal = Calendar.getInstance()
//		cal.add(Calendar.DAY_OF_MONTH, 1)
		cal.set(Calendar.HOUR_OF_DAY, 12)
		cal.set(Calendar.MINUTE, 0)
		cal.set(Calendar.SECOND, 0)
		println cal.getTime().getTime()
		
		def result = 1413432000267 -1413345600019
		
		println result
		
	}
}
