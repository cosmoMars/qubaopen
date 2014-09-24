package com.qubaopen.survey.controller;

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import com.mathworks.toolbox.javabuilder.MWClassID
import com.mathworks.toolbox.javabuilder.MWNumericArray

import createFit.MatFactory

@RestController
@RequestMapping(value = 'point')
public class PointController {
	
	@RequestMapping(value = 'printPoint', method = RequestMethod.POST)
	printPoint() {
		MatFactory matFactory = new MatFactory();
		
		Double[] d1, d2;
		Object[] o1;
		d1 = new Double[16];
		d2 = new Double[16];
		o1 = new Object[16];

		for (int i = 0; i < 16; i++) {
			d1[i] = new Double(i);
			d2[i] = new Double(i);
			o1[i] = i;
		}

		MWNumericArray n1 = new MWNumericArray(d1, MWClassID.DOUBLE);

		MWNumericArray n2 = new MWNumericArray(d2, MWClassID.DOUBLE);

		Object[] oList = new Object[2];
		oList[0] = d1;
		oList[1] = d2;


		Object[] result = matFactory.createFit(1, n1, n2);

		MWNumericArray a = new MWNumericArray(result[0], MWClassID.DOUBLE);

		System.out.println(a);
		System.out.println(a.getDouble());

		MWNumericArray temp = (MWNumericArray) result[0];

		double[][] weights = (double[][]) temp.toDoubleArray();

		def list = []
		for (int i = 0; i < weights.length; i++) {

			for (int j = 0; j < weights[i].length; j++) {

				System.out.print(i + "," + j + "   :");
				System.out.println(weights[i][j]);
				list << weights[i][j]
			}

		}
		list
	}
}
