package com.qubaopen.survey.controller.survey

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.survey.SurveyQuestion
import com.qubaopen.survey.repository.survey.SurveyQuestionRepository
import com.qubaopen.survey.service.survey.SurveyService

@RestController
@RequestMapping('surveyQuestions')
public class SurveyQuestionController extends AbstractBaseController<SurveyQuestion, Long> {

	@Autowired
	SurveyQuestionRepository surveyQuestionRepository

	@Autowired
	SurveyService surveyService

	@Override
	protected MyRepository<SurveyQuestion, Long> getRepository() {
		surveyQuestionRepository
	}


	/**
	 * 查询问卷选项以及逻辑
	 * @param surveyId
	 * @return
	 */
	@RequestMapping(value = 'findBySurvey/{surveyId}', method = RequestMethod.GET)
	findBySurvey(@PathVariable long surveyId) {

		logger.trace(" -- 查询问卷选项以及逻辑 -- ")

		surveyService.findBySurvey(surveyId)
	}



}
