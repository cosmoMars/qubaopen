package com.qubaopen.survey.controller.reward

import org.apache.commons.lang3.time.DateFormatUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.reward.RewardActivity
import com.qubaopen.survey.repository.reward.RewardActivityRepository
import com.qubaopen.survey.repository.user.UserReceiveAddressRepository

@RestController
@RequestMapping("rewardActivitys")
public class RewardActivityController extends AbstractBaseController<RewardActivity, Long> {

	@Autowired
	RewardActivityRepository rewardActivityRepository

	@Autowired
	UserReceiveAddressRepository userReceiveAddressRepository

	@Override
	protected MyRepository<RewardActivity, Long> getRepository() {
		rewardActivityRepository
	}


	/**
	 * 获取当天上线奖品活动
	 * @return
	 */
	@RequestMapping(value = 'findOnlineReward/{userId}', method = RequestMethod.GET)
	findOnlineReward(@PathVariable long userId) {

		logger.trace ' -- 获取上线奖品活动 -- '

		def defaultAddress = userReceiveAddressRepository.findDefaultAddressByUserId(userId),
			today = DateFormatUtils.format(new Date(), "yyyy-MM-dd"),
			rewardList = rewardActivityRepository.findAll(
				[
					startTime_lessThan: today,
					endTime_greaterThan: today,
					status_equal: RewardActivity.Status.ONLINE
				]
			)
		def result = [
				'addressId': defaultAddress.id ?: '',
				'rewardList': rewardList ?: []
			]
		result
	}

}
