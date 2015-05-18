package com.qubaopen.survey.controller.hospital

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.hospital.Hospital
import com.qubaopen.survey.entity.hospital.HospitalInfo
import com.qubaopen.survey.entity.user.User
import com.qubaopen.survey.repository.hospital.HospitalInfoRepository
import com.qubaopen.survey.repository.hospital.HospitalRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping('hospital')
@SessionAttributes('currentUser')
public class HospitalController extends AbstractBaseController<Hospital, Long> {

	static Logger logger = LoggerFactory.getLogger(HospitalController.class)
	
	@Autowired
	HospitalRepository hospitalRepository
	
	@Autowired
	HospitalInfoRepository hospitalInfoRepository
	@Override
	MyRepository<Hospital, Long> getRepository() {
		hospitalRepository
	}

	/**
	 * @param ids
	 * @param pageable
	 * @param user
	 * @return
	 * 诊所列表
	 */
	@RequestMapping(value = 'retrieveHospitalList', method = RequestMethod.POST)
	retrieveHospitalList(@RequestParam(required = false) String ids,
		@PageableDefault(page = 0, size = 0) Pageable pageable,
		@ModelAttribute('currentUser') User user) {
		
		logger.trace '-- 诊所列表 --'
		
		
		def list = [], hospitals, data = []
		if (ids) {
			def strIds = ids.split(',')
			strIds.each {
				list << Long.valueOf(it.trim())
			}
			hospitals = hospitalRepository.findOtherHospital(list, pageable)
		} else {
			hospitals = hospitalRepository.findAll(
					[
							'hospitalInfo.loginStatus_equal': HospitalInfo.LoginStatus.Audited
					], pageable)
		}
		hospitals.each {
			data << [
					'hospitalId'  : it.id,
					'hospitalName': it.hospitalInfo?.name
			]
		}
		[
			'success' : '1',
			'data' : data
		]
	}
		
	@RequestMapping(value = 'retrieveHosptialDetial/{id}', method = RequestMethod.GET)
	retrieveHosptialDetial(@PathVariable long id, @ModelAttribute('currentUser') User user) {
		
		logger.trace '-- 获取诊所详细 --'
		
		def hospital = hospitalInfoRepository.findOne(id)
		
		[
			'success' : '1',
			'name' : hospital?.name,
			'address' : hospital?.address,
			'establishTime' : hospital?.establishTime,
			'phone' : hospital?.phone,
			'urgentPhone' : hospital?.urgentPhone,
			'qq' : hospital?.qq,
			'introduce' : hospital?.introduce,
			'wordsConsult' : hospital?.wordsConsult,
			'minCharge' : hospital?.minCharge,
			'maxCharge' : hospital?.maxCharge,
			'records' : hospital?.hospitalDoctorRecords.size()
		]
	}
	
}
