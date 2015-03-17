package com.knowheart3.service.user

import com.knowheart3.repository.user.UserIDCardBindRepository
import com.knowheart3.repository.user.UserInfoRepository
import com.knowheart3.repository.user.UserReceiveAddressRepository
import com.qubaopen.survey.entity.user.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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

		def userInfo = userInfoRepository.findOne(userId),
			userIdCardBind = userIDCardBindRepository.findByUserId(userId)
		def user = new User(id : userId),
			defaultAddress = userReceiveAddressRepository.findByUserAndTrueAddress(user, true)

		[
			'success' : '1',
			'userId' : userId,
			'name' : userIdCardBind?.userIDCard?.name,
			'nickName' : userInfo?.nickName,
			'sex' : userInfo?.sex?.ordinal(),
			'birthday' : userInfo?.birthday,
			'bloodType' : userInfo?.bloodType?.ordinal(),
			'email' : userInfo?.user.email,
			'defaultAddress' : defaultAddress?.detialAddress,
			'IdCard' : userIdCardBind?.userIDCard?.IDCard,
			'avatarPath' : userInfo?.avatarPath,
			'district' : '',
			'signature' : userInfo?.signature
		]
	}

}
