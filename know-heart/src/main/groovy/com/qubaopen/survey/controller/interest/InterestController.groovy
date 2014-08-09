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
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.repository.interest.InterestRepository
import com.qubaopen.survey.service.interest.InterestService

@RestController
@RequestMapping('interests')
public class InterestController extends AbstractBaseController<Interest, Long> {

	@Autowired
	InterestRepository interestRepository

	@Autowired
	InterestService interestService

	@Override
	protected MyRepository<Interest, Long> getRepository() {
		interestRepository
	}

	/**
	 * 获取用户兴趣问卷
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = 'retrieveInterest/{userId}', method = RequestMethod.GET)
	retrieveInterest(@PathVariable long userId) {

		logger.trace ' -- 获取用户兴趣问卷 -- '

		def user = new User(id : userId)
		def interestList = interestRepository.findUnfinishInterest(user)

		//TODO 计算答问卷的好友数量
		def result = []
		result << ['success' : "1"]
		interestList.each {
			def interest = [
				'id' : it.id,
				'interestType' : it.interestType.name,
				'questionnaireTagType' : it.questionnaireTagType.name,
				'type' : it.type.toString(),
				'title' : it.title,
				'golds' : it.golds ?: 0,
				'status' : it.status.toString(),
				'remark' : it.remark,
				'totalRespondentsCount' : it.totalRespondentsCount ?: 0,
				'recommendedValue' : it.recommendedValue ?: 0,
				'friendCount' : 0
			]
			result << interest
		}
		result
	}

	/**
	 * 通过用户问题选项，计算得到结果选项
	 * @param questionOptions
	 * @return
	 */
	@RequestMapping(value = 'calculateInterestResult', method = RequestMethod.GET)
	calculateInterestResult(
		@RequestParam long userId,
		@RequestParam(required = false) long interestId,
		@RequestParam(required = false) String questionJson) {

		logger.trace ' -- 通过用户问题选项，计算得到结果选项 -- '

		if (!interestId) {
			return '{"success": "0", "message": "err1234"}'
		}
		if (!questionJson) {
			return '{"success": "0", "message": "err1234"}'
		}

		interestService.calculateInterestResult(userId, interestId, questionJson)
	}

}
