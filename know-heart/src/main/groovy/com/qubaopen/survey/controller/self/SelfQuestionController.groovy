package com.qubaopen.survey.controller.self

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.self.Self
import com.qubaopen.survey.entity.self.SelfQuestion
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.vo.QuestionVo
import com.qubaopen.survey.repository.self.SelfQuestionOptionRepository
import com.qubaopen.survey.repository.self.SelfQuestionOrderRepository
import com.qubaopen.survey.repository.self.SelfQuestionRepository
import com.qubaopen.survey.repository.self.SelfRepository
import com.qubaopen.survey.repository.self.SelfResultOptionRepository
import com.qubaopen.survey.repository.self.SelfSpecialInsertRepository
import com.qubaopen.survey.service.self.SelfService

@RestController
@RequestMapping("selfQuestions")
public class SelfQuestionController extends AbstractBaseController<SelfQuestion, Long> {

	@Autowired
	SelfQuestionRepository selfQuestionRepository

	@Autowired
	SelfQuestionOrderRepository selfQuestionOrderRepository

	@Autowired
	SelfQuestionOptionRepository selfQuestionOptionRepository

	@Autowired
	SelfSpecialInsertRepository selfSpecialInsertRepository

	@Autowired
	SelfRepository selfRepository

	@Autowired
	SelfResultOptionRepository selfResultOptionRepository

	@Autowired
	SelfService selfService

	@Override
	protected MyRepository<SelfQuestion, Long> getRepository() {
		selfQuestionRepository
	}

	/**
	 * 获取自测问卷
	 * @param selfId
	 * @return
	 */
	@RequestMapping(value =  'findBySelf/{selfId}', method = RequestMethod.GET)
	findBySelf(@PathVariable long selfId) {

		logger.trace ' -- 获取自测问卷 -- '

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
	 * 计算自测结果选项
	 * @param userId
	 * @param selfId
	 * @param questionJson
	 * @return
	 */
	@RequestMapping(value = 'calculateSelfResult', method = RequestMethod.GET)
	calculateSelfReslut(@RequestParam long userId, @RequestParam long selfId, @RequestParam String questionJson) {

		logger.trace ' -- 计算自测结果选项 -- '

		def user = new User(id : userId),
			self = selfRepository.findOne(selfId)

		if (!self) {
			return '{"success": 0, "error": "err122"}'
		}

		def javaType = objectMapper.typeFactory.constructParametricType(ArrayList.class, QuestionVo.class)
		def questionVos = objectMapper.readValue(questionJson, javaType)

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

				if (optionMap.get(questionType)) { // key: 种类, value: 题目
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
				if (resultMap.get(score)) { // key: 分数, value: 种类
					resultMap.get(score) << k
				} else {
					def typeNameList = []
					typeNameList << k
					resultMap.put(score, typeNameList)
				}
			}

			def resultName = (resultMap.sort().values().sum() as List).reverse()

			def result = selfResultOptionRepository.findByTypeAlphabet(resultName[0] + '%', '%' + resultName[1] + '%', '%' + resultName[2] + '%')

			if (!result) {
				return '{"success": 0, "error": "err2323"}'
			}

			selfService.saveQuestionnaireAndUserAnswer(user, self, questionVos, questions, questionOptions, result[0])

			return result[0]

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

			selfService.saveQuestionnaireAndUserAnswer(user, self, questionVos, questions, questionOptions, result[0])

			return result[0]


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
			selfService.saveQuestionnaireAndUserAnswer(user, self, questionVos, questions, questionOptions, result)
		}
	}

}
