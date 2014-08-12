package com.qubaopen.survey.service.self

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.fasterxml.jackson.databind.ObjectMapper
import com.qubaopen.survey.entity.mindmap.MapStatistics
import com.qubaopen.survey.entity.self.Self
import com.qubaopen.survey.entity.self.SelfQuestion
import com.qubaopen.survey.entity.self.SelfQuestionOption
import com.qubaopen.survey.entity.self.SelfResultOption
import com.qubaopen.survey.entity.self.SelfUserAnswer
import com.qubaopen.survey.entity.self.SelfUserQuestionnaire
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.vo.QuestionVo
import com.qubaopen.survey.repository.mindmap.MapStatisticsRepository
import com.qubaopen.survey.repository.self.SelfQuestionOptionRepository
import com.qubaopen.survey.repository.self.SelfQuestionRepository
import com.qubaopen.survey.repository.self.SelfRepository
import com.qubaopen.survey.repository.self.SelfResultOptionRepository
import com.qubaopen.survey.repository.self.SelfSpecialInsertRepository
import com.qubaopen.survey.repository.self.SelfUserAnswerRepository
import com.qubaopen.survey.repository.self.SelfUserQuestionnaireRepository

@Service
public class SelfService {

	@Autowired
	SelfQuestionRepository selfQuestionRepository

	@Autowired
	SelfQuestionOptionRepository selfQuestionOptionRepository

	@Autowired
	SelfSpecialInsertRepository selfSpecialInsertRepository

	@Autowired
	SelfRepository selfRepository

	@Autowired
	SelfResultOptionRepository selfResultOptionRepository

	@Autowired
	SelfUserQuestionnaireRepository selfUserQuestionnaireRepository

	@Autowired
	SelfUserAnswerRepository selfUserAnswerRepository

	@Autowired
	MapStatisticsRepository mapStatisticsRepository

	@Autowired
	ObjectMapper objectMapper

