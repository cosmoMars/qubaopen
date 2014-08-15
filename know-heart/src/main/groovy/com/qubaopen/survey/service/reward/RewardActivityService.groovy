package com.qubaopen.survey.service.reward

import org.apache.commons.lang3.time.DateFormatUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.qubaopen.survey.entity.reward.RewardActivity
import com.qubaopen.survey.repository.reward.RewardActivityRepository
import com.qubaopen.survey.repository.user.UserReceiveAddressRepository

@Service
public class RewardActivityService {

	@Autowired
	UserReceiveAddressRepository userReceiveAddressRepository

	@Autowired
	RewardActivityRepository rewardActivityRepository

	/**
	 * 获取上线奖品类型信息
	 * @param userId
	 * @return
	 */
	@Transactional
	retrieveOnlineReward(long userId) {
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
				'addressId': defaultAddress?.id,
				'rewardList': rewardList ?: []
			]
		result
	}
}
