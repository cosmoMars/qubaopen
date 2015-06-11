package com.knowheart3.service.self

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.fasterxml.jackson.databind.ObjectMapper
import com.knowheart3.repository.EPQBasicRepository
import com.knowheart3.repository.self.SelfResultOptionRepository
import com.qubaopen.survey.entity.mindmap.MapRecord
import com.qubaopen.survey.entity.self.Self
import com.qubaopen.survey.entity.self.SelfQuestion
import com.qubaopen.survey.entity.self.SelfQuestionOption
import com.qubaopen.survey.entity.self.SelfResult
import com.qubaopen.survey.entity.self.SelfResultOption
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.vo.QuestionVo

@Service
public class SelfResultService {

	@Autowired
	ObjectMapper objectMapper

	@Autowired
	SelfPersistentService selfPersistentService

	@Autowired
	SelfResultOptionRepository selfResultOptionRepository
	
	@Autowired
	EPQBasicRepository epqBasicRepository

	/**
	 * 计算SCORE
	 * @param user
	 * @param self
	 * @param questionOptions
	 * @param questionIds
	 * @param questions
	 * @param refresh
	 * @return
	 */
	@Transactional
	calculateScore(User user, Self self, List<SelfQuestionOption> questionOptions, List<QuestionVo> questionVos, List<SelfQuestion> questions, boolean refresh) {
		def score = 0

		questionOptions.each {
			 score += it.score
		}

		score = score * self.coefficient
		
		if ('SAS' == self.abbreviation) {
			score = score * 1.25 as int
		}
		def resultOption
		if ('Delay' == self.abbreviation) {
			resultOption = selfResultOptionRepository.findOne(208l)
		} else {
			resultOption = selfResultOptionRepository.findOneByFilters(
				'selfResult.self_equal' : self,
				'highestScore_greaterThanOrEqualTo' : score,
				'lowestScore_lessThanOrEqualTo' : score
			)
		}

		if (resultOption) {
			def selfUserQuestionnaire = selfPersistentService.saveQuestionnaireAndUserAnswer(user, self, questionVos, questions, questionOptions, resultOption, refresh, score)
			if (refresh) {
				def mapRecord = new MapRecord(
						name : self.abbreviation,
						value : score
				)
				def result = []
				result << mapRecord
				selfPersistentService.saveMapStatistics(user, selfUserQuestionnaire, result, resultOption, score)
			}
		}
		resultOption
	}

	@Transactional
	calculateQType(User user, Self self, List<SelfQuestionOption> questionOptions, List<QuestionVo> questionVos, List<SelfQuestion> questions, boolean refresh) {

		def abbreviation = self.abbreviation
		def result = null
		switch (abbreviation) {
			case 'SDS' :
				result = calculateSDS(user, self, questionOptions, questionVos, questions, refresh)
				break
			case 'PDP' :
				result = calculatePDP(user, self, questionOptions, questionVos, questions, refresh)
				break
			case 'E' :
				result = calculateEPQ(user, self, questionOptions, questionVos, questions, refresh)
				break
			case 'P' :
				result = calculateEPQ(user, self, questionOptions, questionVos, questions, refresh)
				break
			case 'N' :
				result = calculateEPQ(user, self, questionOptions, questionVos, questions, refresh)
				break
			case 'PANA' :
				result = calculatePANA(user, self, questionOptions, questionVos, questions, refresh)
				break
			case 'ADULT' :
				result = calculateAdult(user, self, questionOptions, questionVos, questions, refresh)
				break
			case 'HopeSense' :
				result = calculateHopeSense(user, self, questionOptions, questionVos, questions, refresh)
				break
			case 'AMS' :
				result = calculateAMS(user, self, questionOptions, questionVos, questions, refresh)
				break
			case 'Bass' :
				result = calculateBass(user, self, questionOptions, questionVos, questions, refresh)
				break
			case 'ECR' :
				result = calculateECR(user, self, questionOptions, questionVos, questions, refresh)
				break
			case 'RPUS' :
				result = calculateRPUS(user, self, questionOptions, questionVos, questions, refresh)
				break
			case 'PSDQ' :
				result = calculatePSDQ(user, self, questionOptions, questionVos, questions, refresh)
				break
		}
		result
	}

