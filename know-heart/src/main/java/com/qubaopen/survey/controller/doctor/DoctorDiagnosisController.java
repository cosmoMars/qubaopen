package com.qubaopen.survey.controller.doctor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.booking.DoctorDiagnosis;
import com.qubaopen.survey.entity.user.User;
import com.qubaopen.survey.repository.doctor.DoctorDiagnosisRepository;

@RestController
@RequestMapping("doctorDiagnosis")
@SessionAttributes("currentUser")
public class DoctorDiagnosisController extends AbstractBaseController<DoctorDiagnosis, Long> {

	private static Logger logger = LoggerFactory.getLogger(DoctorDiagnosisController.class);
	
	@Autowired
	private DoctorDiagnosisRepository doctorDiagnosisRepository;
	
	@Override
	protected MyRepository<DoctorDiagnosis, Long> getRepository() {
		return doctorDiagnosisRepository;
	}

	/**
	 * @param bookingId
	 * @param user
	 * @return
	 * 获取医师诊断
	 */
	@RequestMapping(value = "retrieveDoctorDiagnosisByBookingId", method = RequestMethod.GET)
	private List<DoctorDiagnosis> retrieveDoctorDiagnosisByBookingId(@RequestParam long bookingId,
			@ModelAttribute("currentUser") User user) {
		
		logger.trace("-- 获取医师诊断 --");
		
		return doctorDiagnosisRepository.findByBookingIdOrderByTimeDesc(bookingId);
		
	}
}
