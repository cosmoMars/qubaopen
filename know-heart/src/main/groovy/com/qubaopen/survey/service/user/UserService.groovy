package com.qubaopen.survey.service.user


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserInfo
import com.qubaopen.survey.entity.user.UserUDID
import com.qubaopen.survey.repository.user.UserInfoRepository
import com.qubaopen.survey.repository.user.UserRepository
import com.qubaopen.survey.repository.user.UserUDIDRepository
import com.qubaopen.survey.utils.DateCommons

@Service
public class UserService {

	@Autowired
	UserRepository userRepository

	@Autowired
	UserInfoRepository userInfoRepository

	@Autowired
	UserUDIDRepository userUDIDRepository

	/**
	 * 保存user，新建userInfo，保存头像
	 * @param user
	 * @param avatar
	 * @return
	 */
	@Transactional
	void saveUserAndUserAvatar(User user, MultipartFile avatar) {

		user = userRepository.save(user)

		def userInfo = new UserInfo(
			id : user.id,
			publicAnswersToFriend : true,
			avatar : avatar.bytes
		)
		def userUdid = new UserUDID(
			id : user.id,
			push : true,
			startTime : DateCommons.String2Date('09:00','HH:mm'),
			endTime : DateCommons.String2Date('22:00','HH:mm')
		)

		userInfoRepository.save(userInfo)
		userUDIDRepository.save(userUdid)

	}

}
