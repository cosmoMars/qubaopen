package com.qubaopen.survey.controller.point;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

import createFit.MatFactory;


/**
 * Created by duel on 2014/9/14 16:40. matlab
 */
@RestController
@RequestMapping(value = "point")
public class PointController {

	@RequestMapping(value = "getPoint", method = RequestMethod.POST)
	public List<Double> getPoint () throws MWException {

		MatFactory matFactory = new MatFactory();
		Double[] d1, d2;
//		Object[] o1;
		d1 = new Double[16];
		d2 = new Double[16];
//		o1 = new Object[16];

		for (int i = 0; i < 16; i++) {
			d1[i] = new Double(i);
			d2[i] = new Double(i);
//			o1[i] = i;
		}

		MWNumericArray n1 = new MWNumericArray(d1, MWClassID.DOUBLE);

		MWNumericArray n2 = new MWNumericArray(d2, MWClassID.DOUBLE);

//		Object[] oList = new Object[2];
//		oList[0] = d1;
//		oList[1] = d2;

		Object[] result = matFactory.createFit(1, n1, n2);

//		MWNumericArray a = new MWNumericArray(result[0], MWClassID.DOUBLE);
		MWNumericArray temp = (MWNumericArray) result[0];

		double[][] weights = (double[][]) temp.toDoubleArray();
		List<Double> console = new ArrayList<Double>();
		for (int i = 0; i < weights.length; i++) {

			for (int j = 0; j < weights[i].length; j++) {
				console.add(weights[i][j]);
			}
		}
		return console;
	}
}
