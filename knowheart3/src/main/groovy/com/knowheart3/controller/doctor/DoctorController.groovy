package com.knowheart3.controller.doctor

import com.knowheart3.repository.base.AreaCodeRepository
import com.knowheart3.repository.doctor.DoctorAddressRepository
import com.knowheart3.repository.doctor.DoctorInfoRepository
import com.knowheart3.repository.doctor.DoctorRepository
import com.knowheart3.service.AreaCodeService
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.user.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

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
	
	@Autowired
	AreaCodeRepository areaCodeRepository
	
	@Autowired
	AreaCodeService areaCodeService
	
	@Override
	MyRepository<Doctor, Long> getRepository() {
		doctorRepository
	}

	/**
	 * @param genreId
	 * @param targetId
	 * @param ids
	 * @param pageable
	 * @param user
	 * @return
	 * 获取医师列表
	 */
	@RequestMapping(value = 'retrieveDoctorList', method = RequestMethod.POST)
	retrieveDoctorList(@RequestParam(required = false) Long genreId,
		@RequestParam(required = false) Long targetId,
		@RequestParam(required = false) String areaCode,
		@RequestParam(required = false) Boolean faceToFace,
		@RequestParam(required = false) Boolean video,
		@RequestParam(required = false) String ids,
		@PageableDefault(page = 0, size = 20) Pageable pageable,
		@ModelAttribute('currentUser') User user) {
		logger.trace '-- 医师列表 --'

		def list = [], doctorInfos, data = [], filters = [:]
		if (ids) {
			def strIds = ids.split(',')
			strIds.each {
				list << Long.valueOf(it.trim())
			}
			filters.put('ids', list)
		}
		filters.put('pageable', pageable)
		if (genreId != null) {
			filters.put('genreId', genreId)
		}
		if (targetId != null) {
			filters.put('targetId', targetId)
		}
		if (areaCode != null) {
			def code = areaCodeRepository.findByCode(areaCode),
				idsList = []
			idsList = areaCodeService.getAreaCodeIds(idsList, code)
			filters.put('areaCode', idsList)
		}
		if (faceToFace != null) {
			filters.put('faceToFace', faceToFace)
		}
		if (video != null) {
			filters.put('video', video)
		}
		doctorInfos = doctorInfoRepository.findByFilter(filters)
		doctorInfos.each {
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
		def more = true
		if (doctorInfos && doctorInfos.size() < pageable.pageSize) {
			more = false
		}
		[
			'success' : '1',
			'more' : more,
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
			'onlineFee' : doctorInfo?.onlineFee,
			'offlineFee' : doctorInfo?.offlineFee,
			'timeData' : timeData
		]
		
	}
}
