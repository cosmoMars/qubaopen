package com.qubaopen.doctor.controller.booking;

import java.util.Date;
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
import com.qubaopen.doctor.repository.booking.DoctorDiagnosisRepository;
import com.qubaopen.doctor.repository.doctor.BookingRepository;
import com.qubaopen.doctor.repository.doctor.DoctorInfoRepository;
import com.qubaopen.survey.entity.booking.Booking;
import com.qubaopen.survey.entity.booking.DoctorDiagnosis;
import com.qubaopen.survey.entity.doctor.Doctor;
import com.qubaopen.survey.entity.doctor.DoctorInfo;

@RestController
@RequestMapping("doctorDiagnosis")
@SessionAttributes("currentDoctor")
public class DoctorDiagnosisController extends AbstractBaseController<DoctorDiagnosis, Long> {
	
	private static Logger logger = LoggerFactory.getLogger(DoctorDiagnosisController.class);

	@Autowired
	private DoctorDiagnosisRepository doctorDiagnosisRepository;
	
	@Autowired
	private BookingRepository bookingRepository;
	
	@Autowired
	private DoctorInfoRepository doctorInfoRepository;
	
	@Override
	protected MyRepository<DoctorDiagnosis, Long> getRepository() {
		return doctorDiagnosisRepository;
	}

	
	/**
	 * @param booking
	 * @param doctor
	 * @return
	 * 查询医师诊断
	 */
	@RequestMapping(value = "retrieveDoctorDiagnosisByBooking", method = RequestMethod.POST)
	private List<DoctorDiagnosis> retrieveDoctorDiagnosisByBooking(@RequestParam long bookingId, @ModelAttribute("currentDoctor") Doctor doctor) {
		
		logger.trace("-- 查询医师诊断 --");
		
		return doctorDiagnosisRepository.findByBookingIdOrderByTimeDesc(bookingId);
	}
	
	/**
	 * @param booking
	 * @param doctor
	 * @return
	 * 添加医师诊断
	 */
	@RequestMapping(value = "addDoctorDiagnosis", method = RequestMethod.POST)
	private String addDoctorDiagnosis(@RequestParam long bookingId, 
			@RequestParam(required = false) String diagnosis,
			@ModelAttribute("currentDoctor") Doctor doctor) {
		
		logger.trace("-- 查询医师诊断 --");
		
		DoctorInfo di = doctorInfoRepository.findOne(doctor.getId());
				
		if (di.getLoginStatus() != DoctorInfo.LoginStatus.Audited) {
			return "{\"success\" : \"0\", \"message\" : \"err916\"}";
		}
			
		
		if (diagnosis == null || "".equals(diagnosis.trim())) {
			return "{\"success\" : \"0\", \"message\" : \"err808\"}";
		}
		DoctorDiagnosis doctorDiagnosis = new DoctorDiagnosis();
		
		Booking booking = bookingRepository.findOne(bookingId);
		
		doctorDiagnosis.setBooking(booking);
		doctorDiagnosis.setTime(new Date());
		doctorDiagnosis.setDiagnosis(diagnosis);
		
//		booking.getDoctorDiagnosis().add(doctorDiagnosis);
		
		doctorDiagnosisRepository.save(doctorDiagnosis);
		
		return "{\"success\" : \"1\"}";
	}
	
	
	
	/**
	 * @param diagnosisId
	 * @param diagnosis
	 * @param doctor
	 * @return
	 * 修改医师诊断
	 */
	@RequestMapping(value = "modifyDoctorDiagnosis", method = RequestMethod.POST)
	private String modifyDoctorDiagnosis(@RequestParam long diagnosisId, 
			@RequestParam(required = false) String diagnosis,
			@ModelAttribute("currentDoctor") Doctor doctor) {
		
		logger.trace("-- 修改医师诊断 --");
		
		if (diagnosis == null || "".equals(diagnosis.trim())) {
			return "{\"success\" : \"0\", \"message\" : \"err808\"}";
		}
		
		DoctorDiagnosis dd = doctorDiagnosisRepository.findOne(diagnosisId);
		
		dd.setDiagnosis(diagnosis);
		doctorDiagnosisRepository.save(dd);
		
		return "{\"success\" : \"1\"}";
	}
}
