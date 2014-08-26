package com.qubaopen.survey.service.interest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.qubaopen.survey.entity.interest.Interest
import com.qubaopen.survey.entity.interest.InterestQuestion
import com.qubaopen.survey.entity.interest.InterestQuestionOption
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.vo.QuestionVo
import com.qubaopen.survey.repository.interest.InterestQuestionOptionRepository
import com.qubaopen.survey.repository.interest.InterestQuestionOrderRepository
import com.qubaopen.survey.repository.interest.InterestResultOptionRepository

@Service
public class InterestResultService {

	@Autowired
	InterestResultOptionRepository interestResultOptionRepository

	@Autowired
	InterestPersistentService interestPersistentService

	@Autowired
	InterestQuestionOptionRepository interestQuestionOptionRepository

	@Autowired
	InterestQuestionOrderRepository interestQuestionOrderRepository

	@Transactional
	calculateScore(User user, Interest interest, List<InterestQuestionOption> questionOptions, List<QuestionVo> questionVos, List<InterestQuestion> questions) {

		def score = 0

		questionOptions.each {
			score += it.score
		}
		score = score * interest.coefficient

		def result = interestResultOptionRepository.findOneByFilters(
			'interestResult.interest_equal' : interest,
			'highestScore_greaterThanOrEqualTo' : score,
			'lowestScore_lessThanOrEqualTo' : score
		)

		interestPersistentService.saveQuestionnaireAndUserAnswer(user, interest, questionVos, questions, questionOptions, result)

		result
	}

	@Transactional
	calculateQType(User user, Interest interest, List<InterestQuestionOption> questionOptions, List<QuestionVo> questionVos, List<InterestQuestion> questions) {


	}

	@Transactional
	calculateOType(User user, Interest interest, List<InterestQuestionOption> questionOptions, List<QuestionVo> questionVos, List<InterestQuestion> questions) {


	}

	@Transactional
	calculateTurn(User user, Interest interest, List<InterestQuestionOption> questionOptions, List<QuestionVo> questionVos, List<InterestQuestion> questions) {

		def questionOrders = interestQuestionOrderRepository.findByInterestAndNextQuestionId(interest, 0)

		def resultOrder = questionOrders.find {
			questionOptions.find {  qo ->
				it.optionId = qo.id
			}
		}

		def result = interestResultOptionRepository.findOne(resultOrder.resultOptionId)
		interestPersistentService.saveQuestionnaireAndUserAnswer(user, interest, questionVos, questions, questionOptions, result)

		result
	}

	@Transactional
	calculateSave(User user, Interest interest, List<InterestQuestionOption> questionOptions, List<QuestionVo> questionVos, List<InterestQuestion> questions) {


	}
}
