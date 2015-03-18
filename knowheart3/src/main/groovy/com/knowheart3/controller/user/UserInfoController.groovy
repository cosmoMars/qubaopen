package com.knowheart3.controller.user

import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang3.time.DateFormatUtils
import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

import com.knowheart3.repository.user.UserInfoRepository
import com.knowheart3.repository.user.UserRepository
import com.knowheart3.service.user.UserInfoService
import com.knowheart3.service.user.UserService
import com.knowheart3.utils.UploadUtils;

import static com.knowheart3.utils.ValidateUtil.*;

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserInfo



@RestController
@RequestMapping('userInfos')
@SessionAttributes('currentUser')
public class UserInfoController extends AbstractBaseController<UserInfo, Long> {
	
	@Autowired
	UserInfoRepository userInfoRepository

	@Autowired
	UserInfoService userInfoService
	
	@Autowired
	UserRepository userRepository
	
	@Autowired
	UserService userService

    @Autowired
    UploadUtils uploadUtils

	@Override
	protected MyRepository<UserInfo, Long> getRepository() {
		userInfoRepository
	}

	/**
	 * 获取个人信息
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = 'retrievePersonalInfo', method = RequestMethod.GET)
	retrievePersonalInfo(@ModelAttribute('currentUser') User user) {

		logger.trace ' -- 获得用户个人信息 -- '

		userInfoService.retrievePersonalInfo(user.id)
	}

	/**
	 * 修改个人信息
	 * @param userId
	 * @return
	 */
	@Override
	@RequestMapping(method = RequestMethod.PUT)
	modify(@RequestBody UserInfo userInfo) {
		
		logger.trace ' -- 获得修改个人信息 -- '
		
		if (userInfo.nickName) {
			if (!validateNormalString(userInfo.nickName.trim())) {
				return '{"success" : "0", "message" : "err103"}'
			}
			def bytes = userInfo.nickName.trim().getBytes('gb2312')
			if (bytes.size() < 2 || bytes.size() > 14) {
				return '{"success" : "0", "message" : "err103"}'
			}
		}
		
		if (userInfo.birthday) {
			if (userInfo.birthday >= DateUtils.parseDate(DateFormatUtils.format(new Date(), 'yyyy-MM-dd'), 'yyyy-MM-dd') || userInfo.birthday <= DateUtils.parseDate('1900', 'yyyy')) {
				return '{"success" : "0", "message" : "err104"}'
			}
		}
		
		userInfoRepository.modify(userInfo)
		'{"success": "1"}'
	}


	/**
	 * 上传头像
	 * @param userId
	 * @param avatar
	 */
	@RequestMapping(value = 'uploadAvatar', method = RequestMethod.POST, consumes = 'multipart/form-data')
	uploadAvatar(@RequestParam(required = false) MultipartFile avatar, @ModelAttribute('currentUser') User user, HttpServletRequest request) {

		logger.trace(' -- 上传头像 -- ')

		if (avatar) {
			def pic = 'pic',
				file = new File("${request.getServletContext().getRealPath('/')}$pic");
			if (!file.exists() && !file.isDirectory()) {
				file.mkdir()
			}

			def filename = "${user.id}_${DateFormatUtils.format(new Date(), 'yyyyMMdd-HHmmss')}.png",
				avatarPath = "${request.getServletContext().getRealPath('/')}pic/$filename"

			println avatarPath

			saveFile(avatar.bytes, avatarPath)

			def userInfo = user.userInfo
			userInfo.avatarPath = "/pic/$filename"
			userInfoRepository.save(userInfo)
			return '{"success": "1"}'
		}
		'{"success": "0", "message" : "err102"}'
	}

	private void saveFile(byte[] bytes, String filename) {
		def fos = new FileOutputStream(filename)
		fos.write(bytes)
		fos.close()
	}

