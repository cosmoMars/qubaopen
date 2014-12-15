package com.qubaopen.doctor.controller.user;

import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import com.qubaopen.doctor.repository.user.UserIDCardBindRepository
import com.qubaopen.survey.entity.user.User

@Service
public class UserIDCardBindService {
	
	@Autowired
	UserIDCardBindRepository userIDCardBindRepository

	/**
	 * 提交身份证验证，每月每个账号只能认证3次，修改1次
	 * @param idCard
	 * @param name
	 * @param user
	 * @return
	 */
	
	def calculateAgeByIdCard(User user) {
		def userIDCardBind = userIDCardBindRepository.findOne(user.id)
		if (!userIDCardBind) {
			return null
		}
		def idCard = userIDCardBind.userIDCard.IDCard,
			age = Integer.valueOf(DateTime.now().year) - Integer.valueOf(idCard.substring(6, 10))
		
		String lastValue = idCard.substring(idCard.length() - 1, idCard.length());
		def sex
		if (lastValue.trim().toLowerCase().equals('x') || lastValue.trim().toLowerCase().equals('e')) {
			sex = 0
		} else {
			sex = Integer.parseInt(lastValue) % 2
		}
		[age : age, sex : sex]
	}
		
}
