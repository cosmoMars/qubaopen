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
import com.qubaopen.survey.repository.interest.InterestRepository
import com.qubaopen.survey.repository.interest.InterestResultOptionRepository
import com.qubaopen.survey.repository.interest.InterestSpecialInsertRepository
import com.qubaopen.survey.repository.interest.InterestUserAnswerRepository
import com.qubaopen.survey.repository.interest.InterestUserQuestionnaireRepository
import com.qubaopen.survey.repository.user.UserInfoRepository

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
	UserInfoRepository userInfoRepository

	@Autowired
	InterestRepository interestRepository

	@Autowired
	InterestResultService interestResultService

	@Autowired
	ObjectMapper objectMapper


	@Transactional(readOnly = true)
	retrieveInterest(long userId) {
		def user = new User(id : userId)
		def interestList = interestRepository.findUnfinishInterest(user)

		// TODO 计算答问卷的好友数量
//		def friendCount = userFriendRepository.countUserFriend(user)
//		def friend = userFriendRepository.findByUser(user)

		def data = []
		interestList.each {
			def tagNames = []
			it.questionnaireTagTypes.each { q ->
				tagNames << q.name
			}
			def friendCount = interestUserQuestionnaireRepository.countUserFriend(user, it)
			def interest = [
				'interestId' : it.id,
				'interestType' : it.interestType.name,
				'questionnaireTagType' : tagNames,
				'type' : it.type.toString(),
				'title' : it.title,
				'golds' : it.golds,
				'status' : it.status.toString(),
				'remark' : it.remark,
				'totalRespondentsCount' : it.totalRespondentsCount,
				'recommendedValue' : it.recommendedValue,
				'friendCount' : friendCount,
				'guidanceSentence' : it.guidanceSentence
			]
			data << interest
		}
		[
			'success' : '1',
			'message' : '成功',
			'data' : data
		]
	}

	/**
	 * 通过interestId查找问卷
	 * @param interestId
	 * @return
	 */
	@Transactional(readOnly = true)
	findByInterest(long interestId) {

		def interest = new Interest(id : interestId),
			questions = interestQuestionRepository.findByInterest(interest)

		def specialInserts = interestSpecialInsertRepository.findByInterest(interest)

		def questionList = []
		questions.each { q ->
			def options = []
			q.interestQuestionOptions.each { qo ->
				def option = [
				    'optionId' : qo.id,
					'optionNum' : qo.optionNum,
					'optionContent' : qo.content
				]
				options << option
			}

			def question = [
				'questionId' : q.id,
				'questionContent' : q.content,
				'questionType' : q.type,
				'optionCount' : q.optionCount,
				'limitTime' : q.answerTimeLimit,
				'special' : q.special,
				'questionNum' : q.questionNum,
				'options' : options,
				'order' : q.qOrder
			]
			questionList << question
		}

		def inserts = []
		specialInserts.each { si ->
			def options = []
			si.interestQuestion.interestQuestionOptions.each { siq ->
				def option = [
					'optionId' : siq.id,
					'optionNum' : siq.optionNum,
					'optionContent' : siq.content
				]
				options << option
			}

			def insert = [
				'lastQuestionId' : si.interestQuestion.id,
				'lastOptionId' : si.interestQuestionOption.id,
				'nextQuestion' : [
					'questionId' : si.interestQuestion.id,
					'questionContent' : si.interestQuestion.content,
					'questionType' : si.interestQuestion.type,
					'optionCount' : si.interestQuestion.optionCount,
					'limitTime' : si.interestQuestion.answerTimeLimit,
					'special' : si.interestQuestion.special,
					'questionNum' : si.interestQuestion.questionNum,
					'options' : options
				]
			]
			inserts << insert
		}

		[
			'success' : '1',
			'message' : '成功',
			'questions' : questionList,
			'specialInserts' : inserts
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
	calculateInterestResult(long userId, long interestId, String questionJson,String url) {

		def interest = interestRepository.findOne(interestId),
			user = new User(id : userId)

		def javaType = objectMapper.typeFactory.constructParametricType(ArrayList.class, QuestionVo.class),
			questionVos = objectMapper.readValue(questionJson, javaType)

		def questionIds = [], optionIds = []

		questionVos.each {
			questionIds << it.questionId
			it.contents.each { c ->
				optionIds += c.id
			}
		}

//		def score = interestQuestionOptionRepository.sumQuestionOptions(ids as long[])
		def questions = interestQuestionRepository.findAll(questionIds),
			questionOptions = interestQuestionOptionRepository.findAll(optionIds)

		def type = interest.questionnaireType.name,
			result = null

		switch (type) {
			case 'score' :
				result = interestResultService.calculateScore(user, interest, questionOptions, questionVos, questions)
				break
			case 'qtype' :

				break
			case 'rtype' :

				break
			case 'turn' :

				break
			case 'save' :

				break

		}


		if (type == Interest.Type.SORCE) { //积分形式得答案
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

			resultOption

		} else if (type == Interest.Type.DISORDER) { // 乱序

//			saveQuestionnaireAndUserAnswers(user, interest, questionVos, questions, questionOptions, null)

		}
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
					}
					if (type == InterestQuestion.Type.MULTIPLE) { // 多选
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
					}
					if (type == InterestQuestion.Type.QA) { // 问答题
						vo.contents.each { voc ->
							answer = new InterestUserAnswer(
								user : user,
								interestUserQuestionnaire : interestUserQuestionnaire,
								interestQuestion : q,
								interestQuestionOption : new InterestQuestionOption(Id : voc.id),
								content : voc.cnt
							)
							userAnswers << answer
						}
					}
					if (type == InterestQuestion.Type.SORT) { // 排序
						vo.contents.each { voc ->
							answer = new InterestUserAnswer(
								user : user,
								interestUserQuestionnaire : interestUserQuestionnaire,
								interestQuestionOption : new InterestQuestionOption(Id : voc.id),
								interestQuestion : q,
								turn : voc.order
							)
							userAnswers << answer
						}
					}
				}
			}
		}
		interestUserAnswerRepository.save(userAnswers)
	}

	@Transactional
	retrieveFriendAnswer(long friendId, long interestId) {
		def friend = new User(id : friendId),
		interest = new Interest(id : interestId)


		def answerRecord = interestUserQuestionnaireRepository.findByUserAndInterest(friend, interest)

		if (!answerRecord) {
			return '{"success" : "0", "message" : "err没有好友问卷结果"}'
		}

		def friendInfo = userInfoRepository.findOne(friendId)
		[
			'success' : '1',
			'message' : '成功',
			'answer' : [
				'resultNum' : answerRecord.interestResultOption.resultNum,
				'content' : answerRecord.interestResultOption.content,
				'title' : answerRecord.interestResultOption.title
			],
			'friend': [
				'friendId' : friendId,
				'friendName' : friendInfo.name
			]
		]
	}

}
