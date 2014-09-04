package com.qubaopen.survey.service.survey

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.fasterxml.jackson.databind.ObjectMapper
import com.qubaopen.survey.entity.interest.InterestQuestion
import com.qubaopen.survey.entity.survey.Survey
import com.qubaopen.survey.entity.survey.SurveyQuestion
import com.qubaopen.survey.entity.survey.SurveyQuestionOption
import com.qubaopen.survey.entity.survey.SurveyUserAnswer
import com.qubaopen.survey.entity.survey.SurveyUserQuestionnaire
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.vo.QuestionVo
import com.qubaopen.survey.repository.survey.SurveyLogicRepository
import com.qubaopen.survey.repository.survey.SurveyQuestionRepository
import com.qubaopen.survey.repository.survey.SurveyQuotaRepository
import com.qubaopen.survey.repository.survey.SurveyRepository
import com.qubaopen.survey.repository.survey.SurveyUserAnswerRepository
import com.qubaopen.survey.repository.survey.SurveyUserQuestionnaireRepository
import com.qubaopen.survey.repository.user.UserQuotaRepository

@Service
public class SurveyService {

	@Autowired
	SurveyUserQuestionnaireRepository surveyUserQuestionnaireRepository

	@Autowired
	SurveyUserAnswerRepository surveyUserAnswerRepository

	@Autowired
	SurveyQuestionRepository surveyQuestionRepository

	@Autowired
	SurveyLogicRepository surveyLogicRepository

	@Autowired
	UserQuotaRepository userQuotaRepository

	@Autowired
	SurveyRepository surveyRepository

	@Autowired
	SurveyQuotaRepository surveyQuotaRepository

	@Autowired
	ObjectMapper objectMapper

	/**
	 * 获取调研问卷
	 * @param userId
	 * @return
	 */
	@Transactional
	retrieveSurvey(long userId){
		def userQuota = userQuotaRepository.findOne(userId)

		def surveyNoQuota = surveyRepository.findSurveyWithoutQuota()

		if (!userQuota) {
			return surveyNoQuota
		}

		def userAge = (int)(((new Date()).time - userQuota.birthday.time)/1000/60/60/24/365)

		def surveyQuotas = surveyQuotaRepository.findAllByActivated(true)

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
			if (isMatch && it.areaCode) {
				if (userQuota.areaCode != it.areaCode) {
					isMatch = false
				}
			}
			if (isMatch) {
				resultSurvey = resultSurvey + it.survey
			}
		}

		return resultSurvey
	}

	/**
	 * 查询问卷选项以及逻辑
	 * @param surveyId
	 * @return
	 */
	@Transactional
	findBySurvey(long surveyId) {
		def survey = new Survey(id : surveyId),
			surveyQuestions = surveyQuestionRepository.findAllBySurvey(survey)

		def surveyLogics = []

		if (surveyQuestions) {
			surveyLogics = surveyLogicRepository.findAlLBySurveyQuestions(surveyQuestions)
		}

		def result = [
			'surveyQuestions' : surveyQuestions,
			'surveyLogics' : surveyLogics
		]
	}

	/**
	 *
	 * 保存调研问卷
	 * @param userId
	 * @param surveyId
	 * @param questionJson
	 * @return
	 */
	@Transactional
	saveSurveyResult(long userId, long surveyId, String questionJson) {
		def user = new User(id : userId),
			survey = new Survey(id : surveyId)

		def javaType = objectMapper.typeFactory.constructParametricType(ArrayList.class, QuestionVo.class)
		def questionVos = objectMapper.readValue(questionJson, javaType)

		def ids = []
		questionVos.each {
			ids << it.questionId
		}

		def questions = surveyQuestionRepository.findAll(ids)

//		saveQuestionnaireAndUserAnswer(user, survey, questions, questionVos)

		return '{"success" : 1}'

	}

	/**
	 * @param user
	 * @param survey
	 * @param questions
	 * @param questionVos
	 */
	@Transactional
	void saveQuestionnaireAndUserAnswer(User user, Survey survey, List<InterestQuestion> questions, List<QuestionVo> questionVos) {

		def surveyUserQuestionnaire = new SurveyUserQuestionnaire(
			survey : survey,
			user : user,
			createdDate : new Date(),
			status : SurveyUserQuestionnaire.Status.ADUITING,
			transmit : SurveyUserQuestionnaire.Transmit.NOTRANSMIT
		)
		surveyUserQuestionnaire = surveyUserQuestionnaireRepository.save(surveyUserQuestionnaire)

		def userAnswers = []

		questions.each { q ->
			def type = q.type

			def answer = null

			questionVos.find { vo ->
				if (vo.questionId == q.id) {
					if (type == SurveyQuestion.Type.SINGLE) { // 单选
						def choiceId = vo.contents[0].id
						answer = new SurveyUserAnswer(
							user : user,
							surveyUserQuestionnaire : surveyUserQuestionnaire,
							surveyQuestion : q,
							surveyQuestionOption : new SurveyQuestionOption(id : choiceId)
						)
						userAnswers << answer
					}
					if (type == SurveyQuestion.Type.MULTIPLE) { // 多选
						vo.contents.each { voc ->
							answer = new SurveyUserAnswer(
								user : user,
								surveyUserQuestionnaire : surveyUserQuestionnaire,
								surveyQuestion : q,
								surveyQuestionOption : new SurveyQuestionOption(id : voc.id)
							)
							userAnswers << answer
						}
					}
					if (type == SurveyQuestion.Type.QA) { // 问答
						vo.contents.each { voc ->
							answer = new SurveyUserAnswer(
								user : user,
								surveyUserQuestionnaire : surveyUserQuestionnaire,
								surveyQuestion : q,
								content : voc.cnt
							)
							userAnswers << answer
						}
					}
					if (type == SurveyQuestion.Type.SORT) { // 排序
						vo.contents.each { voc ->
							answer = new SurveyUserAnswer(
								user : user,
								surveyUserQuestionnaire : surveyUserQuestionnaire,
								surveyQuestion : q,
								surveyQuestionOption : new SurveyQuestionOption(id : voc.id),
								turn : voc.order
							)
						userAnswers << answer
						}
					}
				}
			}
			surveyUserAnswerRepository.save(userAnswers)
		}
	}
}
