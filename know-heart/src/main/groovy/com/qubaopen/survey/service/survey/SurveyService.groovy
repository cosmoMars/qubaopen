package com.qubaopen.survey.service.survey

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.qubaopen.survey.controller.survey.SurveyQuestionController
import com.qubaopen.survey.entity.survey.Survey
import com.qubaopen.survey.entity.survey.SurveyQuestion
import com.qubaopen.survey.entity.survey.SurveyQuestionOption
import com.qubaopen.survey.entity.survey.SurveyUserAnswer
import com.qubaopen.survey.entity.survey.SurveyUserQuestionnaire
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.vo.QuestionVo
import com.qubaopen.survey.repository.survey.SurveyUserAnswerRepository
import com.qubaopen.survey.repository.survey.SurveyUserQuestionnaireRepository

@Service
public class SurveyService {

	@Autowired
	SurveyQuestionController surveyQuestionController

	@Autowired
	SurveyUserQuestionnaireRepository surveyUserQuestionnaireRepository

	@Autowired
	SurveyUserAnswerRepository surveyUserAnswerRepository

	@Transactional
	void saveQuestionnaireAndUserAnswer(User user, Survey survey, List<QuestionVo> questionVos) {

		def surveyUserQuestionnaire = new SurveyUserQuestionnaire(
			survey : survey,
			user : user,
			createDate : new Date(),
			status : SurveyUserQuestionnaire.status.ADUITING,
			transmit : SurveyUserQuestionnaire.Transmit.NOTRANSMIT
		)
		surveyUserQuestionnaire = surveyUserQuestionnaireRepository.save(surveyUserQuestionnaire)

		def userAnswers = []

		questionVos.each { vo ->
			def question = new SurveyQuestion(id : vo.questionId)
			if (vo.choiceIds && vo.choiceIds.length > 0) { // 单选或多选
				vo.choiceIds.each { cId ->
					def answer = new SurveyUserAnswer(
						user : user,
						surveyUserQuestionnaire : surveyUserQuestionnaire,
						surveyQuestion : question,
						surveyQuestionOption : new SurveyQuestionOption(id : cId)
					)
					userAnswers << answer
				}
			} else if (vo.content && !vo.content.isEmpty) { // 问答题
				def answer = new SurveyUserAnswer(
					user : user,
					surveyUserQuestionnaire : surveyUserQuestionnaire,
					surveyQuestion : question,
					content : vo.content
				)
				userAnswers << answer
			} else if (vo.orderIds && vo.orderIds.length > 0) { // 排序题
				vo.orderIds.eachWithIndex { oId, index ->
					def answer = new SurveyUserAnswer(
						user : user,
						surveyUserQuestionnaire : surveyUserQuestionnaire,
						surveyQuestion : question,
						surveyQuestionOption : new SurveyQuestionOption(id : oId),
						turn : index + 1
					)
					userAnswers << answer
				}
			}
			surveyUserAnswerRepository.save(userAnswers)
		}
	}
}
