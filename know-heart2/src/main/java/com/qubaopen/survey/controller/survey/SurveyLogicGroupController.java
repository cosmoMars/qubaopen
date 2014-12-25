package com.qubaopen.survey.controller.survey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.survey.SurveyLogicGroup;
import com.qubaopen.survey.repository.survey.SurveyLogicGroupRepository;

@RestController
@RequestMapping("surveyLogicGroups")
public class SurveyLogicGroupController extends AbstractBaseController<SurveyLogicGroup, Long> {

	@Autowired
	private SurveyLogicGroupRepository surveyLogicGroupRepository;

	@Override
	protected MyRepository<SurveyLogicGroup, Long> getRepository() {
		return surveyLogicGroupRepository;
	}

}
