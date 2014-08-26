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
	SelfResultService selfResultService

	/**
	 * 获取自测问卷
	 * @param userId
	 * @return
	 */
	@Transactional(readOnly = true)
	retrieveSelf() {
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
			specialInserts = selfSpecialInsertRepository.findAllBySelf(self),
			questionOrders = selfQuestionOrderRepository.findBySelf(self)

		def questionResult = [],
			questionNo = 1
		questions.each { q ->

			def resultOptions = [],
				selfQuestionOptions = q.selfQuestionOptions as List
			Collections.sort(selfQuestionOptions, new OptionComparator())
			selfQuestionOptions.each { qo -> // 选项
				resultOptions << [
					'optionId' : qo.id,
					'optionNum' : qo.optionNum,
					'optionContent' : qo.content
				]
			}

			def resultOrder = []
			if (q.special) { // 是特殊题
				specialInserts.each { si ->
					if (q.id == si.specialQuestionId) { // 找到特殊题 取得上一题id 完成order
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
					'options' : resultOptions
				]
			} else if (q.children) { // 判断是矩阵题
				def children = q.children as List
				Collections.sort(children, new QuestionComparator())
				children.each { c->
					def childResult = [],
						childOptions = c.selfQuestionOptions as List
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
			'questions' : questionResult
		]
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
	calculateSelfReslut(long userId, long selfId, String questionJson, boolean refresh) {

		def user = new User(id : userId),
			self = selfRepository.findOne(selfId)

		def javaType = objectMapper.typeFactory.constructParametricType(ArrayList.class, QuestionVo.class),
			questionVos = objectMapper.readValue(questionJson, javaType)

		def questionIds = [], optionIds = []
		questionVos.each { // 获得所有问题id和答案选项id
			questionIds << it.questionId
			it.contents.each { c ->
				optionIds += c.id
			}
		}

		def questions = selfQuestionRepository.findAll(questionIds),
			questionOptions = selfQuestionOptionRepository.findAll(optionIds)

		def typeName = self.questionnaireType.name, // 获得问卷类型名称
			result = null

		switch (typeName) {
			case 'score' : // 得分
				result = selfResultService.calculateScore(user, self, questionOptions, questionVos, questions, refresh)
				break
			case 'qtype' : // 问题种类
				result = selfResultService.calculateQType(user, self, questionOptions, questionVos, questions, refresh)
				break
			case 'otype' : // 问题选项种类
				result = selfResultService.calculateOType(user, self, questionOptions, questionVos, questions, refresh)
				break
			case 'turn' : // 跳转
				result = selfResultService.calculateTurn(user, self, questionOptions, questionVos, questions, refresh)
				break
			case 'save' : // 保存
				result = selfResultService.calculateSave(user, self, questionOptions, questionVos, questions, refresh)
				break
		}

		result

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
