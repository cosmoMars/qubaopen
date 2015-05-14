package com.knowheart3.service.user;

import org.apache.commons.lang3.time.DateUtils
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.knowheart3.repository.user.UserIDCardBindRepository
import com.knowheart3.repository.user.UserIDCardLogRepository
import com.knowheart3.repository.user.UserIDCardRepository
import com.knowheart3.repository.user.UserInfoRepository
import com.knowheart3.service.IdentityValidationService
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserIDCard
import com.qubaopen.survey.entity.user.UserIDCardBind
import com.qubaopen.survey.entity.user.UserIDCardLog
import com.qubaopen.survey.entity.user.UserInfo

@Service
public class UserIDCardBindService {
	
	@Autowired
	UserIDCardRepository userIDCardRepository
	
	@Autowired
	IdentityValidationService identityValidationService
	
	@Autowired
	UserIDCardLogRepository userIDCardLogRepository
	
	@Autowired
	UserIDCardBindRepository userIDCardBindRepository
	
	@Autowired
	UserInfoRepository userInfoRepository

	/**
	 * 提交身份证验证，每月每个账号只能认证3次，修改1次
	 * @param idCard
	 * @param name
	 * @param user
	 * @return
	 */
	@Transactional
	def submitUserIdCard(String idCard, String name, User user) {
		
		def userIdCardBind = userIDCardBindRepository.findOne(user.id),
			userIdCard = userIDCardRepository.findByIDCard(idCard),
			existLogs = userIDCardLogRepository.findCurrentMonthIdCardLogByUser(user),
			existIdCardBind = userIDCardBindRepository.findByUserIDCard(userIdCard)
			
		if (existIdCardBind && existIdCardBind.user != user) {
			return '{"success" : "0", "message" : "err203"}' // 身份证已被他人绑定
		}
			
		if (existLogs.size() >= 3) {
			return '{"success" : "0", "message" : "err201"}' // 每月绑定次数达到上线
		}
		if (!userIdCardBind) { // 第一次绑定
			if (userIdCard) {
				def userIdCardLog = new UserIDCardLog(
					user : user,
					IDCard : idCard,
					name : name,
					status : 'local'
				)
//				userIdCard.name = name
				if (userIdCard.name != name) {
					return '{"success" : "0", "message" : "err200"}'
				}
				userIDCardLogRepository.save(userIdCardLog)
				
				def infoMap = retrieveBirthdayAndSex(idCard),
				birthday = infoMap['birthday'],
				sex = infoMap['sex'] as int,
				userInfo = userInfoRepository.findOne(user.id)
				userInfo.birthday = DateUtils.parseDate(birthday, "yyyyMMdd")
				if (1 == sex) {
					userInfo.sex = UserInfo.Sex.MALE
				}
				if (0 == sex) {
					userInfo.sex = UserInfo.Sex.FEMALE
				}
				
				userInfoRepository.save(userInfo)
			} else {
				def result,
					map = identityValidationService.identityValidation(idCard, name),
					mapStatus = map.get('status')
				
				if ('0' == mapStatus) {
					result = map.get('compStatus')
				} else {
					result = mapStatus
				}
				def userIdCardLog = new UserIDCardLog(
					user : user,
					IDCard : idCard,
					name : name,
					status : result
				)
				userIDCardLogRepository.save(userIdCardLog)
				if ('0' != mapStatus) {
					return '{"success" : "0", "message" : "err200"}' // 绑定失败
				}
				if ('0' == mapStatus && '1' == result) {
					return '{"success" : "0", "message" : "err205"}'
				}
				if ('0' == mapStatus && '2' == result) {
					return '{"success" : "0", "message" : "err200"}'
				}
				if ('0' == mapStatus && '3' == result) {
					userIdCard = new UserIDCard(
						IDCard : idCard,
						name : name
					)
					def infoMap = retrieveBirthdayAndSex(idCard),
					birthday = infoMap['birthday'],
					sex = infoMap['sex'] as int,
					userInfo = userInfoRepository.findOne(user.id)
					userInfo.birthday = DateUtils.parseDate(birthday, "yyyyMMdd")
					if (1 == sex) {
					userInfo.sex = UserInfo.Sex.MALE
					}
					if (0 == sex) {
						userInfo.sex = UserInfo.Sex.FEMALE
					}
					userInfoRepository.save(userInfo)
				}
			}
			if (userIdCard) {
				userIdCardBind = new UserIDCardBind(
					id : user.id,
					userIDCard : userIdCard
				)
				userIDCardRepository.save(userIdCard)
				userIDCardBindRepository.save(userIdCardBind)
				return '{"success" : "1", "message" : "认证成功"}'
			} else {
				return '{"success" : "0", "message" : "err200"}'
			}
		} else {
			def correctLog = existLogs.find {
				it.status == 'local' || it.status == '2' || it.status == '3'
			}
			if (correctLog) {
				def userIdCardLog = new UserIDCardLog(
					user : user,
					IDCard : idCard,
					name : name,
					status : 'used'
				)
				userIDCardLogRepository.save(userIdCardLog)
				return '{"success" : "0", "message" : "err204"}' // 本月已成功绑定，无法修改
			}
				
			if (userIdCard) {
				def userIdCardLog = new UserIDCardLog(
					user : user,
					IDCard : idCard,
					name : name,
					status : 'local'
				)
//				userIdCard.name = name
				if (userIdCard.name != name) {
					return '{"success" : "0", "message" : "err200"}'
				}
				userIDCardRepository.save(userIdCard)
				userIDCardLogRepository.save(userIdCardLog)
				def infoMap = retrieveBirthdayAndSex(idCard),
				birthday = infoMap['birthday'],
				sex = infoMap['sex'] as int,
				userInfo = userInfoRepository.findOne(user.id)
				userInfo.birthday = DateUtils.parseDate(birthday, "yyyyMMdd")
				if (1 == sex) {
					userInfo.sex = UserInfo.Sex.MALE
				}
				if (0 == sex) {
					userInfo.sex = UserInfo.Sex.FEMALE
				}
				userInfoRepository.save(userInfo)
				return '{"success" : "1", "message" : "认证成功"}'
			} else {
				def result,
					map = identityValidationService.identityValidation(idCard, name),
					mapStatus = map.get('status')
				if ('0' == mapStatus) {
					result = map.get('compStatus')
				} else {
					result = mapStatus
				}
					
				def userIdCardLog = new UserIDCardLog(
					user : user,
					IDCard : idCard,
					name : name,
					status : result
				)
				userIDCardLogRepository.save(userIdCardLog)
				if ('0' != mapStatus) {
					return '{"success" : "0", "message" : "err200"}' // 绑定失败
				}
				if ('0' == mapStatus && '1' == result) {
					return '{"success" : "0", "message" : "err205"}'
				}
				if ('0' == mapStatus && '2' == result) {
					return '{"success" : "0", "message" : "err200"}'
				}
				if ('0' == mapStatus && '3' == result) {
					userIdCard = new UserIDCard(
						IDCard : idCard,
						name : name
					)
					def infoMap = retrieveBirthdayAndSex(idCard),
					birthday = infoMap['birthday'],
					sex = infoMap['sex'] as int,
					userInfo = userInfoRepository.findOne(user.id)
					userInfo.birthday = DateUtils.parseDate(birthday, "yyyyMMdd")
					if (1 == sex) {
						userInfo.sex = UserInfo.Sex.MALE
					}
					if (0 == sex) {
						userInfo.sex = UserInfo.Sex.FEMALE
					}
					userInfoRepository.save(userInfo)
					userIDCardRepository.save(userIdCard)
				}
			}
			if (userIdCard) {
				userIdCardBind.userIDCard = userIdCard
				userIDCardBindRepository.save(userIdCardBind)
				return '{"success" : "1", "message" : "认证成功"}'
			} else {
				return '{"success" : "0", "message" : "err200"}'
			}
		}
	}
	
	def calculateAgeByIdCard(User user) {
		def userIDCardBind = userIDCardBindRepository.findOne(user.id)
		if (!userIDCardBind) {
			return null
		}
		def idCard = userIDCardBind.userIDCard.IDCard,
			age = Integer.valueOf(DateTime.now().year) - Integer.valueOf(idCard.substring(6, 10))
		
		String last2Value = idCard.substring(idCard.length() - 2, idCard.length() - 1);
		def sex = Integer.parseInt(last2Value) % 2
//		if (lastValue.trim().toLowerCase().equals('x') || lastValue.trim().toLowerCase().equals('e')) {
//			sex = 0
//		} else {
//			sex = Integer.parseInt(lastValue) % 2
//		}
		[age : age, sex : sex]
	}
	
	def retrieveBirthdayAndSex(String idCard) {
		
		def birthday = idCard.substring(6, 14)
		
		String last2Value = idCard.substring(idCard.length() - 2, idCard.length() - 1);
		def sex = Integer.parseInt(last2Value) % 2
		
		[birthday : birthday, sex : sex]
	}
}
