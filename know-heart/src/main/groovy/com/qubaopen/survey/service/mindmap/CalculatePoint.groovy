package com.qubaopen.survey.service.mindmap;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

import createFit.MatFactory;

@Service
public class CalculatePoint {

	@Transactional
	getPoint(List<Double> time, List<Double> chart, List<Double> timeC) {
		
		MatFactory matFactory = new MatFactory()
		def d1 = time as Double[]
		def d2 = chart as Double[]

		MWNumericArray n1 = new MWNumericArray(d1, MWClassID.DOUBLE)
		MWNumericArray n2 = new MWNumericArray(d2, MWClassID.DOUBLE)

		Object[] result = matFactory.createFit(1, n1, n2)

		MWNumericArray temp = (MWNumericArray) result[0]

		double[][] weights = (double[][]) temp.toDoubleArray()
		List<Double> console = new ArrayList<Double>()
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[i].length; j++) {
				console.add(weights[i][j]);
			}
		}
		
		def chartC = []
		timeC.each {
			def resultC = console[0] + console[1] * Math.cos(it * console[3]) + console[2] * Math.sin(it * console[3])
			chartC << (int)resultC
		}
		chartC
	}
}
