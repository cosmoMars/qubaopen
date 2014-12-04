package com.qubaopen.doctor.controller.doctor;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.doctor.repository.doctor.DoctorUdidRepository;
import com.qubaopen.doctor.utils.DateCommons
import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.doctor.DoctorUdid
import com.qubaopen.survey.entity.user.User

@RestController
@RequestMapping('doctorUdid')
@SessionAttributes('currentDoctor')
public class DoctorUdidController extends AbstractBaseController<DoctorUdid, Long> {

	@Autowired
	DoctorUdidRepository doctorUdidRepository
	
	@Override
	protected MyRepository<DoctorUdid, Long> getRepository() {
		// TODO Auto-generated method stub
		return doctorUdidRepository;
	}
	
	/**
	 * 获取用户UDID信息
	 * @return
	 */
	@RequestMapping(value = 'retrieveDoctorUdid', method = RequestMethod.GET)
	retrieveDoctorUdid(@ModelAttribute('currentUser') Doctor doctor) {

		logger.trace(" -- 获取用户UDID信息 -- ")

		def udid = doctorUdidRepository.findOne(doctor.id)

		[
			'success' : '1',
			'message' : '成功',
			'id' : udid.id,
			'push' : udid.push,
			'startTime' : DateCommons.Date2String(udid?.startTime, "HH:mm"),
			'endTime' : DateCommons.Date2String(udid?.endTime, "HH:mm")
		]
	}
	
	/**
	 * 修改推送信息
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@RequestMapping(value = 'modifyUdid', method = RequestMethod.PUT)
	modifyUdid(@RequestParam(required = false) String startTime,
		@RequestParam(required = false) String endTime,
		@RequestParam(required = false) Boolean push,
		@ModelAttribute('currentDoctor') Doctor doctor
		) {

		def doctorUdid = doctorUdidRepository.findOne(doctor.id)
		if (push != null) {
			doctorUdid.push = push
		}
		if (startTime) {
			doctorUdid.startTime = DateCommons.String2Date(startTime, 'HH:mm')
		}
		if (endTime) {
			doctorUdid.endTime = DateCommons.String2Date(endTime, 'HH:mm')
		}
		doctorUdidRepository.save(doctorUdid)
		'{"success": "1"}'
	}


}
