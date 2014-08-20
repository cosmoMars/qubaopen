package com.qubaopen.survey.service.self

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.fasterxml.jackson.databind.ObjectMapper
import com.qubaopen.survey.entity.self.Self
import com.qubaopen.survey.entity.self.SelfQuestion
import com.qubaopen.survey.entity.self.SelfQuestionOption
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.vo.QuestionVo
import com.qubaopen.survey.repository.self.SelfQuestionOptionRepository
import com.qubaopen.survey.repository.self.SelfQuestionOrderRepository
import com.qubaopen.survey.repository.self.SelfQuestionRepository
import com.qubaopen.survey.repository.self.SelfRepository
import com.qubaopen.survey.repository.self.SelfSpecialInsertRepository

@Service
public class SelfService {

	@Autowired
	SelfQuestionRepository selfQuestionRepository

	@Autowired
	SelfSpecialInsertRepository selfSpecialInsertRepository

	@Autowired
	SelfQuestionOptionRepository selfQuestionOptionRepository

	@Autowired
	SelfQuestionOrderRepository selfQuestionOrderRepository

	@Autowired
	SelfRepository selfRepository

	@Autowired
	ObjectMapper objectMapper

	@Autowired
	ResultService resultService

	/**
	 * 获取自测问卷
	 * @param userId
	 * @return
	 */
	@Transactional(readOnly = true)
	retrieveSelf(long userId) {
		def user = new User(id : userId)

		selfRepository.findSelfWithoutManagement()

	}

