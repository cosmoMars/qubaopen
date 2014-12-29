package com.qubaopen.doctor.controller.doctor;

import static com.qubaopen.doctor.utils.ValidateUtil.*

import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang3.time.DateFormatUtils
import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes
import org.springframework.web.multipart.MultipartFile

import com.fasterxml.jackson.databind.node.ArrayNode
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.doctor.repository.doctor.DoctorAddressRepository
import com.qubaopen.doctor.repository.doctor.DoctorIdCardBindRepository
import com.qubaopen.doctor.repository.doctor.DoctorInfoRepository
import com.qubaopen.doctor.repository.doctor.DoctorRepository
import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.doctor.DoctorInfo

@RestController
@RequestMapping('doctorInfo')
@SessionAttributes('currentDoctor')
public class DoctorInfoController extends AbstractBaseController<DoctorInfo, Long> {

	@Autowired
	DoctorRepository doctorRepository
	
	@Autowired
	DoctorInfoRepository doctorInfoRepository
	
	@Autowired
	DoctorIdCardBindRepository doctorIdCardBindRepository
	
	@Autowired
	DoctorAddressRepository doctorAddressRepository
	
	
	@Override
	protected MyRepository<DoctorInfo, Long> getRepository() {
		return doctorInfoRepository;
	}
	
	
	/**
	 * @param doctor
	 * @return
	 * 获取医师信息
	 */
	@RequestMapping(value = 'retrieveDoctorInfo', method = RequestMethod.GET)
	retrieveDoctorInfo(@ModelAttribute('currentDoctor') Doctor doctor) {
		
		logger.trace ' -- 获得用户个人信息 -- '
		
		def doctorInfo = doctorInfoRepository.findOne(doctor.id),
			doctorIdCardBind = doctorIdCardBindRepository.findOne(doctor.id),
			address = doctorAddressRepository.findByDoctorAndUsed(doctor, true)
		def infoTime = doctorInfo?.bookingTime, timeData = []
		if (infoTime) {
			def times = infoTime.split(',')
			times.eachWithIndex { value, index ->
				for (i in 0..value.length() - 1) {
					if ('1' == value[i] || '1'.equals(value)) {
						timeData << [
							'dayId': index + 1,
							'startTime' : DateFormatUtils.format(DateUtils.parseDate("$i:00", 'HH:mm'), 'HH:mm'),
							'endTime' : DateFormatUtils.format(DateUtils.parseDate("${i+1}:00", 'HH:mm'), 'HH:mm')
						]
					}
				}
			}
		}

		[
			'success' : '1',
			'doctorId' : doctor.id,
			'phone' : doctorInfo?.phone,
			'name' : doctorIdCardBind?.userIDCard?.name,
			'infoName' : doctorInfo?.name,
			'contactPhone' : doctorInfo?.phone,
			'sex' : doctorInfo?.sex?.ordinal(),
			'birthday' : doctorInfo?.birthday,
			'experience' : doctorInfo?.experience,
			'field' : doctorInfo?.field,
			'qq' : doctorInfo?.qq,
			'faceToFace' : doctorInfo?.faceToFace,
			'video' : doctorInfo?.video,
			'targetUser' : doctorInfo?.targetUser,
			'genre' : doctorInfo?.genre,
			'time' : doctorInfo?.bookingTime,
			'introduce' : doctorInfo?.introduce,
			'quick' : doctorInfo.quick,
			'email' : doctorInfo?.doctor?.email,
			'address' : address?.address,
			'idCard' : doctorIdCardBind?.userIDCard?.IDCard,
			'recordPath' : doctorInfo?.recordPath, 
			'avatarPath' : doctorInfo?.avatarPath,
			'loginStatus' : doctorInfo?.loginStatus?.ordinal(),
			'refauslReason' : doctorInfo?.refusalReason,
			'commentConsult' : doctorInfo?.commentConsult,
			'phoneConsult' : doctorInfo?.phoneConsult,
			'timeData' : timeData
		]
	}
	
