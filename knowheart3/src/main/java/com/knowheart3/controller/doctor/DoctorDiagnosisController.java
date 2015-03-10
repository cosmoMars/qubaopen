package com.knowheart3.controller.doctor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.knowheart3.repository.doctor.DoctorDiagnosisRepository;
import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.booking.DoctorDiagnosis;
import com.qubaopen.survey.entity.user.User;

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
	private Map<String, Object> retrieveDoctorDiagnosisByBookingId(@RequestParam long bookingId,
			@ModelAttribute("currentUser") User user) {
		
		logger.trace("-- 获取医师诊断 --");
        Map<String, Object> result = new HashMap<String, Object>();
        if (null == user.getId()) {
            result.put("success", "0");
            result.put("message", "err000");
            return result;
        }
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		List<DoctorDiagnosis> doctorDiagnosis = doctorDiagnosisRepository.findByBookingIdOrderByTimeDesc(bookingId);
		
		for (DoctorDiagnosis dd : doctorDiagnosis) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("diagnosisId", dd.getId());
			map.put("diagnosis", dd.getDiagnosis() != null ? dd.getDiagnosis() : "");
			map.put("time", dd.getTime());
			list.add(map);
		}

		result.put("success", "1");
		result.put("data", list);
		return result;
	}
}
