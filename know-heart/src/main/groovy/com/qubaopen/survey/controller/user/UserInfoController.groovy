package com.qubaopen.survey.controller.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserInfo
import com.qubaopen.survey.repository.user.UserIDCardBindRepository
import com.qubaopen.survey.repository.user.UserInfoRepository
import com.qubaopen.survey.repository.user.UserReceiveAddressRepository
import com.qubaopen.survey.repository.user.UserRepository

@RestController
@RequestMapping("userInfos")
public class UserInfoController extends AbstractBaseController<UserInfo, Long> {

	@Autowired
	UserInfoRepository userInfoRepository

	@Autowired
	UserRepository userRepository

	@Autowired
	UserIDCardBindRepository userIDCardBindRepository

	@Autowired
	UserReceiveAddressRepository userReceiveAddressRepository

	@Override
	protected MyRepository<UserInfo, Long> getRepository() {
		userInfoRepository
	}

	@RequestMapping(value = 'retrievePersonalInfo/{userId}', method = RequestMethod.GET)
	retrievePersonalInfo(@PathVariable long userId) {

		logger.trace ' -- 获得用户个人信息 -- '

		def userIdCardBind = userIDCardBindRepository.findByUserId(userId)

		def userInfo = userInfoRepository.findOne(userId)

		def user = new User(id : userId),
			defaultAddress = userReceiveAddressRepository.findByUserAndDefaultAddress(user, true)

		def result = [
			'userId' : userId,
			'sex' : userInfo.sex ?: '',
			'birthday' : userInfo?.birthday ?: '',
			'bloodType' : userInfo?.bloodType ?: '',
			'email' : userInfo.user.email ?: '',
			'defaultAddress' : defaultAddress.detialAddress ?: '',
			'IDCard' : userIdCardBind?.userIDCard?.IDCard ?: ''
		]

		result
	}

	/**
	 * 上传头像
	 * @param userId
	 * @param avatar
	 */
	@RequestMapping(value = 'uploadAvatar', method = RequestMethod.POST, consumes = 'multipart/form-data')
	void uploadAvatar(@RequestParam long userId, @RequestParam MultipartFile avatar) {

//		def width = 128,
//			height = 128
////			ratio = 0.0
//
//		BufferedImage photo = ImageIO.read(avatar.inputStream)
//		height = photo.height > height ? height: photo.height
//		width = photo.width > width ? width : photo.width
//
//		Image image = photo.getScaledInstance(width, height, Image.SCALE_DEFAULT)//这个是用来进行图片大小调整的
//
//		BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
//
		def userInfo = userInfoRepository.findOne(userId)
//
//		ByteArrayOutputStream out = new ByteArrayOutputStream()
//		boolean flag = ImageIO.write(tag, 'jpg', out)

		userInfo.avatar = avatar.bytes

		userInfoRepository.save(userInfo)
	}

	/**
	 * 显示头像
	 * @param userId
	 * @param output
	 * @return
	 */
	@RequestMapping(value = 'retrieveAvatar', method = RequestMethod.GET)
	retrieveAvatar(@RequestParam long userId, OutputStream output) {
		def userInfo = userInfoRepository.findOne(userId)
		output.write(userInfo.avatar)
	}

}