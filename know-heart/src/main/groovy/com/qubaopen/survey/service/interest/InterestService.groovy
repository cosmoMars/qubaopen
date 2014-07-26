package com.qubaopen.survey.service.interest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.qubaopen.survey.entity.interest.Interest
import com.qubaopen.survey.entity.interest.InterestQuestion
import com.qubaopen.survey.entity.interest.InterestQuestionOption
import com.qubaopen.survey.entity.interest.InterestResultOption
import com.qubaopen.survey.entity.interest.InterestUserAnswer
import com.qubaopen.survey.entity.interest.InterestUserQuestionnaire
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.vo.QuestionVo
import com.qubaopen.survey.repository.interest.InterestSpecialInsertRepository
import com.qubaopen.survey.repository.interest.InterestUserAnswerRepository
import com.qubaopen.survey.repository.interest.InterestUserQuestionnaireRepository

@Service
public class InterestService {

	@Autowired
	InterestSpecialInsertRepository interestSpecialInsertRepository

	@Autowired
	InterestUserQuestionnaireRepository interestUserQuestionnaireRepository

	@Autowired
	InterestUserAnswerRepository interestUserAnswerRepository

	@Transactional
	void saveQuestionnaireAndUserAnswers(User user, Interest interest, List<QuestionVo> questionVos, List<InterestQuestionOption> options, InterestResultOption resultOption) {

		def interestUserQuestionnaire = new InterestUserQuestionnaire(
			user : user,
			interest : interest,
			interestResultOption : resultOption,
			status : InterestUserQuestionnaire.Status.COMPLETED,
			transmit : InterestUserQuestionnaire.Transmit.NOTRANSMIT,
			time : new Date()
		)
		interestUserQuestionnaire =	interestUserQuestionnaireRepository.save(interestUserQuestionnaire)

		def userAnswers = []

		questionVos.each { vo ->
			def question = new InterestQuestion(id : vo.questionId)
			if (vo.choiceIds && vo.choiceIds.length > 0) { // 单选或多选
				vo.choiceIds.find { cId ->
					def option = null
					options.find { o ->
						if (o.id == cId) {
							option = o
						}
					}
					def answer = new InterestUserAnswer(
						user : user,
						interestUserQuestionnaire : interestUserQuestionnaire,
						interestQuestionOption : option,
						interestQuestion : question,
						score : option.score
					)
					userAnswers << answer
				}
			} else if (vo.content && !vo.content.isEmpty) { //问答题
				def answer = new InterestUserAnswer(
					user : user,
					interestUserQuestionnaire : interestUserQuestionnaire,
					interestQuestion : question,
					content : vo.content
				)
				userAnswers << answer
			} else if (vo.orderIds && vo.orderIds.length > 0) {
				vo.orderIds.eachWithIndex { oId, index ->
					def option = new InterestQuestionOption(id : oId),
						answer = new InterestUserAnswer(
							user : user,
							interestUserQuestionnaire : interestUserQuestionnaire,
							interestQuestion : question,
							interestQuestionOption : option,
							turn : index + 1
						)
					userAnswers << answer
				}
			}
		}
		interestUserAnswerRepository.save(userAnswers)
	}

}
