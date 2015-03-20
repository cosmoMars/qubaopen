package com.qubaopen.doctor.controller.doctor
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.doctor.repository.doctor.DoctorInfoRepository
import com.qubaopen.doctor.repository.doctor.DoctorRecordRepository
import com.qubaopen.doctor.utils.UploadUtils
import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.doctor.DoctorInfo
import com.qubaopen.survey.entity.doctor.DoctorRecord
import org.apache.commons.lang3.time.DateUtils
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping('doctorRecord')
@SessionAttributes('currentDoctor')
public class DoctorRecordController extends AbstractBaseController<DoctorRecord, Long> {

	@Autowired
	DoctorRecordRepository doctorRecordRepository

    @Autowired
    DoctorInfoRepository doctorInfoRepository

    @Autowired
    UploadUtils uploadUtils
	
	@Override
	MyRepository<DoctorRecord, Long> getRepository() {
		doctorRecordRepository
	}
	
	/**
	 * @param doctor
	 * @return
	 * 获取资质证明
	 */
	@RequestMapping(value = 'retrieveDoctorRecord', method = RequestMethod.GET)
	retrieveDoctorRecord(@ModelAttribute('currentDoctor') Doctor doctor) {
		
		def dr = doctorRecordRepository.findOne(doctor.id)
        def recUrl
        if (dr) {
            recUrl = uploadUtils.retrievePriavteUrl(dr.recordPath)
        }
		[
			'success' : '1',
			'educationalStart' : dr?.educationalStart,
			'educationalEnd' : dr?.educationalEnd,
			'school' : dr?.school,
			'profession' : dr?.profession,
			'degree' : dr?.degree,
			'educationalIntroduction' : dr?.educationalIntroduction,
			'trainStart' : dr?.trainStart,
			'trainEnd' : dr?.trainEnd,
			'course' : dr?.course,
			'organization' : dr?.organization,
			'trainIntroduction' : dr?.trainIntroduction,
			'supervise' : dr?.supervise,
			'orientation' : dr?.orientation,
			'superviseHour' : dr?.superviseHour,
			'contactMethod' : dr?.contactMethod,
			'superviseIntroduction' : dr?.superviseIntroduction,
			'selfStart' : dr?.selfStart,
			'selfEnd' : dr?.selfEnd,
			'totalHour' : dr?.totalHour,
			'selfIntroduction' : dr?.selfIntroduction,
			'record' : recUrl
		]
	}
	
	/**
	 * @param educationalStart
	 * @param educationalEnd
	 * @param school
	 * @param profession
	 * @param degree
	 * @param educationalIntroduction
	 * @param trainStart
	 * @param trainEnd
	 * @param course
	 * @param organization
	 * @param trainIntroduction
	 * @param supervise
	 * @param orientation
	 * @param superviseHour
	 * @param contactMethod
	 * @param superviseIntroduction
	 * @param selfStart
	 * @param selfEnd
	 * @param totalHour
	 * @param selfIntroduction
	 * @param record
	 * @param doctor
	 * @param request
	 * @return
	 * 修改
	 */
	@Transactional
	@RequestMapping(value = 'modifyDoctorRecord', method = RequestMethod.POST, consumes = 'multipart/form-data')
	modifyDoctorRecord(@RequestParam(required = false) String educationalStart,
		@RequestParam(required = false) String educationalEnd,
		@RequestParam(required = false) String school,
		@RequestParam(required = false) String profession,
		@RequestParam(required = false) String degree,
		@RequestParam(required = false) String educationalIntroduction,
		@RequestParam(required = false) String trainStart,
		@RequestParam(required = false) String trainEnd,
		@RequestParam(required = false) String course,
		@RequestParam(required = false) String organization,
		@RequestParam(required = false) String trainIntroduction,
		@RequestParam(required = false) String supervise,
		@RequestParam(required = false) String orientation,
		@RequestParam(required = false) Integer superviseHour,
		@RequestParam(required = false) String contactMethod,
		@RequestParam(required = false) String superviseIntroduction,
		@RequestParam(required = false) String selfStart,
		@RequestParam(required = false) String selfEnd,
		@RequestParam(required = false) Integer totalHour,
		@RequestParam(required = false) String selfIntroduction,
		@RequestParam(required = false) MultipartFile record,
		@ModelAttribute('currentDoctor') Doctor doctor,
		HttpServletRequest request) {
		
		
		def dr = doctorRecordRepository.findOne(doctor.id)
		
		if (dr) {
			if (school) {
				dr.school = school
			}
			if (profession) {
				dr.profession = profession
			}
			if (degree) {
				dr.degree = degree
			}
			if (educationalIntroduction) {
				dr.educationalIntroduction = educationalIntroduction
			}
			if (course) {
				dr.course = course
			}
			if (organization) {
				dr.organization = organization
			}
			if (trainIntroduction) {
				dr.trainIntroduction = trainIntroduction
			}
			if (supervise) {
				dr.supervise = supervise
			}
			if (orientation) {
				dr.orientation = orientation
			}
			if (superviseHour != null) {
				dr.superviseHour = superviseHour
			}
			if (contactMethod) {
				dr.contactMethod = contactMethod
			}
			if (superviseIntroduction) {
				dr.superviseIntroduction = superviseIntroduction
			}
			if (totalHour != null) {
				dr.totalHour = totalHour
			}
			if (selfIntroduction) {
				dr.selfIntroduction = selfIntroduction
			}
			
		} else {
		
			dr = new DoctorRecord(
				id : doctor.id,
				school : school,
				profession : profession,
				degree : degree,
				educationalIntroduction : educationalIntroduction,
				course : course,
				organization : organization,
				trainIntroduction : trainIntroduction,
				supervise : supervise,
				orientation : orientation,
				contactMethod : contactMethod,
				superviseIntroduction : superviseIntroduction,
				selfIntroduction : selfIntroduction
			)
			
		}
		if (educationalStart) {
			dr.educationalStart = DateUtils.parseDate(educationalStart, 'yyyy-MM-dd')
		}
		if (educationalEnd) {
			dr.educationalEnd = DateUtils.parseDate(educationalEnd, 'yyyy-MM-dd')
		}
		if (trainStart) {
			dr.trainStart = DateUtils.parseDate(trainStart, 'yyyy-MM-dd')
		}
		if (trainEnd) {
			dr.trainEnd = DateUtils.parseDate(trainEnd, 'yyyy-MM-dd')
		}
		if (selfStart) {
			dr.selfStart = DateUtils.parseDate(selfStart, 'yyyy-MM-dd')
		}
		if (selfEnd) {
			dr.selfEnd = DateUtils.parseDate(selfEnd, 'yyyy-MM-dd')
		}
		if (superviseHour != null) {
			dr.superviseHour = superviseHour
		}
		if (totalHour != null) {
			dr.totalHour = totalHour
		}
		if (record) {

            def doctorInfo = doctorInfoRepository.findOne(dr.id)

            def drName = 'dr' + doctorInfo.id
            def recordPath = uploadUtils.uploadTo7niu(2, drName, record)
            doctorInfo.recordPath = recordPath
            doctorInfo.lastModifiedDate = new DateTime()
            doctorInfo.loginStatus = DoctorInfo.LoginStatus.Auditing
            doctorInfoRepository.save(doctorInfo)
		}
		
		doctorRecordRepository.save(dr)
		'{"success" : "1"}'
	}
		
}
