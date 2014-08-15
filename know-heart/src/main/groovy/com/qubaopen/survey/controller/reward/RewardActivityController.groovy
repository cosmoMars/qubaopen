package com.qubaopen.survey.controller.reward

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.reward.RewardActivity
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.repository.reward.RewardActivityRepository
import com.qubaopen.survey.service.reward.RewardActivityService

@RestController
@RequestMapping('rewardActivitys')
@SessionAttributes('currentUser')
public class RewardActivityController extends AbstractBaseController<RewardActivity, Long> {

	@Autowired
	RewardActivityRepository rewardActivityRepository

	@Autowired
	RewardActivityService rewardActivityService

	@Override
	protected MyRepository<RewardActivity, Long> getRepository() {
		rewardActivityRepository
	}

	/**
	 * 新增
	 */
	@Override
	@RequestMapping(method = RequestMethod.POST)
	add(@RequestBody RewardActivity entity, BindingResult result) {

		def rewardActivity = rewardActivityRepository.save(entity)
		[
			"success" : "1",
			"rewardActivityId" : rewardActivity.id
		]
	}


	/**
	 * 获取当天上线奖品活动
	 * @return
	 */
	@RequestMapping(value = 'retrieveOnlineReward', method = RequestMethod.GET)
	retrieveOnlineReward(@ModelAttribute('currentUser') User user) {

		logger.trace ' -- 获取上线奖品活动 -- '

		rewardActivityService.retrieveOnlineReward(user.id)
	}

}
