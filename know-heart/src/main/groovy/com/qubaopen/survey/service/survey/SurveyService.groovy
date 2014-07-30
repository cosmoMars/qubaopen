package com.qubaopen.survey.service.survey

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.qubaopen.survey.controller.survey.SurveyQuestionController
import com.qubaopen.survey.entity.interest.InterestQuestion
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
