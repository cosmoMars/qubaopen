package com.qubaopen.survey.controller.interest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.interest.Interest
import com.qubaopen.survey.entity.interest.InterestQuestion
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.vo.QuestionVo
import com.qubaopen.survey.repository.interest.InterestQuestionOptionRepository
import com.qubaopen.survey.repository.interest.InterestQuestionOrderRepository
import com.qubaopen.survey.repository.interest.InterestQuestionRepository
import com.qubaopen.survey.repository.interest.InterestResultOptionRepository
import com.qubaopen.survey.repository.interest.InterestSpecialInsertRepository
import com.qubaopen.survey.service.interest.InterestService


@RestController
@RequestMapping("interestQuestions")
public class InterestQuestionController extends AbstractBaseController<InterestQuestion, Long> {

	@Autowired
	InterestQuestionRepository interestQuestionRepository

	@Autowired
	InterestResultOptionRepository interestResultOptionRepository

	@Autowired
	InterestQuestionOrderRepository interestQuestionOrderRepository

	@Autowired
	InterestSpecialInsertRepository interestSpecialInsertRepository

	@Autowired
	InterestQuestionOptionRepository interestQuestionOptionRepository

	@Autowired
	InterestService interestService

	@Override
	protected MyRepository<InterestQuestion, Long> getRepository() {
		interestQuestionRepository
	}

	/**
	 * 通过问卷查询问卷问题，问题顺序，特殊问题插入
	 * @param interestId
	 * @return
	 */
	@RequestMapping(value = 'findByInterest/{interestId}', method = RequestMethod.GET)
	findByInterest(@PathVariable long interestId) {
		def interest = new Interest(id : interestId),
		 	questions = interestQuestionRepository.findByInterest(interest),
			questionOrders = []
		if (questions) {
			questionOrders = interestQuestionOrderRepository.findByInterestQuestion(questions)
		}

		def specialInserts = interestSpecialInsertRepository.findByInterest(interest)

		def result = [
			'questions' : questions,
			'questionOrders' : questionOrders,
			'specialInserts' : specialInserts
		]

		return result
	}

	/**
	 * 通过用户问题选项，计算得到结果选项
	 * @param questionOptions
	 * @return
	 */
	@RequestMapping(value = 'calculateInterestResult', method = RequestMethod.GET)
	calculateInterestResult(
		@RequestParam long userId,
		@RequestParam long interestId,
		@RequestParam String questionJson) {

		def interest = new Interest(id : interestId),
			user = new User(id : userId)

		def javaType = objectMapper.typeFactory.constructParametricType(ArrayList.class, QuestionVo.class)
		def questionVos = objectMapper.readValue(questionJson, javaType)

		def questionIds = [], optionIds = []

		questionVos.each {
			questionIds << it.questionId
			optionIds += it.choiceIds as List
		}

//		def score = interestQuestionOptionRepository.sumQuestionOptions(ids as long[])
		def questions = interestQuestionRepository.findAll(questionIds),
			questionOptions = interestQuestionOptionRepository.findAll(optionIds)

		def score = 0
		questionOptions.each {
			score = score + it.score
		}

	    def resultOption = interestResultOptionRepository.findOneByFilters(
			[
				'interestResult.interest_equal' : interest,
				'lowestScore_lessThanOrEqualTo' : score,
				'highestScore_greaterThanOrEqualTo' : score
			]
		)

		// 保存用户问卷答卷，以及用户问卷问题
		interestService.saveQuestionnaireAndUserAnswers(user, interest, questionVos, questions, questionOptions, resultOption)

		resultOption
	}

}
