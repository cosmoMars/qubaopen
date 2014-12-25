package com.qubaopen.survey.service.mindmap;

public class ddd {
	public static void main(String[] args) {
		def map = [:]
//		def map = [C:15, D:56, AB:102]
		
		map << [C:15]
		map << [D:56]
		map << [AB:102]
		println map
		println map['C']
		
		def m = ["C":15, "D":56, "AB":102]
		
		println m['C']
	}
}
