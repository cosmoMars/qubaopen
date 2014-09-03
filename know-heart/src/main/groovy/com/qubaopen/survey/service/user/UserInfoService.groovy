package com.qubaopen.survey.service.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.repository.user.UserIDCardBindRepository
import com.qubaopen.survey.repository.user.UserInfoRepository
import com.qubaopen.survey.repository.user.UserReceiveAddressRepository

@Service
public class UserInfoService {

	@Autowired
	UserIDCardBindRepository userIDCardBindRepository

	@Autowired
	UserInfoRepository userInfoRepository

	@Autowired
	UserReceiveAddressRepository userReceiveAddressRepository


	/**
	 * 获取个人信息
	 * @param userId
	 * @return
	 */
	@Transactional
	retrievePersonalInfo(long userId) {

		def userInfo = userInfoRepository.findOne(userId)

		def userIdCardBind = userIDCardBindRepository.findByUserId(userId)


		def user = new User(id : userId),
			defaultAddress = userReceiveAddressRepository.findByUserAndTrueAddress(user, true)

		def result = [
			'success' : '1',
			'userId' : userId,
			'name' : userInfo?.name,
			'nickName' : userInfo?.nickName,
			'sex' : userInfo?.sex,
			'birthday' : userInfo?.birthday,
			'bloodType' : userInfo?.bloodType,
			'email' : userInfo?.user.email,
			'defaultAddress' : defaultAddress?.detialAddress,
			'IdCard' : userIdCardBind?.userIDCard?.IDCard,
			'avatarPath' : userInfo?.avatarPath,
			'district' : '',
			'signature' : userInfo?.signature

		]

		result
	}

}