	@Transactional
	calculateOType(User user, Self self, List<SelfQuestionOption> questionOptions, List<QuestionVo> questionVos, List<SelfQuestion> questions, boolean refresh) {
		def abbreviation = self.abbreviation
		def result = null
		switch (abbreviation) {
			case 'MBTI' :
				result = calculateMBTI(user, self, questionOptions, questionVos, questions, refresh)
				break
			case 'DISC' :
				result = calculateDISC(user, self, questionOptions, questionVos, questions, refresh)
				break
			case 'SCL90' :
				result = calculateSCL90(user, self, questionOptions, questionVos, questions, refresh)
				break
				
		}
		result
	}
	@Transactional
	calculateTurn(User user, Self self, List<SelfQuestionOption> questionOptions, List<QuestionVo> questionVos, List<SelfQuestion> questions, boolean refresh) {
	
	}
	
	
	@Transactional
	calculateSave(User user, Self self, List<SelfQuestionOption> questionOptions, List<QuestionVo> questionVos, List<SelfQuestion> questions, boolean refresh) {
	
	}
	

	/*************************************************************************************************/

	/**
	 * 计算SDS
	 * @param user
	 * @param self
	 * @param questionOptions
	 * @param questionVos
	 * @param questions
	 * @param refresh
	 * @return
	 */
	@Transactional
	calculateSDS(User user, Self self, List<SelfQuestionOption> questionOptions, List<QuestionVo> questionVos, List<SelfQuestion> questions, boolean refresh) {
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

		def result = []
		optionMap.each { k, v -> // 计算每一个类型的分数
			def score = 0
			v.each {
				score = score + it.score
			}
			def mapRecord = new MapRecord(
				name : k,
				value : score	
			)
			result << mapRecord

			if (resultMap.get(score)) { // key: 分数, value: 种类
				resultMap.get(score) << k
			} else {
				def typeNameList = []
				typeNameList << k
				resultMap.put(score, typeNameList)
			}
		}
		if (resultMap.empty || resultMap.size() == 0) {
			return '{"success": 0, "error": "err601"}'
		}

		def resultName = (resultMap.sort().values().sum() as List).reverse()

		if (resultName.size() <= 3) {
			return '{"success": 0, "error": "err601"}'
		}

		def resultOption = selfResultOptionRepository.findByTypeAlphabet(resultName[0] + '%', '%' + resultName[1] + '%', '%' + resultName[2] + '%', self)

		if (!resultOption) {
			resultOption = selfResultOptionRepository.findByTypeAlphabet(resultName[0] + '%', '%' + resultName[1] + '%', self)
		}

		if (resultOption[0] != null) {
			def selfUserQuestionnaire = selfPersistentService.saveQuestionnaireAndUserAnswer(user, self, questionVos, questions, questionOptions, resultOption[0], refresh, 0)
			if (refresh) {
				selfPersistentService.saveMapStatistics(user, selfUserQuestionnaire, result, resultOption[0], null) // 保存心理地图
			}
		}

		resultOption[0]
	}

	/**
	 * 计算PDP
	 * @param user
	 * @param self
	 * @param questionOptions
	 * @param questionIds
	 * @param questions
	 * @param refresh
	 * @return
	 */

	// TODO
	@Transactional
	calculatePDP(User user, Self self, List<SelfQuestionOption> questionOptions, List<QuestionVo> questionVos, List<SelfQuestion> questions, boolean refresh) {
		def optionMap = [:]
		questionOptions.each {
			def questionType = it.selfQuestion.selfQuestionType.name
			if (optionMap.get(questionType)) {
				optionMap.get(questionType) << it
			} else {
				def optionList = []
				optionList << it
				optionMap.put(questionType, optionList)
			}
		}

		def resultMap = [:]
		
		def result = []
		
		optionMap.each { k, v -> // 计算每一个类型的分数
			def score = 0
			v.each {
				score += it.score
			}
			def mapRecord = new MapRecord(
				name : k,
				value : score	
			)
			result << mapRecord
			if (resultMap.get(score)) { // key: 分数, value: 孔雀
				resultMap.get(score) << k
			} else {
				def typeList = []
				typeList << k
				resultMap.put(score, typeList)
			}
		}
		def resultNames = resultMap.get(resultMap.keySet().max()) as List,
			resultOption = selfResultOptionRepository.findByName(resultNames[0])

		if (resultOption) {
			def selfUserQuestionnaire = selfPersistentService.saveQuestionnaireAndUserAnswer(user, self, questionVos, questions, questionOptions, resultOption, refresh, 0)
			if (refresh) {
				selfPersistentService.saveMapStatistics(user, selfUserQuestionnaire, result, resultOption, null) // 保存心理地图
			}
		}

		resultOption
	}

