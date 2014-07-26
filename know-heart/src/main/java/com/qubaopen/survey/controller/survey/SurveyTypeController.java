package com.qubaopen.survey.controller.survey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.survey.SurveyType;
import com.qubaopen.survey.repository.survey.SurveyTypeRepository;

@RestController
@RequestMapping("surveyTypes")
public class SurveyTypeController extends AbstractBaseController<SurveyType, Long> {

	@Autowired
	private SurveyTypeRepository surveyTypeRepository;

	@Override
	protected MyRepository<SurveyType, Long> getRepository() {
		return surveyTypeRepository;
	}

}
