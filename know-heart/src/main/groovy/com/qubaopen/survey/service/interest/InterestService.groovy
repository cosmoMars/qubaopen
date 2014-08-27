package com.qubaopen.survey.service.interest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.fasterxml.jackson.databind.ObjectMapper
import com.qubaopen.survey.entity.interest.Interest
import com.qubaopen.survey.entity.interest.InterestType
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
	retrieveInterest(long userId, Long interestTypeId, Long sortTypeId, List<Long> ids, Pageable pageable) {
		
		def user = new User(id : userId),
			interestList = []
		def filter = [ user : user, interestTypeId : interestTypeId, sortTypeId : sortTypeId, pageable : pageable, ids : ids],
			result = interestRepository.findByFilters(filter) as Map
		interestList = result.get('interests') as List
		def lastPage = false
		if (interestList.size() < pageable.getPageSize()) {
			lastPage = true
		}
		
//		if ('Time' == turnType) {
//			interestList = interestRepository.getInterestByTime(user, pageable)
//		}
//		
//		if (interestTypeId && !turnType) {
//			interestList = interestRepository.findByInterestType(user, new InterestType(id : interestTypeId))
//		}
//		if (turnType) {
//			interestList = interestRepository.getInterestByTurnType(user, turnType)
//		}
		
		/*else {
			def filters = [interestTypeId : interestTypeId, turnType : turnType, user : user]
			interestList = interestRepository.findByFilters(filters)
		}*/
		
		/*if (interestTypeId) {
			def result = interestRepository.findAll(
				['interestType_equal' : new InterestType(id : interestTypeId)],
				pageable
			)
		}*/
		
		
		
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
			'lastPage' : lastPage,
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
	calculateInterestResult(long userId, long interestId, String questionJson) {

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
				result = interestResultService.calculateQType(user, interest, questionOptions, questionVos, questions)
				break
			case 'otype' :
				result = interestResultService.calculateOType(user, interest, questionOptions, questionVos, questions)
				break
			case 'turn' :
				result = interestResultService.calculateTurn(user, interest, questionOptions, questionVos, questions)
				break
			case 'save' :
				result = interestResultService.calculateSave(user, interest, questionOptions, questionVos, questions)
				break

		}

		result

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
