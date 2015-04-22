package com.qubaopen.backend.controller.doctor;

import java.util.ArrayList;
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

import com.qubaopen.backend.repository.assistant.AssistantRepository;
import com.qubaopen.backend.repository.doctor.DoctorCaseRepository;
import com.qubaopen.backend.repository.doctor.DoctorInfoRepository;
import com.qubaopen.backend.repository.doctor.DoctorRepository;
import com.qubaopen.backend.service.SmsService;
import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.AuditStatus;
import com.qubaopen.survey.entity.doctor.DoctorCase;


@RestController
@RequestMapping("doctorCase")
public class DoctorCaseController extends AbstractBaseController<DoctorCase, Long>{

	@Autowired
	private DoctorRepository doctorRepository;

    @Autowired
    private DoctorInfoRepository doctorInfoRepository;

	@Autowired
	private SmsService smsService;

    @Autowired
    private AssistantRepository assistantRepository;

	@Autowired
	private DoctorCaseRepository doctorCaseRepository;
	
	@Override
	protected MyRepository<DoctorCase, Long> getRepository() {
		return doctorCaseRepository;
	}

	
	/**
	 * 获取案例列表
	 * @param pageable
	 * @param status
	 * @param doctorId  2015-04-20 暂时没用到
	 * @return
	 */
	@RequestMapping(value = "retrieveDoctorCase", method = RequestMethod.GET)
	private Object retrieveDoctorCase(@PageableDefault(page = 0, size = 20)
			Pageable pageable,
			@RequestParam(required = false) Integer status,
			@RequestParam(required = false) Long doctorId) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		AuditStatus s = AuditStatus.values()[status];
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("status_equal",s);
		List<DoctorCase> doctorCases = doctorCaseRepository.findAll(filter, pageable).getContent();
		

		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> map ;
		for (DoctorCase doctorCase : doctorCases) {
			map = new HashMap<String, Object>();
			map.put("id", doctorCase.getId());
			map.put("doctorId", doctorCase.getDoctor()==null?"":doctorCase.getDoctor().getId());
			map.put("title", doctorCase.getTitle());
			map.put("content", doctorCase.getContent());
			map.put("createdDate", doctorCase.getCreatedDate().toDate());
			map.put("picPath", doctorCase.getPicPath());
			map.put("refusalReason", doctorCase.getRefusalReason());
			map.put("status", doctorCase.getStatus().ordinal());
		
			list.add(map);
		}
		
		result.put("success", "1");
		result.put("list", list);
		return result;
	}
	
	
	/**
	 * 案例审核
	 * @param id
	 * @param status
	 * @param refusalReason
	 * @return
	 */
	@RequestMapping(value = "modifyDoctorCaseStatus", method = RequestMethod.POST)
	private Object modifyDoctorCaseStatus(
			@RequestParam(required = false) Long id,
			@RequestParam(required = false) int status,
			@RequestParam(required = false) String refusalReason
			) {

        DoctorCase doctorCase = doctorCaseRepository.findOne(id);
        
		
		if(null != doctorCase){
			AuditStatus s = AuditStatus.values()[status];
			if(s == AuditStatus.Failure){
				//拒绝
				doctorCase.setRefusalReason(refusalReason);
				
			} 

			doctorCase.setStatus(s);
			doctorCaseRepository.save(doctorCase);
			return "{\"success\" : \"1\"}";
		}

		return "{\"success\" : \"没有该id\"}";
	}
	
	
}
