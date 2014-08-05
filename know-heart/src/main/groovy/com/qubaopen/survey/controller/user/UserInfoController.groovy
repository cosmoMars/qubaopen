package com.qubaopen.survey.controller.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.user.UserInfo
import com.qubaopen.survey.repository.user.UserInfoRepository
import com.qubaopen.survey.service.user.UserInfoService

@RestController
@RequestMapping("userInfos")
public class UserInfoController extends AbstractBaseController<UserInfo, Long> {

	@Autowired
	UserInfoRepository userInfoRepository

	@Autowired
	UserInfoService userInfoService

	@Override
	protected MyRepository<UserInfo, Long> getRepository() {
		userInfoRepository
	}

	/**
	 * 获取个人信息
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = 'retrievePersonalInfo/{userId}', method = RequestMethod.GET)
	retrievePersonalInfo(@PathVariable long userId) {

		logger.trace ' -- 获得用户个人信息 -- '

		userInfoService.retrievePersonalInfo(userId)
	}

	/**
	 * 修改个人信息
	 * @param userId
	 * @return
	 */
	@Override
	@RequestMapping(method = RequestMethod.PUT)
	modify(@RequestBody UserInfo userInfo) {

		try {
			userInfoRepository.save(userInfo)
			'{"success": 1}'
		} catch (Exception e) {
//			e.printStackTrace()
			'{"success": 0}'
		}



	}

	/**
	 * 上传头像
	 * @param userId
	 * @param avatar
	 */
	@RequestMapping(value = 'uploadAvatar', method = RequestMethod.POST, consumes = 'multipart/form-data')
	uploadAvatar(@RequestParam long userId, @RequestParam MultipartFile avatar) {

		logger.trace(' -- 上传头像 -- ')

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

		return '{"success": 1}'
	}

	/**
	 * 显示头像
	 * @param userId
	 * @param output
	 * @return
	 */
	@RequestMapping(value = 'retrieveAvatar', method = RequestMethod.GET)
	retrieveAvatar(@RequestParam long userId, OutputStream output) {

		logger.trace(' -- 显示头像 -- ')

		def userInfo = userInfoRepository.findOne(userId)
		output.write(userInfo.avatar)
	}

}