package com.qubaopen.doctor.controller.doctor;

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttributes

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.doctor.repository.doctor.DoctorShareRepository
import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.doctor.DoctorShare

@RestController
@RequestMapping('doctorShare')
@SessionAttributes('currentDoctor')
public class DoctorShareController extends AbstractBaseController<DoctorShare, Long> {

	private static Logger logger = LoggerFactory.getLogger(DoctorShareController.class)
	
	@Autowired
	DoctorShareRepository doctorShareRepository
	
	@Override
	protected MyRepository<DoctorShare, Long> getRepository() {
		return doctorShareRepository;
	}
	
	/**
	 * @param target
	 * @param origin
	 * @param doctor
	 * @return
	 * 医师分享
	 */
	@RequestMapping(value = 'share', method = RequestMethod.POST)
	share(@RequestParam(required = false) Integer target,
		@RequestParam(required = false) Integer origin,
		@ModelAttribute('currentDoctor') Doctor doctor
		) {
		
		logger.trace('-- 医师分享 --')
		
		if (target != null && origin != null) {
			def doctorShare = new DoctorShare(
				doctor : doctor,
				shareTarget : DoctorShare.ShareTarget.values()[target],
				shareOrigin : DoctorShare.ShareOrigin.values()[origin],
			)
			doctorShareRepository.save(doctorShare)
			return '{"success" : "1"}'
		}
		'{"success" : "0", "message" : "传入的参数不正确"}'
	}

}
