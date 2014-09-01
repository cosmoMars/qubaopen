package com.qubaopen.survey.service.self

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.fasterxml.jackson.databind.ObjectMapper
import com.qubaopen.survey.entity.mindmap.MapRecord;
import com.qubaopen.survey.entity.self.Self
import com.qubaopen.survey.entity.self.SelfQuestion
import com.qubaopen.survey.entity.self.SelfQuestionOption
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.vo.QuestionVo
import com.qubaopen.survey.repository.self.SelfResultOptionRepository

@Service
public class SelfResultService {

	@Autowired
	ObjectMapper objectMapper

	@Autowired
	SelfPersistentService selfPersistentService

	@Autowired
	SelfResultOptionRepository selfResultOptionRepository

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

		def resultOption = selfResultOptionRepository.findOneByFilters(
			'selfResult.self_equal' : self,
			'highestScore_greaterThanOrEqualTo' : score,
			'lowestScore_lessThanOrEqualTo' : score
		)
		
		if (refresh) {
			def mapRecord = new MapRecord(
				name : self.abbreviation,
				value : score
			)
			def result = []
			result << mapRecord
			selfPersistentService.saveMapStatistics(user, self, result, resultOption, score)

			selfPersistentService.saveQuestionnaireAndUserAnswer(user, self, questionVos, questions, questionOptions, resultOption)
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
//			case 'EPQ' :
//				result = calculateSDS(user, self, questionOptions, questionVos, questions, refresh)
//				break
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

		def resultOption = selfResultOptionRepository.findByTypeAlphabet(resultName[0] + '%', '%' + resultName[1] + '%', '%' + resultName[2] + '%')

		if (refresh) {
			selfPersistentService.saveMapStatistics(user, self, result, resultOption[0], 0) // 保存心理地图

			selfPersistentService.saveQuestionnaireAndUserAnswer(user, self, questionVos, questions, questionOptions, resultOption[0])
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

		if (refresh) {
			selfPersistentService.saveMapStatistics(user, self, result, resultOption, 0) // 保存心理地图

			selfPersistentService.saveQuestionnaireAndUserAnswer(user, self, questionVos, questions, questionOptions, resultOption)
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
		def resultOption = selfResultOptionRepository.findByName(resultName)

		if (refresh) {
			selfPersistentService.saveMapStatistics(user, self, result, resultOption, 0)

			selfPersistentService.saveQuestionnaireAndUserAnswer(user, self, questionVos, questions, questionOptions, resultOption)
		}

		resultOption
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
		}

		if (refresh) {
			selfPersistentService.saveMapStatistics(user, self, result, null, 0)

			selfPersistentService.saveQuestionnaireAndUserAnswer(user, self, questionVos, questions, questionOptions, null)
		}

		'{"success": "1", "message" : "问卷已完成，请通过心里地图查看内容"}'
	}
}
