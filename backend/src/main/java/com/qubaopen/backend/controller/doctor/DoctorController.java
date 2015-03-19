package com.qubaopen.backend.controller.doctor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.backend.repository.doctor.DoctorRepository;
import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.doctor.Doctor;
import com.qubaopen.survey.entity.doctor.DoctorInfo;


@RestController
@RequestMapping("doctor")
public class DoctorController extends AbstractBaseController<Doctor, Long>{
	
	@Autowired
	private DoctorRepository doctorRepository;
	
	@Override
	protected MyRepository<Doctor, Long> getRepository() {
		return doctorRepository;
	}

	
	/**
	 * 获取医师列表
	 * @param pageable
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "retrieveDoctor", method = RequestMethod.GET)
	private Object retrieveTopic(@PageableDefault(page = 0, size = 20)
			Pageable pageable,
			@RequestParam(required = false) Integer status) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		DoctorInfo.LoginStatus s = DoctorInfo.LoginStatus.values()[status];
		List<Doctor> list = doctorRepository.getList(pageable, s);
		
		result.put("success", "1");
		result.put("list", list.size());
		return result;
	}
}
