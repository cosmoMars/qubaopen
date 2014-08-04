package com.qubaopen.survey.controller.survey

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.survey.Survey
import com.qubaopen.survey.repository.survey.SurveyQuotaRepository
import com.qubaopen.survey.repository.survey.SurveyRepository
import com.qubaopen.survey.repository.user.UserQuotaRepository
import com.qubaopen.survey.service.survey.SurveyService

@RestController
@RequestMapping("surveys")
public class SurveyController extends AbstractBaseController<Survey, Long> {

	@Autowired
	SurveyRepository surveyRepository

	@Autowired
	SurveyQuotaRepository surveyQuotaRepository

	@Autowired
	UserQuotaRepository userQuotaRepository

	@Autowired
	SurveyService surveyService

	@Override
	protected MyRepository<Survey, Long> getRepository() {
		surveyRepository
	}

	/**
	 * 获取用户的问卷
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = 'retrieveSurvey/{userId}', method = RequestMethod.GET)
	retrieveSurvey(@PathVariable long userId) {

		logger.trace ' -- 获取用户的问卷 -- '

		surveyService.retrieveSurvey(userId)
	}

}
