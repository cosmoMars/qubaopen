package com.qubaopen.survey.controller.mindmap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.mindmap.MapCoefficient;
import com.qubaopen.survey.repository.mindmap.MapCoefficientRepository;

@RestController
@RequestMapping("mapCoefficients")
public class MapCoefficientController extends AbstractBaseController<MapCoefficient, Long> {

	@Autowired
	private MapCoefficientRepository mapCoefficientRepository;

	@Override
	protected MyRepository<MapCoefficient, Long> getRepository() {
		return mapCoefficientRepository;
	}

}
