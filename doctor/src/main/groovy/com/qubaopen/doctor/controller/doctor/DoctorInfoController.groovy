package com.qubaopen.doctor.controller.doctor;

import java.net.Authenticator.RequestorType;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.doctor.repository.doctor.DoctorAddressRepository;
import com.qubaopen.doctor.repository.doctor.DoctorIdCardBindRepository;
import com.qubaopen.doctor.repository.doctor.DoctorInfoRepository;
import com.qubaopen.doctor.repository.doctor.DoctorRepository;

import static com.qubaopen.doctor.utils.ValidateUtil.*;

import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.doctor.DoctorInfo
import com.qubaopen.survey.entity.user.UserInfo;

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

		[
			'success' : '1',
			'doctorId' : doctor.id,
			'name' : doctorIdCardBind?.userIDCard?.name,
			'infoName' : doctorInfo?.name,
			'contactPhone' : doctorInfo?.phone,
			'sex' : doctorInfo?.sex?.ordinal(),
			'birthday' : doctorInfo?.birthday,
			'experience' : doctorInfo?.experience,
			'field' : doctorInfo?.field,
			'qq' : doctorInfo?.qq,
			'consultType' : doctorInfo?.consultType?.ordinal(),
			'targetUser' : doctorInfo?.targetUser,
			'genre' : doctorInfo?.genre,
			'time' : doctorInfo?.time,
			'introduce' : doctorInfo?.introduce,
			'quick' : doctorInfo.quick,
			'email' : doctorInfo?.doctor?.email,
			'address' : address?.address,
			'IdCard' : doctorIdCardBind?.userIDCard?.IDCard,
			'recordPath' : doctorInfo?.recordPath, 
			'avatarPath' : doctorInfo?.avatarPath
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
	modifyDoctorInfo(@RequestParam(required = false) String name,
		@RequestParam(required = false) String phone,
		@RequestParam(required = false) String email,
		@RequestParam(required = false) Integer sex,
		@RequestParam(required = false) String birthday,
		@RequestParam(required = false) String experience,
		@RequestParam(required = false) String field,
		@RequestParam(required = false) String qq,
		@RequestParam(required = false) Integer consultType,
		@RequestParam(required = false) String targetUser,
		@RequestParam(required = false) String genre,
		@RequestParam(required = false) String time,
		@RequestParam(required = false) Boolean quick,
		@RequestParam(required = false) String introduce,
		@RequestParam(required = false) Boolean commentConsult,
		@RequestParam(required = false) Boolean phoneConsult,
		@RequestParam(required = false) MultipartFile avatar,
		@RequestParam(required = false) MultipartFile record,
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
		
		if (phone) {
			if (!validatePhone(phone)) {
				return '{"success" : "0", "message": "err003"}'
			}
			doctorInfo.phone = phone
		}
		
		if (name) {
			doctorInfo.name = name
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
		if (consultType != null) {
			doctorInfo.consultType = DoctorInfo.ConsultType.values()[consultType]
		}
		if (targetUser) {
			doctorInfo.targetUser = targetUser
		}
		if (genre) {
			doctorInfo.genre = genre
		}
		if (time) {
			doctorInfo.time = DateUtils.parseDate(time, 'yyyy-MM-dd HH:mm')
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
