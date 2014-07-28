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

		def user = new User(id : userId),
			self = selfRepository.findOne(selfId)

		def javaType = objectMapper.typeFactory.constructParametricType(ArrayList.class, QuestionVo.class)
		def questions = objectMapper.readValue(questionJson, javaType)

		def ids = []
		questions.each {
			ids += it.choiceIds as List
		}

		def questionOptions = selfQuestionOptionRepository.findAll(ids)

		def selfType = self.selfType

		if (selfType.name == 'SDS') {
			def optionMap = [:]
			questionOptions.each {
				def questionType = it.selfQuestion.selfQuestionType.name

				if (optionMap.get(questionType)) { // 按照问题类型分类
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
				if (resultMap.get(score)) { // key: 分数, value: 类型
					resultMap.get(score) << k
				} else {
					def typeList = []
					typeList << k
					resultMap.put(score, typeList)
				}
			}

			resultMap.sort() // 根据key排序

			resultMap.sort().reverseEach {

			}

//			selfService.saveQuestionnaireAndUserAnswer(user, self, questions, questionOptions)

			return resultMap
		} else if (selfType.name == 'PDP') {

		}

	}

}
