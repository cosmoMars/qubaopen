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
import com.qubaopen.survey.repository.self.SelfQuestionOrderRepository
import com.qubaopen.survey.repository.self.SelfQuestionRepository
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
	SelfSpecialInsertRepository selfSpecialInsertRepository

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
	calculateSelfReslut(@RequestParam long userId, @RequestParam long selfId, @RequestParam String questionJson, @RequestParam boolean refresh) {

		logger.trace ' -- 计算自测结果选项 -- '

		selfService.calculateSelfReslut(userId, selfId, questionJson, refresh)

	}

}