	/**
	 * 修改用户信息 发布
	 * @param userId
	 * @param sharedSina
	 * @param sharedTencent
	 * @param sharedWeChatFriend
	 * @param sharedQQSpace
	 * @param sharedWeChat
	 * @param publicAnswersToChief
	 * @param publicMovementToFriend
	 * @param publicAnswersToFriend
	 * @return
	 */
	@RequestMapping(value = 'modifyUserInfoStatus', method = RequestMethod.POST)
	modifyUserInfoStatus(@RequestParam(required = false) Boolean sharedSina,
		@RequestParam(required = false) Boolean sharedTencent,
		@RequestParam(required = false) Boolean sharedWeChatFriend,
		@RequestParam(required = false) Boolean sharedQQSpace,
		@RequestParam(required = false) Boolean sharedWeChat,
		@RequestParam(required = false) Boolean publicAnswersToChief,
		@RequestParam(required = false) Boolean publicMovementToFriend,
		@RequestParam(required = false) Boolean publicAnswersToFriend,
		@RequestParam(required = false) Boolean saveFlow,
		@ModelAttribute('currentUser') User user) {
			def userInfo = userInfoRepository.findOne(user.id)
			if (sharedSina != null) {
				userInfo.sharedSina = sharedSina
			}
			if (sharedTencent != null) {
				userInfo.sharedTencent = sharedTencent
			}
			if (sharedWeChatFriend != null) {
				userInfo.sharedWeChatFriend = sharedWeChatFriend
			}
			if (sharedQQSpace != null) {
				userInfo.sharedQQSpace = sharedQQSpace
			}
			if (sharedWeChat != null) {
				userInfo.sharedWeChat = sharedWeChat
			}
			if (publicAnswersToChief != null) {
				userInfo.publicAnswersToChief = publicAnswersToChief
			}
			if (publicMovementToFriend != null) {
				userInfo.publicMovementToFriend = publicMovementToFriend
			}
			if (publicAnswersToFriend != null) {
				userInfo.publicAnswersToFriend = publicAnswersToFriend
			}
			if (saveFlow != null) {
				userInfo.saveFlow = saveFlow
			}
			userInfoRepository.save(userInfo)
			'{"success" : "1"}'
		}


		/**
		 * 保存用户年龄和性别
		 * @param age
		 * @param sex
		 * @return
		 */
		@RequestMapping(value = 'submitAgeAndSex', method = RequestMethod.POST)		
		submitAgeAndSex(@RequestParam(required =false) Integer age, @RequestParam(required = false) Integer sex, @ModelAttribute('currentUser') User user) {
			
			def userInfo = userInfoRepository.findOne(user.id)
			if (age != null) {
//				if (age < 12 || age > 99) {
//					return '{"success" : "0", "message" : "err017"}'
//				}
				def c = Calendar.getInstance()
				c.setTime new Date()
				def year = c.get(Calendar.YEAR) - age

				userInfo.birthday = Date.parse('yyyy-MM-dd', "$year-01-01")
	
			}
			if (sex != null) {
				switch (sex) {
					case 0 :
						userInfo.sex = UserInfo.Sex.MALE
						break
					case 1 :
						userInfo.sex = UserInfo.Sex.FEMALE
						break
					case 2 :
						userInfo.sex = UserInfo.Sex.OTHER
						break
				}
			}
			userInfoRepository.save(userInfo)
			'{"success" : "1"}'
		}
		
		/**
		 * 获取用户首页数据，心情、性格解析度、心理指数
		 * @param user
		 * @return
		 */
		@RequestMapping(value = 'getIndexInfo', method = RequestMethod.GET)
		getIndexInfo(@ModelAttribute('currentUser') User user) {
			if (!user) {
				return '{"success" : "0", "message" : "err000"}'
			}
			userService.getIndexInfo(user)
		}
	
		@RequestMapping(value = 'test', method = RequestMethod.GET)
		test(Pageable pageable) {
			userRepository.findAllUsers(pageable)
		}
		
		
		/**
		 * @param user
		 * @return
		 * 确认评估
		 */
		@RequestMapping(value = 'confirmUsertEvaluate', method = RequestMethod.POST)
		confirmUsertEvaluate(@ModelAttribute('currentUser') User user) {
			
			def userInfo = userInfoRepository.findOne(user.id)
			
			userInfo.evaluate = true
			
			userInfoRepository.save(userInfo)
			'{"success" : "1"}'
		}

    /**
     * 上传用户头像
     * @param avatar
     * @param user
     * @return
     */
    @RequestMapping(value = 'uploadUserAvatar', method = RequestMethod.POST, consumes = 'multipart/form-data')
    uploadUserAvatar(@RequestParam(required = false) MultipartFile avatar, @ModelAttribute('currentUser') User user) {

        logger.trace(' -- 上传头像 -- ')

        if (avatar) {
            def userInfo = userInfoRepository.findOne(user.id)
            def url = uploadUtils.uploadUser(user.id, avatar)

            userInfo.avatarPath = url
            userInfoRepository.save(userInfo)

            return '{"success": "1"}'
        }
        '{"success": "0", "message" : "err102"}'
    }
}