package com.qubaopen.survey.service.self

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.fasterxml.jackson.databind.ObjectMapper
import com.qubaopen.survey.entity.self.Self
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
	@Transactional
	retrieveSelf(long userId) {
		def user = new User(id : userId),
			selfList = selfRepository.findAll(),
			data = []
		selfList.each {
			def self = [
				'selfId' : it.id,
				'managementType' : it.managementType.toString(),
				'title' : it.title,
			]
			data << self
		}
		[
			'success' : '1',
			'message' : '成功',
			'data' : data
		]
	}

	/**
	 * 查找自测问卷问题
	 * @param selfId
	 * @return
	 */
	@Transactional
	findBySelf(long selfId) {
		def self = new Self(id : selfId)

		def questions = selfQuestionRepository.findAllBySelf(self)
//			,questionOrders = []

//		if (questions) {
//			questionOrders = selfQuestionOrderRepository.findAllBySelfQuestions(questions)
//		}
//		def questionIds = long[]
//		questions.each {
//			questionIds << it.id
//		}

//		def questionOrders = selfQuestionOrderRepository.findAllBySelfQuestions(questionIds)
		def specialInserts = selfSpecialInsertRepository.findAllBySelf(self)

		def questionList = []
		questions.each { q ->
			def options = []
			q.selfQuestionOptions.each { qo ->
				def option = [
					'optionId' : qo.id,
					'optionNum' : qo.optionNum,
					'optionContent' : qo.content
				]
				options << option
			}
			def orders = selfQuestionOrderRepository.findByQuestionId(q.id)

			def  order = ''
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

			def question = [
				'questionId' : q.id,
				'questionContent' : q.content,
				'questionType' : q.type,
				'optionCount' : q.optionCount,
				'limitTime' : q.answerTimeLimit,
				'special' : q.special,
				'questionNum' : q.questionNum,
				'options' : options,
				'order' : order
			]
			questionList << question
		}

		def inserts = []
		specialInserts.each { si ->
			def options = []
			si.selfQuestion.selfQuestionOptions.each { ssq ->
				def option = [
					'optionId' : ssq.id,
					'optionNum' : ssq.optionNum,
					'optionContent' : ssq.content
				]
				options << option
			}

			def insert = [
				'lastQuestionId' : si.selfQuestion?.id,
				'lastOptionId' : si.selfQuestionOption?.id ,
				'nextQuestion' : [
					'questionId' : si.selfQuestion?.id,
					'questionContent' : si.selfQuestion?.content,
					'questionType' : si.selfQuestion?.type,
					'optionCount' : si.selfQuestion?.optionCount,
					'limitTime' : si.selfQuestion?.answerTimeLimit,
					'special' : si.selfQuestion?.special,
					'questionNum' : si.selfQuestion?.questionNum,
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
		questionVos.each {
			questionIds << it.questionId
			optionIds += it.choiceIds as List
		}

		def questions = selfQuestionRepository.findAll(questionIds)
		def questionOptions = selfQuestionOptionRepository.findAll(optionIds)

		def type = self.type

		if (type == Self.Type.SORCE) {
			def selfType = self.selfType,
				result = null
			switch (selfType.name) {
				case 'SDS' :
					result = resultService.calculateSDS(user, self, questionOptions, questionVos, questions, refresh)
					break
				case 'PDP' :
					result = resultService.calculatePDP(user, self, questionOptions, questionVos, questions, refresh)
					break
				case 'AB' :
					result = resultService.calculateABCD(user, self, questionOptions, questionVos, questions, refresh)
					break
				case 'C' :
					result = resultService.calculateABCD(user, self, questionOptions, questionVos, questions, refresh)
					break
				case 'D' :
					result = resultService.calculateABCD(user, self, questionOptions, questionVos, questions, refresh)
					break
				case 'MBTI' :
					result = resultService.calculateMBTI(user, self, questionOptions, questionVos, questions, refresh)
					break
				[
					'success' : '1',
					'message' : '成功',
					'id' : result.id ?: '',
					'resultTitle' : result?.selfResult?.title ?: '',
					'content' : result.content ?: '',
					'optionTitle' : result.title ?: '',
					'resultNum' : result.resultNum ?: ''
				]
			}
		} else if (type == Self.Type.DISOREDER) {

		}
	}

}
