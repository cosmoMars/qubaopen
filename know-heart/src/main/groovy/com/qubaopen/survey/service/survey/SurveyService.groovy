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
import com.qubaopen.survey.repository.survey.SurveyUserAnswerRepository
import com.qubaopen.survey.repository.survey.SurveyUserQuestionnaireRepository

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
	ObjectMapper objectMapper

	/**
	 * 查找survey问卷
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

		saveQuestionnaireAndUserAnswer(user, survey, questions, questionVos)

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
			def type = q.type.toString()

			def answer = null

			questionVos.find { vo ->
				if (vo.questionId == q.id) {
					if (type == SurveyQuestion.Type.SINGLE.toString() && vo.choiceIds.length <= q.optionCount) { // 单选
						answer = new SurveyUserAnswer(
							user : user,
							surveyUserQuestionnaire : surveyUserQuestionnaire,
							surveyQuestion : q,
							surveyQuestionOption : new SurveyQuestionOption(id : vo.choiceIds[0])
						)
						userAnswers << answer
					}
					if (type == SurveyQuestion.Type.MULTIPLE.toString() && vo.choiceIds.length <= q.optionCount) { // 多选
						vo.choiceIds.each { cId ->
							answer = new SurveyUserAnswer(
								user : user,
								surveyUserQuestionnaire : surveyUserQuestionnaire,
								surveyQuestion : q,
								surveyQuestionOption : new SurveyQuestionOption(id : cId)
							)
							userAnswers << answer
						}
					}
					if (type == SurveyQuestion.Type.QA.toString() && !vo.content.empty) { // 问答
						answer = new SurveyUserAnswer(
							user : user,
							surveyUserQuestionnaire : surveyUserQuestionnaire,
							surveyQuestion : q,
							content : vo.content
						)
						userAnswers << answer
					}
					if (type == SurveyQuestion.Type.SORT.toString() && vo.orderIds.length <= q.optionCount) { // 排序
						vo.orderIds.eachWithIndex { oId, index ->
						answer = new SurveyUserAnswer(
							user : user,
							surveyUserQuestionnaire : surveyUserQuestionnaire,
							surveyQuestion : q,
							surveyQuestionOption : new SurveyQuestionOption(id : oId),
							turn : index + 1
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
