package com.qubaopen.survey.controller.reward

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.reward.RewardActivity
import com.qubaopen.survey.repository.reward.RewardActivityRepository
import com.qubaopen.survey.service.reward.RewardActivityService

@RestController
@RequestMapping('rewardActivitys')
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
	 * 获取当天上线奖品活动
	 * @return
	 */
	@RequestMapping(value = 'retrieveOnlineReward/{userId}', method = RequestMethod.GET)
	retrieveOnlineReward(@PathVariable long userId) {

		logger.trace ' -- 获取上线奖品活动 -- '

		rewardActivityService.retrieveOnlineReward(userId)
	}

}
