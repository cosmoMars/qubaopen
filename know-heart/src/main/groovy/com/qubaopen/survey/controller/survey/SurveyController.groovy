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

@RestController
@RequestMapping("surveys")
public class SurveyController extends AbstractBaseController<Survey, Long> {

	@Autowired
	SurveyRepository surveyRepository

	@Autowired
	SurveyQuotaRepository surveyQuotaRepository

	@Autowired
	UserQuotaRepository userQuotaRepository

	@Override
	protected MyRepository<Survey, Long> getRepository() {
		surveyRepository
	}

	/**
	 * 查找用户的问卷
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = 'retrieveSurvey/{userId}', method = RequestMethod.GET)
	retrieveSurvey(@PathVariable long userId) {

		def userQuota = userQuotaRepository.findOne(userId)

		def surveyNoQuota = surveyRepository.findSurveyWithoutQuota()

		if (!userQuota) {
			return surveyNoQuota
		}

		def userAge = (int)(((new Date()).time - userQuota.birthday.time)/1000/60/60/24/365)

		def surveyQuotas = surveyQuotaRepository.findAllByIsActivated(true)

		def resultSurvey = [] as Set

		surveyQuotas.each {
			def isMatch = true
			if (isMatch && it.sex) {
				if (userQuota.sex.toString() != it.sex.toString()) {
					isMatch = false
				}
			}
			if (isMatch && it.minAge) {
				if (userAge < it.minAge - 1) {
					isMatch = false
				}
			}
			if (isMatch && it.maxAge) {
				if (userAge > it.maxAge + 1) {
					isMatch = false
				}
			}
			if (isMatch && it.provinceCode) {
				if (userQuota.regionCode != it.provinceCode) {
					isMatch = false
				}
			}
			if (isMatch) {
				resultSurvey = resultSurvey + it.survey
			}
		}

		return resultSurvey
	}

}
