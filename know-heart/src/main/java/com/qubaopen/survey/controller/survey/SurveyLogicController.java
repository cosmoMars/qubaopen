package com.qubaopen.survey.controller.survey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.survey.SurveyLogic;
import com.qubaopen.survey.repository.survey.SurveyLogicRepository;

@RestController
@RequestMapping("surveyLogics")
public class SurveyLogicController extends AbstractBaseController<SurveyLogic, Long> {

	@Autowired
	private SurveyLogicRepository surveyLogicRepository;

	@Override
	protected MyRepository<SurveyLogic, Long> getRepository() {
		return surveyLogicRepository;
	}

}
