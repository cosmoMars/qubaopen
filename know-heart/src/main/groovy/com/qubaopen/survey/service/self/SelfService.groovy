package com.qubaopen.survey.service.self

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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

	@Transactional
	void saveQuestionnaireAndUserAnswer(User user, Self self, List<QuestionVo> questionVos, List<SelfQuestion> questions, List<SelfQuestionOption> options, SelfResultOption resultOption) {

		def selfUserQuestionnaire = new SelfUserQuestionnaire(
			user : user,
			self : self,
			selfResultOption : resultOption,
			status : SelfUserQuestionnaire.Status.COMPLETED,
			transmit : SelfUserQuestionnaire.Transmit.NOTRANSMIT,
			time : new Date()
		)
		selfUserQuestionnaire = selfUserQuestionnaireRepository.save(selfUserQuestionnaire)

		def userAnswers = []

		questions.each { q ->
			def type = q.type.SINGLE.toString()
			def answer = null, option = null
			questionVos.find { vo ->
				if (q.id == vo.questionId) {
					if (type == SelfQuestion.Type.SINGLE.toString() && vo.choiceIds.length <= q.optionCount) { // 单选
						options.find { o ->
							if (o.id == vo.choiceIds[0]) {
								option = o
							}
						}
						answer = new SelfUserAnswer(
							user : user,
							selfUserQuestionnaire : selfUserQuestionnaire,
							selfQuestionOption : option,
							score : option.score
						)
						userAnswers << answer
					}
					if (type == SelfQuestion.Type.MULTIPLE.toString() && vo.choiceIds.length <= q.optionCount) { // 多选
						vo.choiceIds.each { cId ->
							options.find { o ->
								if (o.id == cId) {
									option = o
								}
							}
							answer = new SelfUserAnswer(
								user : user,
								selfUserQuestionnaire : selfUserQuestionnaire,
								selfQuestionOption : option,
								score : option.score
							)
							userAnswers << answer
						}
					}
					if (type == SelfQuestion.Type.QA.toString() && !vo.content.empty) { // 问答
						answer = new SelfUserAnswer(
							user : user,
							selfUserQuestionnaire : selfUserQuestionnaire,
							content : vo.content
						)
						userAnswers << answer
					}
					if (type == SelfQuestion.Type.SORT.toString() && vo.orderIds.length <= q.optionCount) { // 排序
						vo.orderIds.eachWithIndex { oId, index ->
							options.find { o ->
								if(o.id == oId) {
									option = o
								}
							}
							answer = new SelfUserAnswer(
								user : user,
								selfUserQuestionnaire : selfUserQuestionnaire,
								selfQuestionOption : option,
								turn : index + 1
							)
							userAnswers << answer
						}
					}
				}
			}
		}
		selfUserAnswerRepository.save(userAnswers)

	}
}
