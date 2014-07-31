package com.qubaopen.survey.service.user


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserInfo
import com.qubaopen.survey.repository.user.UserInfoRepository
import com.qubaopen.survey.repository.user.UserRepository

@Service
public class UserService {

	@Autowired
	UserRepository userRepository

	@Autowired
	UserInfoRepository userInfoRepository

	/**
	 * 保存user，新建userInfo，保存头像
	 * @param user
	 * @param avatar
	 * @return
	 */
	@Transactional
	saveUserAndUserAvatar(User user, MultipartFile avatar) {

		user = userRepository.save(user)

		def userInfo = new UserInfo(
			id : user.id,
			avatar : avatar.bytes
		)
		userInfoRepository.save(userInfo)
	}

}
