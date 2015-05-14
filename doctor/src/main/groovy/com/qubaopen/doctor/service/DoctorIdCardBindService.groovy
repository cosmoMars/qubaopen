package com.qubaopen.doctor.service;

import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.qubaopen.doctor.repository.doctor.DoctorIdCardBindRepository
import com.qubaopen.doctor.repository.doctor.DoctorIdCardLogRepository
import com.qubaopen.doctor.repository.doctor.DoctorInfoRepository
import com.qubaopen.doctor.repository.user.UserIdCardRepository
import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.doctor.DoctorIdCardBind
import com.qubaopen.survey.entity.doctor.DoctorIdCardLog
import com.qubaopen.survey.entity.doctor.DoctorInfo
import com.qubaopen.survey.entity.user.UserIDCard


@Service
public class DoctorIdCardBindService {

	@Autowired
	UserIdCardRepository userIdCardRepository
	
	@Autowired
	DoctorIdCardBindRepository doctorIdCardBindRepository
	
	@Autowired
	DoctorIdCardLogRepository doctorIdCardLogRepository
	
	@Autowired
	IdentityValidationService identityValidationService
	
	@Autowired
	DoctorInfoRepository doctorInfoRepository
	
	/**
	 * 提交身份证验证，每月每个账号只能认证3次，修改1次
	 * @param idCard
	 * @param name
	 * @param doctor
	 * @return
	 */
	@Transactional
	def submitUserIdCard(String idCard, String name, Doctor doctor) {
		
		def doctorIdCardBind = doctorIdCardBindRepository.findOne(doctor.id),
			userIdCard = userIdCardRepository.findByIDCard(idCard),
			existLogs = doctorIdCardLogRepository.findCurrentMonthIdCardLogByUser(doctor),
			existIdCardBind = doctorIdCardBindRepository.findByUserIDCard(userIdCard)
			
		if (existIdCardBind && existIdCardBind.doctor != doctor) {
			return '{"success" : "0", "message" : "err203"}' // 身份证已被他人绑定
		}
			
		if (existLogs.size() >= 3) {
			return '{"success" : "0", "message" : "err201"}' // 每月绑定次数达到上线
		}
		if (!doctorIdCardBind) { // 第一次绑定
			if (userIdCard) {
				def doctorIdCardLog = new DoctorIdCardLog(
					doctor : doctor,
					idCard : idCard,
					name : name,
					status : 'local'
				)
//				userIdCard.name = name
				if (userIdCard.name != name) {
					return '{"success" : "0", "message" : "err205"}'
				}
				def di = doctorInfoRepository.findOne(doctor.id)
				def infoMap = retrieveBirthdayAndSex(idCard),
				birthday = infoMap['birthday'],
				sex = infoMap['sex'] as int
				di.birthday = DateUtils.parseDate(birthday, "yyyyMMdd")
				if (1 == sex) {
					di.sex = DoctorInfo.Sex.Male
				}
				if (0 == sex) {
					di.sex = DoctorInfo.Sex.Female
				}
				di.name = name
				doctorInfoRepository.save(di)
				doctorIdCardLogRepository.save(doctorIdCardLog)
			} else {
				def result,
					map = identityValidationService.identityValidation(idCard, name),
					mapStatus = map.get('status')
				
				if ('0' == mapStatus) {
					result = map.get('compStatus')
				} else {
					result = mapStatus
				}
				def doctorIdCardLog = new DoctorIdCardLog(
					doctor : doctor,
					idCard : idCard,
					name : name,
					status : result
				)
				doctorIdCardLogRepository.save(doctorIdCardLog)
				if ('0' != mapStatus) {
					return '{"success" : "0", "message" : "err200"}' // 绑定失败
				}
				if ('0' == mapStatus && '1' == result) {
					return '{"success" : "0", "message" : "err205"}'
				}
				if ('0' == mapStatus && '2' == result) {
					return '{"success" : "0", "message" : "err206"}'
				}
			
				if ('0' == mapStatus && '3' == result) {
					userIdCard = new UserIDCard(
						IDCard : idCard,
						name : name
					)
					def di = doctorInfoRepository.findOne(doctor.id)
					def infoMap = retrieveBirthdayAndSex(idCard),
					birthday = infoMap['birthday'],
					sex = infoMap['sex'] as int
					di.birthday = DateUtils.parseDate(birthday, "yyyyMMdd")
					if (1 == sex) {
						di.sex = DoctorInfo.Sex.Male
					}
					if (0 == sex) {
						di.sex = DoctorInfo.Sex.Female
					}
					di.name = name
					doctorInfoRepository.save(di)
				}
			}
			if (userIdCard) {
				doctorIdCardBind = new DoctorIdCardBind(
					id : doctor.id,
					userIDCard : userIdCard
				)
				userIdCardRepository.save(userIdCard)
				doctorIdCardBindRepository.save(doctorIdCardBind)
				return '{"success" : "1", "message" : "认证成功"}'
			}/* else {
				return '{"success" : "0", "message" : "err200"}'
			}*/
		} else {
			def correctLog = existLogs.find {
				it.status == 'local' || it.status == '2' || it.status == '3'
			}
			if (correctLog) {
				def doctorIdCardLog = new DoctorIdCardLog(
					doctor : doctor,
					idCard : idCard,
					name : name,
					status : 'used'
				)
				doctorIdCardLogRepository.save(doctorIdCardLog)
				return '{"success" : "0", "message" : "err204"}' // 本月已成功绑定，无法修改
			}
				
			if (userIdCard) {
				def dcotorIdCardLog = new DoctorIdCardLog(
					doctor : doctor,
					idCard : idCard,
					name : name,
					status : 'local'
				)
//				userIdCard.name = name
				if (userIdCard.name != name) {
					return '{"success" : "0", "message" : "err205"}'
				}
				userIdCardRepository.save(userIdCard)
				doctorIdCardLogRepository.save(dcotorIdCardLog)
				
				def di = doctorInfoRepository.findOne(doctor.id)
				def infoMap = retrieveBirthdayAndSex(idCard),
				birthday = infoMap['birthday'],
				sex = infoMap['sex'] as int
				di.birthday = DateUtils.parseDate(birthday, "yyyyMMdd")
				if (1 == sex) {
					di.sex = DoctorInfo.Sex.Male
				}
				if (0 == sex) {
					di.sex = DoctorInfo.Sex.Female
				}
				di.name = name
				doctorInfoRepository.save(di)
				
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
					
				def doctorIdCardLog = new DoctorIdCardLog(
					doctor : doctor,
					idCard : idCard,
					name : name,
					status : result
				)
				doctorIdCardLogRepository.save(doctorIdCardLog)
				if ('0' != mapStatus) {
					return '{"success" : "0", "message" : "err200"}' // 绑定失败
				}
				if ('0' == mapStatus && '1' == result) {
					return '{"success" : "0", "message" : "err205"}'
				}
				if ('0' == mapStatus && '2' == result) {
					return '{"success" : "0", "message" : "err206"}'
				}
				if ('0' == mapStatus && '3' == result) {
					userIdCard = new UserIDCard(
						IDCard : idCard,
						name : name
					)
					def di = doctorInfoRepository.findOne(doctor.id)
					def infoMap = retrieveBirthdayAndSex(idCard),
					birthday = infoMap['birthday'],
					sex = infoMap['sex'] as int
					di.birthday = DateUtils.parseDate(birthday, "yyyyMMdd")
					if (1 == sex) {
						di.sex = DoctorInfo.Sex.Male
					}
					if (0 == sex) {
						di.sex = DoctorInfo.Sex.Female
					}
					di.name = name
					doctorInfoRepository.save(di)
					
					userIdCardRepository.save(userIdCard)
				}
			}
			if (userIdCard) {
				doctorIdCardBind.userIDCard = userIdCard
				doctorIdCardBindRepository.save(doctorIdCardBind)
				return '{"success" : "1", "message" : "认证成功"}'
			} /*else {
				return '{"success" : "0", "message" : "err200"}'
			}*/
		}
	}
	
	def retrieveBirthdayAndSex(String idCard) {
		
		def birthday = idCard.substring(6, 14)
		
		String last2Value = idCard.substring(idCard.length() - 2, idCard.length() - 1);
		def sex = Integer.parseInt(last2Value) % 2
		
		[birthday : birthday, sex : sex]
	}
}
