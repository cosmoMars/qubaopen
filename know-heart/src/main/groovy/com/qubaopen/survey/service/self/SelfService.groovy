package com.qubaopen.survey.service.self

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import com.qubaopen.survey.entity.interest.InterestUserQuestionnaire
import com.qubaopen.survey.entity.self.Self
import com.qubaopen.survey.entity.self.SelfQuestion
import com.qubaopen.survey.entity.self.SelfQuestionOption
import com.qubaopen.survey.entity.self.SelfResultOption
import com.qubaopen.survey.entity.self.SelfUserAnswer
import com.qubaopen.survey.entity.self.SelfUserQuestionnaire
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.vo.QuestionVo
import com.qubaopen.survey.repository.self.SelfUserAnswerRepository
import com.qubaopen.survey.repository.self.SelfUserQuestionnaireRepository

@Service
public class SelfService {

	@Autowired
	SelfUserQuestionnaireRepository selfUserQuestionnaireRepository

	@Autowired
	SelfUserAnswerRepository selfUserAnswerRepository

	void saveQuestionnaireAndUserAnswer(User user, Self self, List<QuestionVo> quesitonVos, List<SelfQuestionOption> options, SelfResultOption resultOption) {

		def selfUserQuestionnaire = new SelfUserQuestionnaire(
			user : user,
			self : self,
			selfResultOption : resultOption,
			status : InterestUserQuestionnaire.Status.COMPLETED,
			transmit : InterestUserQuestionnaire.Transmit.NOTRANSMIT,
			time : new Date()
		)
		selfUserQuestionnaire = selfUserQuestionnaireRepository.save(selfUserQuestionnaire)

		def userAnswers = []
		quesitonVos.each { vo ->

			def question = new SelfQuestion(id : vo.questionId)

			if (vo.choiceIds && vo.choiceIds.length > 0) { //单选或多选
				def option = null
				vo.choiceIds.each { cId ->
					options.find { o ->
						if (o.id == cId) {
							option = o
						}
					}
					def answer = new SelfUserAnswer(
						user : user,
						selfUserQuestionnaire : selfUserQuestionnaire,
						selfQuestionOption : option,
						score : option.score
					)
					userAnswers << answer
				}
			} else if (vo.content && !vo.content.isEmpty()) { // 问答题
				def answer = new SelfUserAnswer(
					user : user,
					selfUserQuestionnaire : selfUserQuestionnaire,
					interestQuestion : question,
					content : vo.content
				)
				userAnswers << answer
			} else if (vo.orderIds && vo.orderIds.length > 0) { //排序题
				vo.orderIds.eachWithIndex { oId, index ->
					def selfQuestionOption = new SelfQuestionOption(id : oId),
						answer = new SelfUserAnswer(
							user : user,
							selfUserQuestionnaire : selfUserQuestionnaire,
							interestQuestion : question,
							selfQuestionOption : selfQuestionOption,
							turn : index + 1
						)
					userAnswers << answer
				}
			}
		}
		selfUserAnswerRepository.save(userAnswers)

	}
}
