package com.qubaopen.survey.controller.interest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.interest.InterestQuestion
import com.qubaopen.survey.repository.interest.InterestQuestionRepository
import com.qubaopen.survey.service.interest.InterestService


@RestController
@RequestMapping('interestQuestions')
public class InterestQuestionController extends AbstractBaseController<InterestQuestion, Long> {

	@Autowired
	InterestQuestionRepository interestQuestionRepository

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

		logger.trace ' -- 通过问卷查询问卷问题，问题顺序，特殊问题插入 -- '

		interestService.findByInterest(interestId)

	}

}