	/**
	 * 查找自测问卷
	 * @param selfId
	 * @return
	 */
	@Transactional
	findBySelf(long selfId) {
		def self = new Self(id : selfId)

		def questions = selfQuestionRepository.findAllBySelf(self),
			questionOrders = []

		if (questions) {
			questionOrders = selfQuestionOrderRepository.findAllBySelfQuestions(questions)
		}

		def specialInserts = selfSpecialInsertRepository.findAllBySelf(self)

		def result = [
			'questions'	: questions,
		'questionOrders' : questionOrders,
		'specialInserts' : specialInserts
		]
		return result
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

		def selfType = self.selfType

		if (selfType.name == 'SDS') { // SDS测试

			def optionMap = [:]
			questionOptions.each {
				def questionType = it.selfQuestion.selfQuestionType.name

				if (optionMap.get(questionType)) { // key: 种类 : I A C, value: 题目
					optionMap.get(questionType) << it
				} else {
					def optionList = []
					optionList << it
					optionMap.put(questionType, optionList)
				}
			}

			def resultMap = [:]

			optionMap.each { k, v -> // 计算每一个类型的分数
				def score = 0
				v.each {
					score = score + it.score
				}
				optionMap.get(k).clear()
				optionMap.put(k, score)
				if (resultMap.get(score)) { // key: 种类, value: 分数
					resultMap.get(score) << k
				} else {
					def typeNameList = []
					typeNameList << k
					resultMap.put(score, typeNameList)
				}
			}
			if (resultMap.empty || resultMap.size() == 0) {
				return '{"success": 0, "error": "没有结果"}'
			}

			def resultName = (resultMap.sort().values().sum() as List).reverse()

			if (resultName.size() < 3) {
				return '{"success": 0, "error": "没有结果"}'
			}

			def result = selfResultOptionRepository.findByTypeAlphabet(resultName[0] + '%', '%' + resultName[1] + '%', '%' + resultName[2] + '%')

			if (refresh) {
				saveMapStatistics(user, self, objectMapper.writeValueAsString(optionMap), result[0], 0) // 保存心理地图

				saveQuestionnaireAndUserAnswer(user, self, questionVos, questions, questionOptions, result[0])
			}

			[
				'success' : '1',
				'message' : '成功',
				'id' : result[0].id ?: '',
				'resultTitle' : result[0]?.selfResult?.title ?: '',
				'content' : result[0].content ?: '',
				'optionTitle' : result[0].title ?: '',
				'resultNum' : result[0].resultNum ?: ''
			]

		} else if (selfType.name == 'PDP') { // PDP测试

			def optionMap = [:]
			questionOptions.each {
				def selfResultOption = it.selfResultOption

				if (optionMap.get(selfResultOption)) { // key: 种类, value: 题目
					optionMap.get(selfResultOption) << it
				} else {
					def optionList = []
					optionList << it
					optionMap.put(selfResultOption, optionList)
				}
			}


			def resultMap = [:]

			optionMap.each { k, v -> // 计算每一个类型的分数
				def score = 0
				v.each {
					score += it.score
				}
				if (resultMap.get(score)) { // key: 分数, value: 种类
					resultMap.get(score) << k
				} else {
					def typeList = []
					typeList << k
					resultMap.put(score, typeList)
				}
			}
			def result = resultMap.get(resultMap.keySet().max()) as List

			if (refresh) {
				saveMapStatistics(user, self, null, result[0], 0) // 保存心理地图

				saveQuestionnaireAndUserAnswer(user, self, questionVos, questions, questionOptions, result[0])
			}

			[
				'success' : '1',
				'message' : '成功',
				'id' : result[0].id ?: '',
				'resultTitle' : result[0]?.selfResult?.title ?: '',
				'content' : result[0].content ?: '',
				'optionTitle' : result[0].title ?: '',
				'resultNum' : result[0].resultNum ?: ''
			]


		} else if (selfType.name == 'AB' || selfType.name == 'C' || selfType.name == 'D') { // AB,C,D 测试

//			def score = selfResultOptionRepository.sumSelfOptions(optionIds)

			def score = 0

			questionOptions.each {
				 score += it.score
			}

			if (selfType.name == 'AB') {
				score = score * 3
			}
			def result = selfResultOptionRepository.findOneByFilters(
				'selfResult.self' : self,
				highestScore_greaterThanOrEqualTo : score,
				lowestScore_lessThanOrEqualTo : score
			)

			if (refresh) {
				saveMapStatistics(user, selfId, null, result, score)

				saveQuestionnaireAndUserAnswer(user, self, questionVos, questions, questionOptions, result)
			}

			[
				'success' : '1',
				'message' : '成功',
				'id' : result.id ?: '',
				'resultTitle' : result?.selfResult?.title ?: '',
				'content' : result.content ?: '',
				'optionTitle' : result.title ?: '',
				'resultNum' : result.resultNum ?: ''
			]
		} else if (selfType.name == 'MBTI') {
			def optionMap = [:]
			questionOptions.each {
				def questionType = it.selfQuestion.selfQuestionType.name

				if (optionMap.get(questionType)) {
					optionMap.get(questionType) << it
				} else {
					def list = []
					list << it
					optionMap.put(questionType, list)
				}
			}

			optionMap.each { k, v ->
				def score = 0
				v.each {
					score += it.score
				}
				optionMap.get(k).clear()
				optionMap.put(k, score)
			}
			def resultName = ''
			if (optionMap.get('E') >= optionMap.get('I')) {
				resultName += 'E'
			} else {
				resultName += 'I'
			}
			if (optionMap.get('N') >= optionMap.get('S')) {
				resultName += 'N'
			} else {
				resultName += 'S'
			}
			if (optionMap.get('F') >= optionMap.get('T')) {
				resultName += 'F'
			} else {
				resultName += 'T'
			}
			if (optionMap.get('P') >= optionMap.get('J')) {
				resultName += 'P'
			} else {
				resultName += 'J'
			}
			def result = selfResultOptionRepository.findByName(resultName)

			if (refresh) {
				saveMapStatistics(user, selfId, objectMapper.writeValueAsString(optionMap), result, 0)

				saveQuestionnaireAndUserAnswer(user, self, questionVos, questions, questionOptions, result)
			}
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

	}


	/**
	 * 保存用户答卷信息，用户答题内容
	 * @param user
	 * @param self
	 * @param questionVos
	 * @param questions
	 * @param options
	 * @param resultOption
	 */
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
							selfQuestion : q,
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
								selfQuestion : q,
								score : option.score
							)
							userAnswers << answer
						}
					}
					if (type == SelfQuestion.Type.QA.toString() && !vo.content.empty) { // 问答
						answer = new SelfUserAnswer(
							user : user,
							selfUserQuestionnaire : selfUserQuestionnaire,
							selfQuestion : q,
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
								selfQuestion : q,
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

	/**
	 * 保存心理地图资料
	 * @param user
	 * @param id
	 * @param result
	 */
	@Transactional
	void saveMapStatistics(User user, Self self, String result, SelfResultOption selfResultOption, int score) {

		def selfType = self.selfType.name
		def type = null
		switch (selfType) {
			case "SDS" :
				type = MapStatistics.Type.SDS
				break
			case "AB" :
				type = MapStatistics.Type.ABCD
				break
			case "C" :
				type = MapStatistics.Type.ABCD
				break
			case "D" :
				type = MapStatistics.Type.ABCD
				break
			case "PDP" :
				type = MapStatistics.Type.PDP
				break
			case "MBTI" :
				type = MapStatistics.Type.MBTI
				break
		}

		def mapStatistics = mapStatisticsRepository.findByUserAndSelf(user, self)
		if (mapStatistics) {
			mapStatistics.result = result
			mapStatistics.selfResultOption = selfResultOption
			mapStatistics.score = score
		} else {
			mapStatistics = new MapStatistics(
				user : user,
				self : self,
				type : type,
				result : result,
				selfResultOption : selfResultOption,
				score : score
			)
		}
		mapStatisticsRepository.save(mapStatistics)
	}
}
