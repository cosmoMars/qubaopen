package com.qubaopen.survey.controller.survey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.survey.SurveyQuestionGroup;
import com.qubaopen.survey.repository.survey.SurveyQuestionGroupRepository;

@RestController
@RequestMapping("surveyQuestionGroups")
public class SurveyQuestionGroupController extends AbstractBaseController<SurveyQuestionGroup, Long> {

	@Autowired
	private SurveyQuestionGroupRepository surveyQuestionGroupRepository;

	@Override
	protected MyRepository<SurveyQuestionGroup, Long> getRepository() {
		return surveyQuestionGroupRepository;
	}

}
