package com.qubaopen.survey.service.mindmap;

import org.springframework.stereotype.Service

@Service
public class CalculateT {
	
	def c1 = 61.5, c2 = 56.7, c3 = 43.3, c4 = 38.5

	def calT(int x, double m, double sd) {
		def bd = new BigDecimal(50 + 10 * (x - m) / sd)
		if (bd > 100) {
			bd = 100.00
		}
		bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	def calLevel(double e, double n) {
		def level
		if (e > c3 && e < c2 && n > c3 && n < c2) {
			level = 1
		} else if (e >= c2 && e < c1 && n > c4 && n < c1) {
			level = 2
		} else if (e > c4 && e <= c3 && n > c4 && n < c1) {
			level = 2
		} else if (e > c4 && e < c1 && n > c4 && n <= c3) {
			level = 2
		} else if (e > c4 && e < c1 && n >= c3 && n < c1) {
			level = 2
		} else {
			level = 3
		}
	}
}
