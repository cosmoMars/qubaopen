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
import com.qubaopen.backend.repository.doctor.DoctorArticleRepository;
import com.qubaopen.backend.repository.doctor.DoctorInfoRepository;
import com.qubaopen.backend.repository.doctor.DoctorRepository;
import com.qubaopen.backend.service.SmsService;
import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.AuditStatus;
import com.qubaopen.survey.entity.doctor.DoctorArticle;


@RestController
@RequestMapping("doctorArticle")
public class DoctorArticleController extends AbstractBaseController<DoctorArticle, Long>{

	@Autowired
	private DoctorRepository doctorRepository;

    @Autowired
    private DoctorInfoRepository doctorInfoRepository;

	@Autowired
	private SmsService smsService;

    @Autowired
    private AssistantRepository assistantRepository;

	@Autowired
	private DoctorArticleRepository doctorArticleRepository;
	
	@Override
	protected MyRepository<DoctorArticle, Long> getRepository() {
		return doctorArticleRepository;
	}

	
	/**
	 * 获取文章列表
	 * @param pageable
	 * @param status
	 * @param doctorId  2015-04-20 暂时没用到
	 * @return
	 */
	@RequestMapping(value = "retrieveDoctorArticle", method = RequestMethod.GET)
	private Object retrieveDoctorArticle(@PageableDefault(page = 0, size = 20)
			Pageable pageable,
			@RequestParam(required = false) Integer status,
			@RequestParam(required = false) Long doctorId) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		AuditStatus s = AuditStatus.values()[status];
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("status_equal",s);
		List<DoctorArticle> doctorArticles = doctorArticleRepository.findAll(filter, pageable).getContent();
		

		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> map ;
		for (DoctorArticle doctorArticle : doctorArticles) {
			map = new HashMap<String, Object>();
			map.put("id", doctorArticle.getId());
			map.put("doctorId", doctorArticle.getDoctor()==null?"":doctorArticle.getDoctor().getId());
			map.put("title", doctorArticle.getTitle());
			map.put("content", doctorArticle.getContent());
			map.put("createdDate", doctorArticle.getCreatedDate().toDate());
			map.put("picPath", doctorArticle.getPicPath());
			map.put("refusalReason", doctorArticle.getRefusalReason());
			map.put("status", doctorArticle.getStatus().ordinal());
		
			list.add(map);
		}
		
		result.put("success", "1");
		result.put("list", list);
		return result;
	}
	
	
	/**
	 * 文章审核
	 * @param id
	 * @param status
	 * @param refusalReason
	 * @return
	 */
	@RequestMapping(value = "modifyDoctorArticleStatus", method = RequestMethod.POST)
	private Object modifyDoctorArticleStatus(
			@RequestParam(required = false) Long id,
			@RequestParam(required = false) int status,
			@RequestParam(required = false) String refusalReason
			) {

        DoctorArticle doctorArticle = doctorArticleRepository.findOne(id);
        
		
		if(null != doctorArticle){
			AuditStatus s = AuditStatus.values()[status];
			if(s == AuditStatus.Failure){
				//拒绝
				doctorArticle.setRefusalReason(refusalReason);
				
			} 

			doctorArticle.setStatus(s);
			doctorArticleRepository.save(doctorArticle);
			return "{\"success\" : \"1\"}";
		}

		return "{\"success\" : \"没有该id\"}";
	}
	
	
}