	/**
	 * 查找自测问卷问题
	 * @param selfId
	 * @return
	 */
	@Transactional(readOnly = true)
	findBySelf(long selfId) {
		def self = new Self(id : selfId)

		def questions = selfQuestionRepository.findBySelf(self),
			specialInserts = selfSpecialInsertRepository.findAllBySelf(self)

		def questionResult = []
		def questionNo = 1
		questions.each { q ->

			def options = [],
				selfQuestionOptions = q.selfQuestionOptions as List
			Collections.sort(selfQuestionOptions, new OptionComparator())
			selfQuestionOptions.each { qo -> // 选项
				options << [
					'optionId' : qo.id,
					'optionNum' : qo.optionNum,
					'optionContent' : qo.content
				]
			}
			def sOrder = ''
			if (q.special) { // 是特殊题
				def specials = specialInserts.findAll { si ->
					q.id == si.specialQuestionId
				}

				def ids = []
				specials.each {
					ids << it.questionId
				}
				sOrder = getOrder(q.self, ids as long[])
			}

			def inserts = specialInserts.findAll { si -> // 取得特殊题上一题
				q.id == si.questionId
			}
			if (inserts) {
				if (inserts.size() > 1) {
					for (i in 0..inserts.size() - 1) {
						sOrder = sOrder + "${inserts[i].questionId}:${inserts[i].questionOptionId}:${inserts[i].specialQuestionId}" as String
						if (i < sOrder.size() - 1) {
							sOrder += '|'
						}
					}
				} else if (inserts.size() == 1){
					sOrder = "${inserts[0].questionId}:${inserts[0].questionOptionId}:${inserts[0].specialQuestionId}" as String
				}
			}

			if (!q.children) {
				def order = ''
				if (inserts) {
					order = sOrder
				} else {
					order = getOrder(q.self, q.id)
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
			} else if (q.children) {
				def children = q.children as List
				Collections.sort(children, new QuestionComparator())
				children.each {
					def childOption = [],
						childOptions = it.selfQuestionOptions as List
					Collections.sort(childOptions, new OptionComparator())
					childOptions.each { qo -> // 选项
						childOption << [
							'optionId' : qo.id,
							'optionNum' : qo.optionNum,
							'optionContent' : qo.content
						]
					}
					questionResult << [
						'questionId' : it.id,
						'questionContent' : it.content,
						'questionType' : it.type,
						'optionCount' : it.optionCount,
						'limitTime' : it.answerTimeLimit,
						'special' : it.special,
						'questionNum' : it.questionNum,
						'matrix' : true,
						'matrixTitle' : q.content,
						'matrixNo' : questionNo,
						'order' : getOrder(q.self, it.id),
						'options' : childOption
					]
				}
			}
			questionNo ++
		}

		[
			'success' : '1',
			'message' : '成功',
			'questions' : questionResult
		]
	}


	String getOrder(Self self, long id) {
		def orders = selfQuestionOrderRepository.findByQuestionIdAndSelf(id, self),
		order = ''
		if (orders.size() > 1) {
			for (i in 0..orders.size() - 1) {
				order = order + "${orders[i].questionId}:${orders[i].optionId}:${orders[i].nextQuestionId}" as String
				if (i < orders.size() - 1) {
					order += '|'
				}
			}
		} else if (orders.size() == 1){
			order = "${orders[0].questionId}:${orders[0].optionId}:${orders[0].nextQuestionId}" as String
		}

		order
	}

	String getOrder(Self self, long[] ids) {
		def orders = selfQuestionOrderRepository.findByQuestionIds(self, ids),
		order = ''
		if (orders.size() > 1) {
			for (i in 0..orders.size() - 1) {
				order = order + "${orders[i].questionId}:${orders[i].optionId}:${orders[i].nextQuestionId}" as String
				if (i < orders.size() - 1) {
					order += '|'
				}
			}
		} else if (orders.size() == 1){
			order = "${orders[0].questionId}:${orders[0].optionId}:${orders[0].nextQuestionId}" as String
		}

		order
	}
	/**
	 * 计算保存问卷信息
	 * @param userId
	 * @param selfId
	 * @param questionJson
	 * @param refresh
	 * @return
	 */
	@Transactional
	calculateSelfReslut(long userId, long selfId, String questionJson, Boolean refresh) {

		def user = new User(id : userId),
			self = selfRepository.findOne(selfId)

		def javaType = objectMapper.typeFactory.constructParametricType(ArrayList.class, QuestionVo.class),
			questionVos = objectMapper.readValue(questionJson, javaType)

		def questionIds = [], optionIds = []
		questionVos.each {
			questionIds << it.questionId
//			optionIds += it.choiceIds as List

			it.content.each { c ->

				if (c.contains(':')) {
					def strC = c.split(':')
					if (strC[0].matches('^[0-9]*$')) {
						optionIds += Long.valueOf(strC[0])
					}
				}

				if (c.matches('^[0-9]*$')) {
					optionIds += Long.valueOf(c)
				}
			}

		}

		def questions = selfQuestionRepository.findAll(questionIds),
			questionOptions = selfQuestionOptionRepository.findAll(optionIds),
			type = self.type

		if (type == Self.Type.SORCE) {
			def selfType = self.selfType,
				result = null

			switch (selfType.name) {
				case 'SCORE' :
					result = resultService.calculateScore(user, self, questionOptions, questionVos, questions, refresh)
					break
				case 'QTYPE' :
					result = resultService.calculateQType(user, self, questionOptions, questionVos, questions, refresh)
					break
				case 'RTYPE' :
					result = resultService.calculateRType(user, self, questionOptions, questionVos, questions, refresh)
					break
			}

			if (!result) {
				return '{"success" : "0", "message" : "err123123"}'
			}
			[
				'success' : '1',
				'message' : '成功',
				'id' : result?.id,
				'resultTitle' : result?.selfResult?.title,
				'content' : result?.content,
				'optionTitle' : result?.title,
				'resultNum' : result?.resultNum
			]
		} else if (type == Self.Type.DISOREDER) {

		}
	}

	class OptionComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			SelfQuestionOption so1 = (SelfQuestionOption) o1
			SelfQuestionOption so2 = (SelfQuestionOption) o2
			return so1.optionNum.compareTo(so2.optionNum)
		}
	}
	class QuestionComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			SelfQuestion so1 = (SelfQuestion) o1
			SelfQuestion so2 = (SelfQuestion) o2
			return so1.questionNum.compareTo(so2.questionNum)
		}
	}

}
