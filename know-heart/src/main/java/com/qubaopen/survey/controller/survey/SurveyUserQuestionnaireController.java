package com.qubaopen.survey.controller.survey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.survey.SurveyUserQuestionnaire;
import com.qubaopen.survey.repository.survey.SurveyUserQuestionnaireRepository;

@RestController
@RequestMapping("surveyUserQuestionnaires")
public class SurveyUserQuestionnaireController extends AbstractBaseController<SurveyUserQuestionnaire, Long> {

	@Autowired
	private SurveyUserQuestionnaireRepository surveyUserQuestionnaireRepository;

	@Override
	protected MyRepository<SurveyUserQuestionnaire, Long> getRepository() {
		return surveyUserQuestionnaireRepository;
	}

}
