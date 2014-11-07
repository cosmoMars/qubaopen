package com.qubaopen.survey.service.interest

import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.fasterxml.jackson.databind.ObjectMapper
import com.qubaopen.survey.entity.interest.Interest
import com.qubaopen.survey.entity.interest.InterestQuestion;
import com.qubaopen.survey.entity.interest.InterestQuestionOption;
import com.qubaopen.survey.entity.interest.InterestType
import com.qubaopen.survey.entity.interest.InterestUserQuestionnaire;
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
	retrieveInterest2(long userId, Long interestTypeId, Long sortTypeId, List<Long> ids, Pageable pageable) {
		
		def user = new User(id : userId),
			interestList = []
		def filter = [ user : user, interestTypeId : interestTypeId, sortTypeId : sortTypeId, pageable : pageable, ids : ids],
			result = interestRepository.findByFilters(filter) as Map
		interestList = result.get('interests') as List
		def lastPage = false
		if (interestList.size() < pageable.getPageSize()) {
			lastPage = true
		}
		
		// TODO 计算答问卷的好友数量
//		def friendCount = userFriendRepository.countUserFriend(user)
//		def friend = userFriendRepository.findByUser(user)

		def data = []
		interestList.each {
			def tags = []
			it.questionnaireTagTypes.each { q ->
				tags << [
					'tagId' : q.id,
					'tagName' : q.name
				]
			}
			def friendCount = interestUserQuestionnaireRepository.countUserFriend(user, it)
			def interest = [
				'interestId' : it.id,
				'interestType' : it.interestType.name,
				'questionnaireTagType' : tags,
				'title' : it.title,
				'golds' : it.golds,
				'status' : it.status.toString(),
				'remark' : it.remark,
				'totalRespondentsCount' : it.totalRespondentsCount,
				'recommendedValue' : it.recommendedValue,
				'friendCount' : friendCount,
				'picPath' :it.picPath
//				'guidanceSentence' : it.guidanceSentence,
				
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
	
	@Transactional(readOnly = true)
	retrieveInterest(long userId, Long typeId, List<Long> ids, Pageable pageable) {
		
		def user = new User(id : userId),
			interestList = []
		def filter = [ user : user, typeId : typeId, pageable : pageable, ids : ids],
			result = interestRepository.findByFilters(filter) as Map
		interestList = result.get('interests') as List
		def lastPage = false
		if (interestList.size() < pageable.getPageSize()) {
			lastPage = true
		}
		
		// TODO 计算答问卷的好友数量
//		def friendCount = userFriendRepository.countUserFriend(user)
//		def friend = userFriendRepository.findByUser(user)

		def data = []
		interestList.each {
			def tags = []
			it.questionnaireTagTypes.each { q ->
				tags << [
					'tagId' : q.id,
					'tagName' : q.name
				]
			}
			def friendCount = interestUserQuestionnaireRepository.countUserFriend(user, it)
			def interest = [
				'interestId' : it.id,
				'interestType' : it.interestType.name,
				'questionnaireTagType' : tags,
				'title' : it.title,
				'golds' : it.golds,
				'status' : it.status.toString(),
				'remark' : it.remark,
				'totalRespondentsCount' : it.totalRespondentsCount,
				'recommendedValue' : it.recommendedValue,
				'friendCount' : friendCount,
				'picPath' :it.picPath
//				'guidanceSentence' : it.guidanceSentence,
				
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
			questions = interestQuestionRepository.findByInterest(interest),
			specialInserts = interestSpecialInsertRepository.findByInterest(interest),
			questionOrders = interestQuestionOrderRepository.findByInterest(interest)
		
		def questionResult = [],
			questionNo = 1
		questions.each { q ->
			def options = [],
				interestQuestionOptions = q.interestQuestionOptions as List
			Collections.sort(interestQuestionOptions, new OptionComparator())
			interestQuestionOptions.each { qo ->
				options << [
				    'optionId' : qo.id,
					'optionNum' : qo.optionNum,
					'optionContent' : qo.content
				]
			}
			
			def resultOrder = []
			if (q.special) { // 是特殊题
				specialInserts.each { si ->
					if (q.id == si.questionId) { // 找到特殊题 取得上一题id 完成order
						questionOrders.findAll { // 找到所有特殊题答题顺序
							it.questionId == si.questionId
						}.each {
							resultOrder << "${it.questionId}:${it.optionId}:${it.nextQuestionId}"
						}
					}
				}
			}

			specialInserts.findAll { si -> // 直接判断是否是特殊题的上一题，取得特殊题上一题
				q.id == si.questionId
			}.each {
				resultOrder << "${it.questionId}:${it.questionOptionId}:${it.specialQuestionId}"
			}
			
			if (!q.children) { // 判断不是矩阵题
				def order = ''
				if (!resultOrder.isEmpty()) {
					order = resultOrder.join('|')
				} else {
					def oResult = []
					questionOrders.findAll {
						q.id == it.questionId
					}.each {
						oResult << "${it.questionId}:${it.optionId}:${it.nextQuestionId}"
					}
					order = oResult.join('|')
				}
				questionResult << [
					'questionId' : q.id,
					'questionContent' : q.content,
					'questionType' : q.type,
					'optionCount' : q.optionCount,
					'limitTime' : q.answerTimeLimit,
					'special' : q.special,
					'questionNum' : questionNo,
					'matrix' : false,
					'order' : order,
					'options' : options
				]
			} else if (q.children) { // 判断是矩阵题
				def children = q.children as List
				Collections.sort(children, new QuestionComparator())
				children.each { c->
					def childResult = [],
						childOptions = c.interestQuestionOptions as List
					Collections.sort(childOptions, new OptionComparator())
					childOptions.each { qo -> // 选项
						childResult << [
							'optionId' : qo.id,
							'optionNum' : qo.optionNum,
							'optionContent' : qo.content
						]
					}
					def orderResult = []
					questionOrders.findAll { qo->
						c.id == qo.questionId
					}.each {
						orderResult << "${it.questionId}:${it.optionId}:${it.nextQuestionId}"
					}

					questionResult << [
						'questionId' : c.id,
						'questionContent' : c.content,
						'questionType' : c.type,
						'optionCount' : c.optionCount,
						'limitTime' : c.answerTimeLimit,
						'special' : c.special,
						'questionNum' : c.questionNum,
						'matrix' : true,
						'matrixTitle' : q.content,
						'matrixNo' : questionNo,
						'order' : orderResult.join('|'),
						'options' : childResult
					]
				}
			}
			questionNo ++
		}
		[
			'success' : '1',
			'message' : '成功',
			'questions' : questionResult,
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
			return '{"success" : "0", "message" : "err602"}' // 没有好友问卷结果
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

	
	def retrieveInterestHistoryById(Long historyId, Long typeId, User user, Pageable pageable) {
		def questionnaires = []
		
		if (!historyId && !typeId) {
			questionnaires = interestUserQuestionnaireRepository.findAll(
				[
					user_equal : user,
				],
				pageable
			)
		}
		if (historyId && !typeId) {
			questionnaires = interestUserQuestionnaireRepository.findAll(
				[
					user_equal : user,
					'id_lessThan' : historyId
				],
				pageable
			)
		}
		if (!historyId && typeId) {
			questionnaires = interestUserQuestionnaireRepository.findAll(
				[
					user_equal : user,
					'interest.interestType.id_equal' : typeId
				],
				pageable
			)
		}
		
		if (historyId != null && typeId != null) {
			questionnaires = interestUserQuestionnaireRepository.findAll(
				[
					user_equal : user,
					'interest.interestType.id_equal' : typeId,
					'id_lessThan' : historyId
				],
				pageable
			)
		}
		questionnaires
	}
	
	class OptionComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			InterestQuestionOption io1 = (InterestQuestionOption) o1
			InterestQuestionOption io2 = (InterestQuestionOption) o2
			return io1.optionNum.compareTo(io2.optionNum)
		}
	}
	class QuestionComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			InterestQuestion io1 = (InterestQuestion) o1
			InterestQuestion io2 = (InterestQuestion) o2
			return io1.questionNum.compareTo(io2.questionNum)
		}
	}

	
}
