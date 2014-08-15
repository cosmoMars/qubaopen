package com.qubaopen.survey.controller.survey

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.survey.Survey
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.repository.survey.SurveyQuotaRepository
import com.qubaopen.survey.repository.survey.SurveyRepository
import com.qubaopen.survey.repository.user.UserQuotaRepository
import com.qubaopen.survey.service.survey.SurveyService

@RestController
@RequestMapping('surveys')
@SessionAttributes('currentUser')
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
	@RequestMapping(value = 'retrieveSurvey', method = RequestMethod.GET)
	retrieveSurvey(@ModelAttribute('currentUser') User user) {

		logger.trace ' -- 获取用户的问卷 -- '

		surveyService.retrieveSurvey(user.id)
	}

	/**
	 *
	 * 保存调研问卷结果
	 * @param userId
	 * @param surveyId
	 * @param questionOptionIds
	 * @return
	 */
	@RequestMapping(value = 'saveSurveyResult', method = RequestMethod.GET)
	saveSurveyResult(@RequestParam long userId, @RequestParam long surveyId, @RequestParam String questionJson) {

		logger.trace(" -- 保存调研问卷结果 -- ")

		surveyService.saveSurveyResult(userId, surveyId, questionJson)

	}

}
