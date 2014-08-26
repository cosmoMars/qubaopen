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
import com.qubaopen.survey.entity.self.SelfQuestion
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.vo.QuestionVo
import com.qubaopen.survey.repository.interest.InterestUserAnswerRepository
import com.qubaopen.survey.repository.interest.InterestUserQuestionnaireRepository

@Service
public class InterestPersistentService {


	@Autowired
	InterestUserQuestionnaireRepository interestUserQuestionnaireRepository

	@Autowired
	InterestUserAnswerRepository interestUserAnswerRepository

	/**
	 *
	 * 保存用户答卷，用户问题记录
	 * @param user
	 * @param interest
	 * @param questionVos
	 * @param questions
	 * @param options
	 * @param resultOption
	 */
	@Transactional
	void saveQuestionnaireAndUserAnswer(User user, Interest interest, List<QuestionVo> questionVos, List<InterestQuestion> questions, List<InterestQuestionOption> options, InterestResultOption resultOption) {

		def interestUserQuestionnaire = new InterestUserQuestionnaire(
			time : new Date(),
			user : user,
			interest : interest,
			interestResultOption : resultOption,
			status : InterestUserQuestionnaire.Status.COMPLETED,
			transmit : InterestUserQuestionnaire.Transmit.NOTRANSMIT
		)
		interestUserQuestionnaire =	interestUserQuestionnaireRepository.save(interestUserQuestionnaire)

		def userAnswers = []

		questions.each { q ->
			def type = q.type
			def answer = null, option = null
			questionVos.find { vo ->
				if (vo.questionId == q.id) {
					if (type == InterestQuestion.Type.SINGLE) { // 单选
						def choiceId = vo.contents[0].id
						options.find { o ->
							if (o.id == choiceId) {
								option = o
							}
						}
						answer = new InterestUserAnswer(
							user : user,
							interestUserQuestionnaire : interestUserQuestionnaire,
							interestQuestionOption : option,
							interestQuestion : q,
							score : option.score
						)
						userAnswers << answer
					} else if (type == InterestQuestion.Type.MULTIPLE) { // 多选
						vo.contents.each { voc ->
							options.find { o ->
								if (o.id == voc.id) {
									option = o
								}
							}
							answer = new InterestUserAnswer(
								user : user,
								interestUserQuestionnaire : interestUserQuestionnaire,
								interestQuestionOption : option,
								interestQuestion : q,
								score : option.score
							)
							userAnswers << answer
						}
					} else if (type == InterestQuestion.Type.QA) { // 问答题
						vo.contents.each { voc ->
							answer = new InterestUserAnswer(
								user : user,
								interestUserQuestionnaire : interestUserQuestionnaire,
								interestQuestion : q,
								interestQuestionOption : new InterestQuestionOption(id : voc.id),
								content : voc.cnt
							)
							userAnswers << answer
						}
					} else if (type == InterestQuestion.Type.SORT) { // 排序
						vo.contents.each { voc ->
							answer = new InterestUserAnswer(
								user : user,
								interestUserQuestionnaire : interestUserQuestionnaire,
								interestQuestionOption : new InterestQuestionOption(id : voc.id),
								interestQuestion : q,
								turn : voc.order
							)
							userAnswers << answer
						}
					} else if (type == SelfQuestion.Type.SCORE) { // 打分
						vo.contents.each {
							options.find { o ->
								if (o.id == it.id) {
									option = o
								}
							}
							answer = new InterestUserAnswer(
								user : user,
								interestUserQuestionnaire : interestUserQuestionnaire,
								interestQuestionOption : new InterestQuestionOption(id : voc.id),
								interestQuestion : q,
								content : option.content

							)
							userAnswers << answer
						}
					}
				}
			}
		}
		interestUserAnswerRepository.save(userAnswers)
	}
}