	/**
	 * @param name
	 * @param sex
	 * @param birthday
	 * @param experience
	 * @param field
	 * @param qq
	 * @param consultType
	 * @param targetUser
	 * @param genre
	 * @param time
	 * @param quick
	 * @param introduce
	 * @param avatar
	 * @param record
	 * @param doctor
	 * @param request
	 * @return
	 * 修改医师信息
	 */
	@Transactional
	@RequestMapping(value = 'modifyDoctorInfo', method = RequestMethod.POST, consumes = 'multipart/form-data')
	modifyDoctorInfo(@RequestParam(required = false) String infoName,
		@RequestParam(required = false) String contactPhone,
		@RequestParam(required = false) String email,
		@RequestParam(required = false) Integer sex,
		@RequestParam(required = false) String birthday,
		@RequestParam(required = false) String experience,
		@RequestParam(required = false) String field,
		@RequestParam(required = false) String qq,
		@RequestParam(required = false) Boolean faceToFace,
		@RequestParam(required = false) Boolean video,
		@RequestParam(required = false) String targetUser,
		@RequestParam(required = false) String genre,
		@RequestParam(required = false) String bookingTime,
		@RequestParam(required = false) Boolean quick,
		@RequestParam(required = false) String introduce,
		@RequestParam(required = false) Boolean commentConsult,
		@RequestParam(required = false) Boolean phoneConsult,
		@RequestParam(required = false) String address,
		@RequestParam(required = false) MultipartFile avatar,
		@RequestParam(required = false) MultipartFile record,
		@RequestParam(required = false) String times,
		@RequestParam(required = false) String json,
		@ModelAttribute('currentDoctor') Doctor doctor,
		HttpServletRequest request
		) {
		
		def doctorInfo = doctorInfoRepository.findOne(doctor.id)
		if (email) {
			if (!validateEmail(email)) {
				return '{"success" : "0", "message": "err005"}'
			}
			doctor.email = email
		}
		
		if (contactPhone) {
			if (!validatePhone(contactPhone)) {
				return '{"success" : "0", "message": "err003"}'
			}
			doctorInfo.phone = contactPhone
		}
		
		if (infoName) {
			doctorInfo.name = infoName
		}
		if (sex != null) {
			doctorInfo.sex = DoctorInfo.Sex.values()[sex]
		}
		if (birthday) {
			doctorInfo.birthday = DateUtils.parseDate(birthday, 'yyyy-MM-dd')
		}
		if (experience) {
			doctorInfo.experience = experience
		}
		if (field) {
			doctorInfo.field = field
		}
		if (qq) {
			doctorInfo.qq = qq
		}
		if (faceToFace != null) {
			doctorInfo.faceToFace = faceToFace
		}
		if (video != null) {
			doctorInfo.video = video
		}
		if (targetUser) {
			doctorInfo.targetUser = targetUser
		}
		if (genre) {
			doctorInfo.genre = genre
		}
		if (bookingTime) {
			doctorInfo.bookingTime = bookingTime
		}
		if (quick) {
			doctorInfo.quick = quick
		}
		if (introduce) {
			doctorInfo.introduce = introduce
		}
		if (commentConsult != null) {
			doctorInfo.commentConsult = commentConsult
		}
		if (phoneConsult != null) {
			doctorInfo.phoneConsult = phoneConsult
		}
		if (address != null) {
			doctorInfo.address = address
		}
		
		if (times != null) {
			println doctorInfo.bookingTime
			def resultTime = doctorInfo.bookingTime.split(','),
				tempTimes = times.split('&')
			tempTimes.each { t ->
				def singleTime = t.split('#'),
					day = singleTime[0] as int,
					type = singleTime[1],
					dayTime = singleTime[2].split(',')
				dayTime.each {
					def index = it as int
					if ('0' == type || '0'.equals(type)) {
						resultTime[day - 1] = resultTime[day - 1].substring(0, index) + '0' + resultTime[day - 1].substring(index + 1)
					} else if ('1' == type || '1'.equals(type)) {
						resultTime[day - 1] = resultTime[day - 1].substring(0, index) + '1' + resultTime[day - 1].substring(index + 1)
					}
				}
			}
			doctorInfo.bookingTime = resultTime.join(',')
		}
		
		if (json != null) {
			def resultModel = doctorInfo.bookingTime.split(',')
			def jsons = json.split('=')
			
			def jsonNodes = objectMapper.readTree(jsons[1]),
				bookingModels = []
				
			jsonNodes.each {
				if (it.get('type') == null) {
					return '{"success" : "0", "message" : "err905"}'// 没有预约时间类型
				}
				if (it.get('day') == null) {
					return '{"success" : "0", "message" : "err910"}'// 没有指定时间
				}
				def index = it.get('day').asInt(),
					type = it.get('type').asInt(),
					hours = (ArrayNode)it.path('times')
					
				if (type == 0) {
					hours.each { h ->
						def idx = h.asInt()
						resultModel[index - 1] = resultModel[index - 1].substring(0, idx) + '0' + resultModel[index - 1].substring(idx + 1) 
					}
				} else if (type == 1) {
					hours.each { h ->
						def idx = h.asInt()
						resultModel[index - 1] = resultModel[index - 1].substring(0, idx) + '1' + resultModel[index - 1].substring(idx + 1)
					}
				}
			}
			doctorInfo.bookingTime = resultModel.join(',')
		}
		
		if (avatar) {
			def doctorDir = 'doctorDir'
			def file = new File("${request.getServletContext().getRealPath('/')}$doctorDir");
			if (!file.exists() && !file.isDirectory()) {
				file.mkdir()
			}
			def fileName = "${doctor.id}_${DateFormatUtils.format(new Date(), 'yyyyMMdd-HHmmss')}.png",
				avatarPath = "${request.getServletContext().getRealPath('/')}$doctorDir/$fileName"
			
			saveFile(avatar.bytes, avatarPath)
			doctorInfo.avatarPath = "/$doctorDir/$fileName"
		}
		if (record) {
			def recordDir = 'recordDir'
			def file = new File("${request.getServletContext().getRealPath('/')}$recordDir");
			if (!file.exists() && !file.isDirectory()) {
				file.mkdir()
			}
			def fileName = "${doctor.id}_${DateFormatUtils.format(new Date(), 'yyyyMMdd-HHmmss')}.png",
				recordPath = "${request.getServletContext().getRealPath('/')}$recordDir/$fileName"
			
			saveFile(record.bytes, recordPath)
			doctorInfo.recordPath = "/$recordDir/$fileName"
		}
		doctorRepository.save(doctor)
		doctorInfoRepository.save(doctorInfo)
		'{"success": "1"}'
	}
		
	
	def saveFile(byte[] bytes, String fileName) {
		def fos = new FileOutputStream(fileName)
		fos.write(bytes)
		fos.close()
	}
}
