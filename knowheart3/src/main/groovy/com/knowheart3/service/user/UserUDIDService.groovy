package com.knowheart3.service.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.knowheart3.repository.user.UserInfoRepository
import com.knowheart3.repository.user.UserUDIDRepository
import com.knowheart3.utils.DateCommons

@Service
public class UserUDIDService {

	@Autowired
	UserInfoRepository userInfoRepository

	@Autowired
	UserUDIDRepository userUDIDRepository


	@Transactional
	retrieveUserUDID(long userId) {

		def udid = userUDIDRepository.findOne(userId),
			userInfo = userInfoRepository.findOne(userId)

		def result = [
			'success' : '1',
			'message' : '成功',
			'id' : udid.id,
			'push' : udid?.push,
			'startTime' : DateCommons.Date2String(udid?.startTime, "HH:mm"),
			'endTime' : DateCommons.Date2String(udid?.endTime, "HH:mm"),
			'saveFlow' : userInfo?.saveFlow,
			'publicAnswersToFriend' : userInfo?.publicAnswersToFriend
		]
		result
	}

}
