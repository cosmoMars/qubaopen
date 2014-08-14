package com.qubaopen.survey.service.self

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.fasterxml.jackson.databind.ObjectMapper
import com.qubaopen.survey.entity.interest.InterestQuestion
import com.qubaopen.survey.entity.self.Self
import com.qubaopen.survey.entity.self.SelfQuestionOption
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.vo.QuestionVo
import com.qubaopen.survey.repository.self.SelfResultOptionRepository

@Service
public class ResultService {

	@Autowired
	ObjectMapper objectMapper

	@Autowired
	PersistentService persistentService

	@Autowired
	SelfResultOptionRepository selfResultOptionRepository
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
	calculateSDS(User user, Self self, List<SelfQuestionOption> questionOptions, List<QuestionVo> questionVos, List<InterestQuestion> questions, boolean refresh) {
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

		def resultList = []
		optionMap.each { k, v -> // 计算每一个类型的分数
			def score = 0
			v.each {
				score = score + it.score
			}
			def tempMap = [name : k, value : score]
			resultList << tempMap

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
			persistentService.saveMapStatistics(user, self, objectMapper.writeValueAsString(resultList), result[0], 0) // 保存心理地图

			persistentService.saveQuestionnaireAndUserAnswer(user, self, questionVos, questions, questionOptions, result[0])
		}

		result[0]
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
	@Transactional
	calculatePDP(User user, Self self, List<SelfQuestionOption> questionOptions, List<QuestionVo> questionVos, List<InterestQuestion> questions, boolean refresh) {
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
			persistentService.saveMapStatistics(user, self, null, result[0], 0) // 保存心理地图

			persistentService.saveQuestionnaireAndUserAnswer(user, self, questionVos, questions, questionOptions, result[0])
		}

		result[0]
	}


	/**
	 * 计算ABCD
	 * @param user
	 * @param self
	 * @param questionOptions
	 * @param questionIds
	 * @param questions
	 * @param refresh
	 * @return
	 */
	@Transactional
	calculateABCD(User user, Self self, List<SelfQuestionOption> questionOptions, List<QuestionVo> questionVos, List<InterestQuestion> questions, boolean refresh) {
		def score = 0

		questionOptions.each {
			 score += it.score
		}

		if (self.selfType.name == 'AB') {
			score = score * 3
		}
		def result = selfResultOptionRepository.findOneByFilters(
			'selfResult.self' : self,
			highestScore_greaterThanOrEqualTo : score,
			lowestScore_lessThanOrEqualTo : score
		)

		if (refresh) {
			persistentService.saveMapStatistics(user, self, null, result, score)

			persistentService.saveQuestionnaireAndUserAnswer(user, self, questionVos, questions, questionOptions, result)
		}

		result
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
	calculateMBTI(User user, Self self, List<SelfQuestionOption> questionOptions, List<QuestionVo> questionVos, List<InterestQuestion> questions, boolean refresh) {
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
		def resultList = []
		optionMap.each { k, v ->
			def score = 0
			v.each {
				score += it.score
			}
			optionMap.get(k).clear()
			optionMap.put(k, score)
			def tempMap = [name : k, value : score]
			resultList << tempMap
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
			persistentService.saveMapStatistics(user, self, objectMapper.writeValueAsString(resultList), result, 0)

			persistentService.saveQuestionnaireAndUserAnswer(user, self, questionVos, questions, questionOptions, result)
		}
		result
	}
}
