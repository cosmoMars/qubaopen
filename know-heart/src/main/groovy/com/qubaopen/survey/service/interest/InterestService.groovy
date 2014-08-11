package com.qubaopen.survey.service.interest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.fasterxml.jackson.databind.ObjectMapper
import com.qubaopen.survey.entity.interest.Interest
import com.qubaopen.survey.entity.interest.InterestQuestion
import com.qubaopen.survey.entity.interest.InterestQuestionOption
import com.qubaopen.survey.entity.interest.InterestResultOption
import com.qubaopen.survey.entity.interest.InterestUserAnswer
import com.qubaopen.survey.entity.interest.InterestUserQuestionnaire
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.vo.QuestionVo
import com.qubaopen.survey.repository.interest.InterestQuestionOptionRepository
import com.qubaopen.survey.repository.interest.InterestQuestionOrderRepository
import com.qubaopen.survey.repository.interest.InterestQuestionRepository
import com.qubaopen.survey.repository.interest.InterestResultOptionRepository
import com.qubaopen.survey.repository.interest.InterestSpecialInsertRepository
import com.qubaopen.survey.repository.interest.InterestUserAnswerRepository
import com.qubaopen.survey.repository.interest.InterestUserQuestionnaireRepository

@Service
public class InterestService {

	@Autowired
	InterestUserQuestionnaireRepository interestUserQuestionnaireRepository

	@Autowired
	InterestQuestionRepository interestQuestionRepository

	@Autowired
	InterestUserAnswerRepository interestUserAnswerRepository

	@Autowired
	InterestResultOptionRepository interestResultOptionRepository

	@Autowired
	InterestQuestionOrderRepository interestQuestionOrderRepository

	@Autowired
	InterestSpecialInsertRepository interestSpecialInsertRepository

	@Autowired
	InterestQuestionOptionRepository interestQuestionOptionRepository

	@Autowired
	ObjectMapper objectMapper

	/**
	 * 通过interestId查找问卷
	 * @param interestId
	 * @return
	 */
	@Transactional(readOnly = true)
	findByInterest(long interestId) {

		def interest = new Interest(id : interestId),
			questions = interestQuestionRepository.findByInterest(interest)

//			questionOrders = []
//		if (questions) {
//			questionOrders = interestQuestionOrderRepository.findByInterestQuestion(questions)
//		}
//
//		def specialInserts = interestSpecialInsertRepository.findByInterest(interest)

		[
			'questions' : questions
//			'questionOrders' : questionOrders,
//			'specialInserts' : specialInserts
		]


	}

	/**
	 * 计算兴趣问卷结果
	 * @param userId
	 * @param interestId
	 * @param questionJson
	 * @return
	 */
	@Transactional
	calculateInterestResult(long userId, long interestId, String questionJson) {

		def interest = new Interest(id : interestId),
			user = new User(id : userId)

		def javaType = objectMapper.typeFactory.constructParametricType(ArrayList.class, QuestionVo.class)
		def questionVos = objectMapper.readValue(questionJson, javaType)

		def questionIds = [], optionIds = []

		questionVos.each {
			questionIds << it.questionId
			optionIds += it.choiceIds as List
		}

//		def score = interestQuestionOptionRepository.sumQuestionOptions(ids as long[])
		def questions = interestQuestionRepository.findAll(questionIds),
			questionOptions = interestQuestionOptionRepository.findAll(optionIds)

		def score = 0
		questionOptions.each {
			score = score + it.score
		}

		def resultOption = interestResultOptionRepository.findOneByFilters(
			[
				'interestResult.interest_equal' : interest,
				'lowestScore_lessThanOrEqualTo' : score,
				'highestScore_greaterThanOrEqualTo' : score
			]
		)

		// 保存用户问卷答卷，以及用户问卷问题
		saveQuestionnaireAndUserAnswers(user, interest, questionVos, questions, questionOptions, resultOption)

		[
			'success' : '1',
			'message' : '成功',
			'id' : resultOption.id ?: '',
			'resultTitle' : resultOption?.interestResult?.title ?: '',
			'content' : resultOption.content ?: '',
			'optionTitle' : resultOption.title ?: '',
			'resultNum' : resultOption.resultNum ?: ''
		]
	}


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
	void saveQuestionnaireAndUserAnswers(User user, Interest interest, List<QuestionVo> questionVos, List<InterestQuestion> questions, List<InterestQuestionOption> options, InterestResultOption resultOption) {

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

		questions.each { q ->
			def type = q.type.toString()
			def answer = null, option = null
			questionVos.find { vo ->
				if (vo.questionId == q.id) {
					if (type == InterestQuestion.Type.SINGLE.toString() && vo.choiceIds.length <= q.optionCount) { // 单选
						options.find { o ->
							if (o.id == vo.choiceIds[0]) {
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
					if (type == InterestQuestion.Type.MULTIPLE.toString() && vo.choiceIds.length <= q.optionCount) { // 多选
						vo.choiceIds.each { cId ->
							options.find { o ->
								if (o.id == cId) {
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
					}
					if (type == InterestQuestion.Type.QA.toString() && !vo.content.empty) { // 问答题
							answer = new InterestUserAnswer(
								user : user,
								interestUserQuestionnaire : interestUserQuestionnaire,
								interestQuestion : q,
								content : vo.content
							)
							userAnswers << answer
					}
					if (type == InterestQuestion.Type.SORT.toString() && vo.orderIds.length <= q.optionCount) { // 排序
						vo.orderIds.eachWithIndex { oId, index ->
							options.find { o ->
								if (oId == o.id) {
									option = o
								}
							}
							answer = new InterestUserAnswer(
								user : user,
								interestUserQuestionnaire : interestUserQuestionnaire,
								interestQuestionOption : option,
								interestQuestion : q,
								turn : index + 1
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