	/**
	 * 计算MBTI
	 * @param user
	 * @param self
	 * @param questionOptions
	 * @param questionIds
	 * @param questions
	 * @param refresh
	 * @return
	 */
	@Transactional
	calculateMBTI(User user, Self self, List<SelfQuestionOption> questionOptions, List<QuestionVo> questionVos, List<SelfQuestion> questions, boolean refresh) {
		def optionMap = [:]
		questionOptions.each {

			def optionType = it.selfQuestionOptionType.name

			if (optionMap.get(optionType)) {
				optionMap.get(optionType) << it
			} else {
				def list = []
				list << it
				optionMap.put(optionType, list)
			}
		}
		def result = []
		optionMap.each { k, v ->
			def score = 0
			v.each {
				score += it.score
			}
			optionMap.get(k).clear()
			optionMap.put(k, score)
			def mapRecord = new MapRecord(
				name : k,
				value : score	
			)
			result << mapRecord
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
//		def	resultOption = selfResultOptionRepository.findOneByFilters(
//			[
//				name_equal : resultName,
//				'selfResult.self_equal' : self
//			]
//		)
		def resultOption = selfResultOptionRepository.findByTypeAlphabet(resultName[0] + '%', '%' + resultName[1] + '%', '%' + resultName[2] + '%','%' + resultName[3] + '%', self)
		if (!resultOption) {
			resultOption = selfResultOptionRepository.findByTypeAlphabet(resultName[0] + '%', '%' + resultName[1] + '%', '%' + resultName[2] + '%', self)
		}
		if (!resultOption) {
			resultOption = selfResultOptionRepository.findByTypeAlphabet(resultName[0] + '%', '%' + resultName[1] + '%', self)
		}

		if (resultOption[0] != null) {
			def selfUserQuestionnaire = selfPersistentService.saveQuestionnaireAndUserAnswer(user, self, questionVos, questions, questionOptions, resultOption[0], refresh, 0)
			if (refresh) {
				selfPersistentService.saveMapStatistics(user, selfUserQuestionnaire, result, resultOption[0], null)
			}
		}


		resultOption[0]
	}

	@Transactional
	calculateDISC(User user, Self self, List<SelfQuestionOption> questionOptions, List<QuestionVo> questionVos, List<SelfQuestion> questions, boolean refresh) {
		def optionMap = [:]
		questionOptions.each {

			def optionType = it.selfQuestionOptionType.name

			if (optionMap.get(optionType)) {
				optionMap.get(optionType) << it
			} else {
				def list = []
				list << it
				optionMap.put(optionType, list)
			}
		}
		def result = []
		def resultMap = [:]
		optionMap.each { k, v ->
			def score = 0
			v.each {
				score += it.score
			}
			def mapRecord = new MapRecord(
				name : k,
				value : score	
			)
			result << mapRecord
			
			if (resultMap.get(score)) { // key: 分数, value: 孔雀
				resultMap.get(score) << k
			} else {
				def typeList = []
				typeList << k
				resultMap.put(score, typeList)
			}
		}
		def resultScore = resultMap.keySet().max()
		def resultNames = resultMap.get(resultScore) as List,
			resultOption = selfResultOptionRepository.findOneByFilters(
				[
					name_equal : resultNames[0],
					'selfResult.self_equal' : self
				]	
			)

		if (resultOption) {
			def selfUserQuestionnaire =	selfPersistentService.saveQuestionnaireAndUserAnswer(user, self, questionVos, questions, questionOptions, resultOption, refresh, resultScore)
			if (refresh) {
				selfPersistentService.saveMapStatistics(user, selfUserQuestionnaire, result, resultOption, resultScore)
			}
		}

		resultOption
	}
	
	
	/**
	 * @param user
	 * @param self
	 * @param questionOptions
	 * @param questionVos
	 * @param questions
	 * @param refresh
	 * @return
	 * SCL90
	 */
	@Transactional
	calculateSCL90(User user, Self self, List<SelfQuestionOption> questionOptions, List<QuestionVo> questionVos, List<SelfQuestion> questions, boolean refresh) {
		
		def optionMap = [:]
		def score = 0
		
		
		questionOptions.each {
			
			score += it.score
		
			if (it.score == 1) { // 阴性
				if (optionMap.get('1')) {
					optionMap.get('1') << it
				} else {
					def list = []
					list << it
					optionMap.put('1', list)
				}
			}
		}
		def mapRecord, resultOption, result = []
		
		def resultScore = score
		
		def size1 = optionMap['1'] ? optionMap['1'].size() : 0
		if (score > 160 || (questionOptions.size() - size1) > 43) {
			
			def doubleScore =  (score - size1) as double
			resultScore = (doubleScore / (questionOptions.size() - size1) * 90) as Integer
		}
		mapRecord = new MapRecord(
			name : self.abbreviation,
			value :	resultScore
		)
		result << mapRecord
		
		resultOption = selfResultOptionRepository.findOneByFilters(
			[
				'selfResult.self_equal' : self,
				'highestScore_greaterThan' : resultScore,
				'lowestScore_lessThanOrEqualTo' : resultScore
			]
		)

		if (resultOption) {
			def selfUserQuestionnaire =	selfPersistentService.saveQuestionnaireAndUserAnswer(user, self, questionVos, questions, questionOptions, resultOption, refresh, resultScore)
			if (refresh) {
				selfPersistentService.saveMapStatistics(user, selfUserQuestionnaire, result, resultOption, resultScore)
			}
		}

		resultOption
	}
	
	/**
	 * 计算EPQ量表
	 * @param user
	 * @param self
	 * @param questionOptions
	 * @param questionVos
	 * @param questions
	 * @param refresh
	 * @return
	 */
	@Transactional
	calculateEPQ(User user, Self self, List<SelfQuestionOption> questionOptions, List<QuestionVo> questionVos, List<SelfQuestion> questions, boolean refresh) {
		
		def optionMap = [:]
		questionOptions.each {
			def questionType = it.selfQuestion.selfQuestionType.name

			if (optionMap.get(questionType)) { // key: 种类 : E, value: 题目
				optionMap.get(questionType) << it
			} else {
				def optionList = []
				optionList << it
				optionMap.put(questionType, optionList)
			}
		}

		def result = []
		optionMap.each { k, v -> // 计算每一个类型的分数
			def score = 0
			v.each {
				score = score + it.score
			}
			def mapRecord = new MapRecord(
				name : k,
				value : score
			)
			result << mapRecord
		}
		
		def option = result.find {
			it.name == self.abbreviation
		}
		
		def optionScore = option.value
		
		def resultOption = selfResultOptionRepository.findOneByFilters(
			[
				'selfResult.self_equal' : self,
				'highestScore_greaterThanOrEqualTo' : optionScore,
				'lowestScore_lessThanOrEqualTo' : optionScore
			]	
		)

		if (resultOption) {
			def selfUserQuestionnaire = selfPersistentService.saveQuestionnaireAndUserAnswer(user, self, questionVos, questions, questionOptions, resultOption, refresh, optionScore)
			if (refresh) {
				selfPersistentService.saveMapStatistics(user, selfUserQuestionnaire, result, resultOption, optionScore) // 保存心理地图
			}
		}


		resultOption
	}
	
	
	/**
	 * 计算正负情感题
	 * @param user
	 * @param self
	 * @param questionOptions
	 * @param questionVos
	 * @param questions
	 * @param refresh
	 * @return
	 */
	@Transactional
	calculatePANA(User user, Self self, List<SelfQuestionOption> questionOptions, List<QuestionVo> questionVos, List<SelfQuestion> questions, boolean refresh) {
		def optionMap = [:]
		questionOptions.each {
			def questionType = it.selfQuestion.selfQuestionType.name

			if (optionMap.get(questionType)) { // key: 种类 : PA NA , value: 题目
				optionMap.get(questionType) << it
			} else {
				def optionList = []
				optionList << it
				optionMap.put(questionType, optionList)
			}
		}

		def resultMap = [:]

		def result = []
		optionMap.each { k, v -> // 计算每一个类型的分数
			def score = 0
			v.each {
				score = score + it.score
			}
			
			optionMap.get(k).clear()
			optionMap.put(k, score)
		}
//		def panaScore = optionMap.get('PA') - optionMap.get('NA')
		
		def mapRecord = new MapRecord(
			name : new Date().time,
			value : optionMap.get('PA'),
			naValue : optionMap.get('NA')
		)
		result << mapRecord

		if (result != null) {
			def selfUserQuestionnaire = selfPersistentService.saveQuestionnaireAndUserAnswer(user, self, questionVos, questions, questionOptions, null, refresh, 0)
			if (refresh) {
				selfPersistentService.saveMapStatistics(user, selfUserQuestionnaire, result, null, null) // 保存心理地图
			}
		}

		def selfResult =  new SelfResult()
		def resultOption = new SelfResultOption(
			selfResult : selfResult,
			content : '正、负情绪情感量表需要您做满7天才会在心理地图中显示结果，请答满7天之后，记得在心理地图-情绪管理中查看结果哦！'
		)
		resultOption
//		'{"success": "1", "message" : "问卷已完成，请通过心里地图查看内容"}'
	}
	
	/**
	 * 计算成人抗压
	 * @param user
	 * @param self
	 * @param questionOptions
	 * @param questionVos
	 * @param questions
	 * @param refresh
	 * @return
	 */
	@Transactional
	calculateAdult(User user, Self self, List<SelfQuestionOption> questionOptions, List<QuestionVo> questionVos, List<SelfQuestion> questions, boolean refresh) {
		def optionMap = [:]
		questionOptions.each {
		
			def questionType = it?.selfQuestion?.selfQuestionType?.name

			if (questionType) {
				if (optionMap.get(questionType)) { // key: 种类 , value: 题目
					optionMap.get(questionType) << it
				} else {
					def optionList = []
					optionList << it
					optionMap.put(questionType, optionList)
				}
			}
		}

		def resultMap = [:]

		def result = []
		optionMap.each { k, v -> // 计算每一个类型的分数
			def score = 0
			v.each {
				score = score + it.score
			}
			
			optionMap.get(k).clear()
			optionMap.put(k, score)
		}
		def allScore = 0
		optionMap.each { k, v ->
			allScore += optionMap.get(k)
			
			def mapRecord = new MapRecord(
				name : k,
				value : optionMap.get(k)
			)
			result << mapRecord
		}
		
		def resultOption = selfResultOptionRepository.findOneByFilters(
			'selfResult.self_equal' : self,
			'highestScore_greaterThanOrEqualTo' : allScore,
			'lowestScore_lessThanOrEqualTo' : allScore
		)

		if (resultOption) {
			def selfUserQuestionnaire = selfPersistentService.saveQuestionnaireAndUserAnswer(user, self, questionVos, questions, questionOptions, null, refresh, allScore)
			if (refresh) {
				selfPersistentService.saveMapStatistics(user, selfUserQuestionnaire, result, resultOption, allScore) // 保存心理地图
			}
		}

		resultOption
	}
	
	/**
	 * 计算幸福指数
	 * @param user
	 * @param self
	 * @param questionOptions
	 * @param questionVos
	 * @param questions
	 * @param refresh
	 * @return
	 */
	@Transactional
	calculateHopeSense(User user, Self self, List<SelfQuestionOption> questionOptions, List<QuestionVo> questionVos, List<SelfQuestion> questions, boolean refresh) {
		
		def optionMap = [:]
		questionOptions.each {
			def questionType = it?.selfQuestion?.selfQuestionType?.name
			
			if (questionType) {
				if (optionMap.get(questionType)) { // key: 种类 , value: 题目
					optionMap.get(questionType) << it
				} else {
					def optionList = []
					optionList << it
					optionMap.put(questionType, optionList)
				}
			}
		}

		def resultMap = [:]

		def result = []
		optionMap.each { k, v -> // 计算每一个类型的分数
			def optionScore = 0
			v.each {
				optionScore = optionScore + it.score
			}
			
			optionMap.get(k).clear()
			optionMap.put(k, optionScore)
		}
		def allScore = 0
		questionOptions.each {
			allScore += it.score
		}
		
		optionMap.each { k, v ->
			def mapRecord = new MapRecord(
				name : k,
				value : optionMap.get(k)
			)
			result << mapRecord
		}
		
		def resultOption = selfResultOptionRepository.findOneByFilters(
			'selfResult.self_equal' : self,
			'highestScore_greaterThanOrEqualTo' : allScore,
			'lowestScore_lessThanOrEqualTo' : allScore
		)
		if (resultOption) {
			def selfUserQuestionnaire = selfPersistentService.saveQuestionnaireAndUserAnswer(user, self, questionVos, questions, questionOptions, null, refresh, allScore)
			if (refresh) {
				selfPersistentService.saveMapStatistics(user, selfUserQuestionnaire, result, resultOption, allScore) // 保存心理地图
			}
		}

		resultOption
	}
	
	/**
	 * 对成功的渴望
	 * @param user
	 * @param self
	 * @param questionOptions
	 * @param questionVos
	 * @param questions
	 * @param refresh
	 * @return
	 */
	@Transactional
	calculateAMS(User user, Self self, List<SelfQuestionOption> questionOptions, List<QuestionVo> questionVos, List<SelfQuestion> questions, boolean refresh) {
		
		def optionMap = [:]
		questionOptions.each {
			def questionType = it?.selfQuestion?.selfQuestionType?.name
			
			if (questionType) {
				if (optionMap.get(questionType)) { // key: 种类 , value: 题目
					optionMap.get(questionType) << it
				} else {
					def optionList = []
					optionList << it
					optionMap.put(questionType, optionList)
				}
			}
		}

		def resultMap = [:]

		def result = []
		optionMap.each { k, v -> // 计算每一个类型的分数
			def optionScore = 0
			v.each {
				optionScore = optionScore + it.score
			}
			
			optionMap.get(k).clear()
			optionMap.put(k, optionScore)
		}
		
		def resutlScore = optionMap['MS'] - optionMap['MF']
		
		optionMap.each { k, v ->
			def mapRecord = new MapRecord(
				name : k,
				value : optionMap.get(k)
			)
			result << mapRecord
		}
		
		def resultOption = selfResultOptionRepository.findOneByFilters(
			'selfResult.self_equal' : self,
			'highestScore_greaterThanOrEqualTo' : resutlScore,
			'lowestScore_lessThanOrEqualTo' : resutlScore
		)

		if (resultOption) {
			def selfUserQuestionnaire = selfPersistentService.saveQuestionnaireAndUserAnswer(user, self, questionVos, questions, questionOptions, null, refresh, resutlScore)
			if (refresh) {
				selfPersistentService.saveMapStatistics(user, selfUserQuestionnaire, result, resultOption, resutlScore) // 保存心理地图
			}
		}

		resultOption
	}
	/**
	 * 测测你上司的领导风格
	 * @param user
	 * @param self
	 * @param questionOptions
	 * @param questionVos
	 * @param questions
	 * @param refresh
	 * @return
	 */
	@Transactional
	calculateBass(User user, Self self, List<SelfQuestionOption> questionOptions, List<QuestionVo> questionVos, List<SelfQuestion> questions, boolean refresh) {
		
		def optionMap = [:]
		questionOptions.each {
			def questionType = it?.selfQuestion?.selfQuestionType?.name
			
			if (questionType) {
				if (optionMap.get(questionType)) { // key: 种类 , value: 题目
					optionMap.get(questionType) << it
				} else {
					def optionList = []
					optionList << it
					optionMap.put(questionType, optionList)
				}
			}
		}

		def typeMap = [:]

		def result = []
		optionMap.each { k, v -> // 计算每一个类型的分数
			def optionScore = 0
			v.each {
				optionScore = optionScore + it.score
			}
			
			typeMap.put(k, optionScore)
		}
		
		def resultOption
		
		if ((double)(typeMap['变革型领导']) / optionMap['变革型领导'].size() > (double)(typeMap['交易型领导']) / optionMap['交易型领导'].size()) {
			resultOption = selfResultOptionRepository.findOne(198l)
		}
		if ((double)(typeMap['变革型领导']) / optionMap['变革型领导'].size() < (double)(typeMap['交易型领导']) / optionMap['交易型领导'].size()) {
			resultOption = selfResultOptionRepository.findOne(199l)
		}
		typeMap.each { k, v ->
			def mapRecord = new MapRecord(
				name : k,
				value : typeMap.get(k)
			)
			result << mapRecord
		}

		if (resultOption) {
			def selfUserQuestionnaire = selfPersistentService.saveQuestionnaireAndUserAnswer(user, self, questionVos, questions, questionOptions, null, refresh, 0)
			if (refresh) {
				selfPersistentService.saveMapStatistics(user, selfUserQuestionnaire, result, resultOption, 0) // 保存心理地图
			}
		}
		
		resultOption
	}

	/**
	 * 计算ECR
	 * @param user
	 * @param self
	 * @param questionOptions
	 * @param questionVos
	 * @param questions
	 * @param refresh
	 * @return
	 */
	@Transactional
	calculateECR(User user, Self self, List<SelfQuestionOption> questionOptions, List<QuestionVo> questionVos, List<SelfQuestion> questions, boolean refresh) {
		def optionMap = [:]
		questionOptions.each {
			def questionType = it.selfQuestion.selfQuestionType.name

			if (optionMap.get(questionType)) { // key: 种类 : avoidance,anxiety, value: 题目
				optionMap.get(questionType) << it
			} else {
				def optionList = []
				optionList << it
				optionMap.put(questionType, optionList)
			}
		}

		def resultMap = [:]

		def result = []
		optionMap.each { k, v -> // 计算每一个类型的分数
			def score = 0
			v.each {
				score = score + it.score
			}
			def mapRecord = new MapRecord(
					name: k,
					value: score
			)
			result << mapRecord

			resultMap.put(k, score)

		}
		def avoidance = resultMap['Avoidance'] / optionMap['Avoidance'].size()
		def anxiety = resultMap['Anxiety'] / optionMap['Anxiety'].size()

		def sec2 = avoidance * 3.2893296 + anxiety * 5.4725318 - 1.5307833
		def fear2 = avoidance * 7.2371075 + anxiety * 8.1776448 - 32.3553266
		def pre2 = avoidance * 3.9246754 + anxiety * 9.7102446 - 28.4573220
		def dis2 = avoidance * 7.3654621 + anxiety * 4.9392039 - 22.22810880


		def ATT2 = 1
		if (sec2 > fear2 && sec2 > pre2 && sec2 > dis2) {
			ATT2 = 1
		}
		if (fear2 > sec2 && fear2 > pre2 && fear2 > dis2) {
			ATT2 = 2
		}
		if (pre2 > sec2 && pre2 > fear2 && pre2 > dis2) {
			ATT2 = 3
		}
		if (dis2 > sec2 && dis2 > fear2 && dis2 > pre2) {
			ATT2 = 4
		}

		def resultOption = selfResultOptionRepository.findOneByFilters(
				'selfResult.self_equal' : self,
				'highestScore_greaterThanOrEqualTo' : ATT2,
				'lowestScore_lessThanOrEqualTo' : ATT2
		)

		if (resultOption) {
			def selfUserQuestionnaire = selfPersistentService.saveQuestionnaireAndUserAnswer(user, self, questionVos, questions, questionOptions, resultOption, refresh, 0)
			if (refresh) {
				selfPersistentService.saveMapStatistics(user, selfUserQuestionnaire, result, resultOption, 0) // 保存心理地图
			}
		}

		resultOption
	}

	/**
	 * 计算RPUS
	 * @param user
	 * @param self
	 * @param questionOptions
	 * @param questionVos
	 * @param questions
	 * @param refresh
	 * @return
	 */
	@Transactional
	calculateRPUS(User user, Self self, List<SelfQuestionOption> questionOptions, List<QuestionVo> questionVos, List<SelfQuestion> questions, boolean refresh) {

		def allScore = 0
		def optionMap = [:]
		questionOptions.each {
			allScore = allScore + it.score
			def questionType = it.selfQuestion.selfQuestionType.name

			if (optionMap.get(questionType)) { // key: 种类 : avoidance,anxiety, value: 题目
				optionMap.get(questionType) << it
			} else {
				def optionList = []
				optionList << it
				optionMap.put(questionType, optionList)
			}
		}

		def resultMap = [:]

		def result = []
		optionMap.each { k, v -> // 计算每一个类型的分数
			def score = 0
			v.each {
				score = score + it.score
			}
			if (k == '成长性') {
				score = (score / 70 * 10) as int
			} else if (k == '满意感') {
				score = (score / 63 * 10) as int
			} else if (k == '冲突感') {
				score = (score / 35 * 10) as int
			} else if (k == '对等性') {
				score = (score / 28 * 10) as int
			}
			def mapRecord = new MapRecord(
					name: k,
					value: score
			)
			result << mapRecord

			resultMap.put(k, score)

		}

		def resultOption = selfResultOptionRepository.findOneByFilters(
				'selfResult.self_equal' : self,
				'highestScore_greaterThanOrEqualTo' : allScore,
				'lowestScore_lessThanOrEqualTo' : allScore
		)

		if (resultOption) {
			def selfUserQuestionnaire = selfPersistentService.saveQuestionnaireAndUserAnswer(user, self, questionVos, questions, questionOptions, resultOption, refresh, 0)
			if (refresh) {
				selfPersistentService.saveMapStatistics(user, selfUserQuestionnaire, result, resultOption, 0) // 保存心理地图
			}
		}

		resultOption
	}
	/**
	 * 计算PSDQ
	 * @param user
	 * @param self
	 * @param questionOptions
	 * @param questionVos
	 * @param questions
	 * @param refresh
	 * @return
	 */
	@Transactional
	calculatePSDQ(User user, Self self, List<SelfQuestionOption> questionOptions, List<QuestionVo> questionVos, List<SelfQuestion> questions, boolean refresh) {

		def isMale = questionOptions.any {
			it.id == 6908l
		} // 男

		def allScore = 0
		def optionMap = [:]
		questionOptions.each {
			allScore = allScore + it.score
			def questionType = it?.selfQuestion?.selfQuestionType?.name
			if (questionType) {
				if (optionMap.get(questionType)) { // key: 种类 : avoidance,anxiety, value: 题目
					optionMap.get(questionType) << it
				} else {
					def optionList = []
					optionList << it
					optionMap.put(questionType, optionList)

				}
			}
		}

		def resultMap = [:], resultOption

		def result = [], resultString = ''


		optionMap.each { k, v -> // 计算每一个类型的分数
			def score = 0
			v.each {
				score = score + it.score
			}

			if (isMale) {

				if (k == '健康') {
					resultOption = selfResultOptionRepository.findByMaleAndHealthy(score)
					resultString += resultOption.content + "\\n"
				} else if (k == '协调') {
					resultOption = selfResultOptionRepository.findByMaleAndCoordinate(score)
					resultString += resultOption.content + "\\n"
				} else if (k == '体育活动') {
					resultOption = selfResultOptionRepository.findByMaleAndSports(score)
					resultString += resultOption.content + "\\n"
				} else if (k == '身体肥胖') {
					resultOption = selfResultOptionRepository.findByMaleAndFat(score)
					resultString += resultOption.content + "\\n"
				} else if (k == '运动能力') {
					resultOption = selfResultOptionRepository.findByMaleAndExercise(score)
					resultString += resultOption.content + "\\n"
				} else if (k == '整体身体') {
					resultOption = selfResultOptionRepository.findByMaleAndBody(score)
					resultString += resultOption.content + "\\n"
				} else if (k == '外表') {
					resultOption = selfResultOptionRepository.findByMaleAndSurface(score)
					resultString += resultOption.content + "\\n"
				} else if (k == '力量') {
					resultOption = selfResultOptionRepository.findByMaleAndPower(score)
					resultString += resultOption.content + "\\n"
				} else if (k == '灵活') {
					resultOption = selfResultOptionRepository.findByMaleAndFlexible(score)
					resultString += resultOption.content + "\\n"
				} else if (k == '耐力') {
					resultOption = selfResultOptionRepository.findByMaleAndEndurance(score)
					resultString += resultOption.content + "\\n"
				} else if (k == '自尊') {
					resultOption = selfResultOptionRepository.findByMaleAndSelfEsteem(score)
					resultString += resultOption.content + "\\n"
				}
			} else {
				if (k == '健康') {
					resultOption = selfResultOptionRepository.findByMaleAndHealthy(score)
					resultString += resultOption.content + "\\n"
				} else if (k == '协调') {
					resultOption = selfResultOptionRepository.findByFemaleAndCoordinate(score)
					resultString += resultOption.content + "\\n"
				} else if (k == '体育活动') {
					resultOption = selfResultOptionRepository.findByFemaleAndSports(score)
					resultString += resultOption.content + "\\n"
				} else if (k == '身体肥胖') {
					resultOption = selfResultOptionRepository.findByFemaleAndFat(score)
					resultString += resultOption.content + "\\n"
				} else if (k == '运动能力') {
					resultOption = selfResultOptionRepository.findByFemaleAndExercise(score)
					resultString += resultOption.content + "\\n"
				} else if (k == '整体身体') {
					resultOption = selfResultOptionRepository.findByFemaleAndBody(score)
					resultString += resultOption.content + "\\n"
				} else if (k == '外表') {
					resultOption = selfResultOptionRepository.findByFemaleAndSurface(score)
					resultString += resultOption.content + "\\n"
				} else if (k == '力量') {
					resultOption = selfResultOptionRepository.findByMaleAndPower(score)
					resultString += resultOption.content + "\\n"
				} else if (k == '灵活') {
					resultOption = selfResultOptionRepository.findByFemaleAndFlexible(score)
					resultString += resultOption.content + "\\n"
				} else if (k == '耐力') {
					resultOption = selfResultOptionRepository.findByFemaleAndEndurance(score)
					resultString += resultOption.content + "\\n"
				} else if (k == '自尊') {
					resultOption = selfResultOptionRepository.findByFemaleAndSelfEsteem(score)
					resultString += resultOption.content + "\\n"
				}
			}


			def mapRecord = new MapRecord(
					name: k,
					value: score
			)
			result << mapRecord

			resultMap.put(k, score)

		}

//		resultOption = selfResultOptionRepository.findOneByFilters(
//				'selfResult.self_equal' : self,
//				'highestScore_greaterThanOrEqualTo' : allScore,
//				'lowestScore_lessThanOrEqualTo' : allScore
//		)

		def resultOpt = new SelfResultOption(
				content: resultString,
				title: '测试结果显示：'
		)

		if (resultOption) {
			def selfUserQuestionnaire = selfPersistentService.saveQuestionnaireAndUserAnswer(user, self, questionVos, questions, questionOptions, null, refresh, 0)
			if (refresh) {
				selfPersistentService.saveMapStatistics(user, selfUserQuestionnaire, result, null, 0) // 保存心理地图
			}
		}

		resultOpt
	}
}
