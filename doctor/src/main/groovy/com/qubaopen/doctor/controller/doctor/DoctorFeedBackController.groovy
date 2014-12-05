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
import com.qubaopen.doctor.repository.doctor.DoctorFeedBackRepository
import com.qubaopen.survey.entity.doctor.Doctor
import com.qubaopen.survey.entity.doctor.DoctorFeedBack
import com.qubaopen.survey.entity.user.UserFeedBack
@RestController
@RequestMapping('doctorFeedBack')
@SessionAttributes('currentDoctor')
public class DoctorFeedBackController extends AbstractBaseController<DoctorFeedBack, Long> {

	@Autowired
	DoctorFeedBackRepository doctorFeedBackRepository
	
	@Override
	MyRepository<DoctorFeedBack, Long> getRepository() {
		doctorFeedBackRepository
	}
	
	/**
	 * @param content
	 * @param contactMethod
	 * @param backType
	 * @param doctor
	 * @return
	 */
	@RequestMapping(value = 'addFeedBack', method = RequestMethod.POST)
	addFeedBack(@RequestParam(required = false) String content,
		@RequestParam(required = false) Integer backType,
		@ModelAttribute('currentDoctor') Doctor doctor) {
		
		def feedBackType
		if (backType != null) {
			feedBackType = DoctorFeedBack.FeedBackType.values()[backType]
		} else {
			feedBackType = DoctorFeedBack.FeedBackType.ORDINARY
		}
	
		def doctorFeedBack = new DoctorFeedBack(
			content : content,
			doctor : doctor,
			feedBackType : feedBackType
		)
		doctorFeedBackRepository.save(doctorFeedBack)
		'{"success": "1"}'
	}

}
