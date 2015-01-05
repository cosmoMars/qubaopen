package com.qubaopen.survey.controller.doctor;

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.doctor.Genre
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.repository.doctor.DoctorAddressRepository
import com.qubaopen.survey.repository.doctor.DoctorInfoRepository
import com.qubaopen.survey.repository.doctor.DoctorRepository

@RestController
@RequestMapping('doctor')
@SessionAttributes('currentUser')
public class DoctorController extends AbstractBaseController<Doctor, Long> {

	private static Logger logger = LoggerFactory.getLogger(DoctorController.class)
	
	@Autowired
	DoctorRepository doctorRepository
	
	@Autowired
	DoctorInfoRepository doctorInfoRepository
	
	@Autowired
	DoctorAddressRepository doctorAddressRepository
	
	@Override
	MyRepository<Doctor, Long> getRepository() {
		doctorRepository
	}

	@RequestMapping(value = 'retrieveDoctorList', method = RequestMethod.POST)
	retrieveDoctorList(@RequestParam(required = false) Long genreId,
		@RequestParam(required = false) Long targetId,
		@RequestParam(required = false) String ids,
		@PageableDefault(page = 0, size = 20) Pageable pageable,
		@ModelAttribute('currentUser') User user) {
		logger.trace '-- 医师列表 --'
		
		def list = [], doctorInfos, data = []
		if (ids) {
			def strIds = ids.split(',')
			strIds.each {
				list << Long.valueOf(it.trim())
			}
			
//			if (generId != null) {
//				
//			}
			
//			doctorInfos = doctorRepository.findOtherDoctor(list, pageable)
		} else {
			if (genreId != null) {
				doctorInfos = doctorInfoRepository.findDoctorWithoutGenre(new Genre(id : genreId), pageable)
			} else {
				doctorInfos = doctorInfoRepository.findAll(pageable)
			}
		}
		doctorInfos.each {
//			def address = doctorAddressRepository.findByDoctorAndUsed(it, true)
			data << [
				'doctorId' : it?.id,
				'doctorName' : it?.name,
				'doctorAvatar' : it?.avatarPath,
				'doctorAddress' : it?.address,
				'doctorIntroduce' : it?.introduce,
				'faceToFace' : it?.faceToFace,
				'video' : it?.video
			]
		}
//		def more = true
//		if (doctors && doctors.size() < pageable.pageSize) {
//			more = false
//		}
		[
			'success' : '1',
			'more' : true,
			'data' : data	
		]
	}
		
	/**
	 * @param id
	 * @param user
	 * @return
	 * 获取医师详细
	 */
	@RequestMapping(value = 'retrieveDoctorDetail/{id}', method = RequestMethod.GET)
	retrieveDoctorDetail(@PathVariable long id, @ModelAttribute('currentUser') User user) {
		
		logger.trace '-- 获取医师详细 --'
		
		def doctorInfo = doctorInfoRepository.findOne(id),
			infoTime = doctorInfo.bookingTime,
			times = infoTime.split(','), timeData = []
		times.eachWithIndex { value, index ->
			for (i in 0..value.length() - 1) {
				if ('1' == value[i] || '1'.equals(value)) {
					timeData << [
						'dayId': index + 1,
						'startTime' : "$i:00" as String,
						'endTime' : "${i+1}:00" as String
					]
				}
			}
		}
		[
			'success' : '1',
			'name' : doctorInfo?.name,
			'address' : doctorInfo?.address,
			'introduce' : doctorInfo?.introduce,
			'field' : doctorInfo?.field,
			'targetUser' : doctorInfo?.targetUser,
			'genre' : doctorInfo?.genre,
			'faceToFace' : doctorInfo?.faceToFace,
			'video' : doctorInfo?.video,
			'avatar' : doctorInfo?.avatarPath,
			'timeData' : timeData
		]
		
	}
}
