package com.qubaopen.survey.service.user;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.entity.user.UserIDCard;
import com.qubaopen.survey.entity.user.UserIDCardBind
import com.qubaopen.survey.entity.user.UserIDCardLog
import com.qubaopen.survey.repository.user.UserIDCardBindRepository;
import com.qubaopen.survey.repository.user.UserIDCardLogRepository
import com.qubaopen.survey.repository.user.UserIDCardRepository
import com.qubaopen.survey.service.IdentityValidationService

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
			userIdCard = userIDCardRepository.findByIDCardAndName(idCard, name),
			existLogs = userIDCardLogRepository.findCurrentMonthIdCardLogByUser(user),
			existIdCardBind = userIDCardBindRepository.findByUserIDCard(userIdCard)
			
		if (existIdCardBind && existIdCardBind.user != user) {
			return '{"success" : "0", "message" : "err100003"}' // 身份证已被他人绑定
		}
			
		if (existLogs.size() >= 3) {
			return '{"success" : "0", "message" : "err100001"}' // 每月绑定次数达到上线
		}
		if (!userIdCardBind) { // 第一次绑定
			if (userIdCard) {
				def userIdCardLog = new UserIDCardLog(
					user : user,
					IDCard : idCard,
					name : name,
					status : 'local'
				)
				userIDCardLogRepository.save(userIdCardLog)
			} else {
				def result = identityValidationService.identityValidation(idCard, name)
//				def result = '0'
				def userIdCardLog = new UserIDCardLog(
					user : user,
					IDCard : idCard,
					name : name,
					status : result
				)
				userIDCardLogRepository.save(userIdCardLog)
				
				if (result != '0') {
					return '{"success" : "0", "message" : "err10000"}' // 绑定失败
				}
				if (result == '0') {
					userIdCard = new UserIDCard(
						IDCard : idCard,
						name : name
					)
					userIDCardRepository.save(userIdCard)
				}
			}
			if (userIdCard) {
				userIdCardBind = new UserIDCardBind(
					id : user.id,
					userIDCard : userIdCard
				)
				userIDCardBindRepository.save(userIdCardBind)
				return '{"success" : "1", "message" : "认证成功"}'
			} else {
				return '{"success" : "0", "message" : "err10000"}'
			}
		} else {
			def correctLog = existLogs.find {
				it.status == '0' || it.status == 'local'
			}
			if (correctLog) {
				def userIdCardLog = new UserIDCardLog(
					user : user,
					IDCard : idCard,
					name : name,
					status : 'used'
				)
				userIDCardLogRepository.save(userIdCardLog)
				return '{"success" : "0", "message" : "err100002"}' // 每月已修改过一次
			}
				
			if (userIdCard) {
				def userIdCardLog = new UserIDCardLog(
					user : user,
					IDCard : idCard,
					name : name,
					status : 'local'
				)
				return '{"success" : "1", "message" : "认证成功"}'
			} else {
				def result = identityValidationService.identityValidation(idCard, name)
//				def result = '0'
				def userIdCardLog = new UserIDCardLog(
					user : user,
					IDCard : idCard,
					name : name,
					status : result
				)
				userIDCardLogRepository.save(userIdCardLog)
				if (result != '0') {
					return '{"success" : "0", "message" : "err10000"}' // 绑定失败
				}
				if (result == '0') {
					userIdCard = new UserIDCard(
						IDCard : idCard,
						name : name
					)
					userIDCardRepository.save(userIdCard)
				}
			}
			if (userIdCard) {
				userIdCardBind.userIDCard = userIdCard
				userIDCardBindRepository.save(userIdCardBind)
				return '{"success" : "1", "message" : "认证成功"}'
			} else {
				return '{"success" : "0", "message" : "err10000"}'
			}
		}
	}
}
