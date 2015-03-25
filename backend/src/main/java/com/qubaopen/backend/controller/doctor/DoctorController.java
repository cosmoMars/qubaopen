package com.qubaopen.backend.controller.doctor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.backend.repository.doctor.AssistantRepository;
import com.qubaopen.backend.repository.doctor.DoctorInfoRepository;
import com.qubaopen.backend.repository.doctor.DoctorRepository;
import com.qubaopen.backend.service.SmsService;
import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.doctor.Assistant;
import com.qubaopen.survey.entity.doctor.Doctor;
import com.qubaopen.survey.entity.doctor.DoctorInfo;


@RestController
@RequestMapping("doctor")
public class DoctorController extends AbstractBaseController<Doctor, Long>{

	@Autowired
	private DoctorRepository doctorRepository;

    @Autowired
    private DoctorInfoRepository doctorInfoRepository;

	@Autowired
	private SmsService smsService;

    @Autowired
    private AssistantRepository assistantRepository;
	
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
	private Object retrieveDoctor(@PageableDefault(page = 0, size = 20)
			Pageable pageable,
			@RequestParam(required = false) Integer status) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		DoctorInfo.LoginStatus s = DoctorInfo.LoginStatus.values()[status];
		List<Doctor> doctors = doctorRepository.getList(pageable, s);

		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> map ;
		for (Doctor doctor : doctors) {
			map = new HashMap<String, Object>();
			map.put("id", doctor.getId());
			map.put("phone", doctor.getPhone());
			map.put("loginStatus", doctor.getDoctorInfo().getLoginStatus().ordinal());
			map.put("name", doctor.getDoctorInfo().getName());
			map.put("sex", doctor.getDoctorInfo().getSex()==null?"":doctor.getDoctorInfo().getSex().ordinal());
			map.put("createTime", doctor.getDoctorInfo().getCreatedDate()==null?"":doctor.getDoctorInfo().getCreatedDate());
			map.put("experiece", doctor.getDoctorInfo().getExperience()==null?"":doctor.getDoctorInfo().getExperience());
			map.put("field", doctor.getDoctorInfo().getField()==null?"":doctor.getDoctorInfo().getField());
			map.put("genre", doctor.getDoctorInfo().getGenre()==null?"":doctor.getDoctorInfo().getGenre());
			map.put("targetUser", doctor.getDoctorInfo().getTargetUser()==null?"":doctor.getDoctorInfo().getTargetUser());
			map.put("avatar", doctor.getDoctorInfo().getAvatarPath()==null?"":doctor.getDoctorInfo().getAvatarPath());
			map.put("record", doctor.getDoctorInfo().getRecordPath()==null?"":doctor.getDoctorInfo().getRecordPath());
			map.put("introduce", doctor.getDoctorInfo().getIntroduce()==null?"":doctor.getDoctorInfo().getIntroduce());
			list.add(map);
		}
		
		result.put("success", "1");
		result.put("list", list);
		return result;
	}
	
	
	
	@RequestMapping(value = "modifyDoctorStatus", method = RequestMethod.POST)
	private Object modifyDoctorStatus(
			@RequestParam(required = false) Long id,
			@RequestParam(required = false) int loginStatus,
			@RequestParam(required = false) String refusalReason,
			@RequestParam(required = false) Boolean review,
			@RequestParam(required = false) String reviewReason) {

        DoctorInfo di = doctorInfoRepository.findOne(id);
		
		if(null != di){
            DoctorInfo.LoginStatus status = DoctorInfo.LoginStatus.values()[loginStatus];
			di.setLoginStatus(status);

            Assistant assistant;
            if (di.getAssistant() != null) {
                assistant = di.getAssistant();
            } else {
                Long assistantId = assistantRepository.findSpaceAssistant();
                assistant = assistantRepository.findOne(assistantId);
                di.setAssistant(assistant);
            }

			String param = "{\"param1\" : \"" + assistant.getName() + "\",\"param2\" : \"" + assistant.getPhone() + "\"}";
            Map<String, Object> result;
			if(status == DoctorInfo.LoginStatus.Refusal && di.getLoginStatus() == DoctorInfo.LoginStatus.Auditing){
				//拒绝
                result = smsService.sendSmsMessage(di.getPhone(), 3, param);
                di.setRefusalReason(refusalReason);
			} else if (status == DoctorInfo.LoginStatus.Audited && (di.getLoginStatus() == DoctorInfo.LoginStatus.Auditing || di.getLoginStatus() == DoctorInfo.LoginStatus.Audited)){
				//通过
                result = smsService.sendSmsMessage(di.getPhone(), 2, param);
                if (review != null) {
                    di.setReview(review);
                    if (review) {
                        di.setReviewReason(reviewReason);
                    }
                }
			} else {
                return "{\"success\" : \"0\", \"message\" : \"修改医师状态不正确\"}";
            }
            if (!StringUtils.equals((String)result.get("resCode"), "0")) {
                return "{\"success\" : \"0\", \"message\" : \"resCode = " + result.get("resCode") + "\"}";
            }
			doctorInfoRepository.save(di);
		}

		return "{\"success\" : \"0\"}";
	}
}
