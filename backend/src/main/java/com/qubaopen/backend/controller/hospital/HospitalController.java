package com.qubaopen.backend.controller.hospital;

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

import com.qubaopen.backend.repository.hospital.HospitalRepository;
import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.hospital.Hospital;
import com.qubaopen.survey.entity.hospital.HospitalInfo;


@RestController
@RequestMapping("hospital")
public class HospitalController extends AbstractBaseController<Hospital,Long>{
	
	@Autowired
	private HospitalRepository hospitalRepository;
	
	@Override
	protected MyRepository<Hospital, Long> getRepository() {
		return hospitalRepository;
	}

	
	/**
	 * 获取医师列表
	 * @param pageable
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "retrieveHospital", method = RequestMethod.GET)
	private Object retrieveHospital(@PageableDefault(page = 0, size = 20)
			Pageable pageable,
			@RequestParam(required = false) Integer status) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		HospitalInfo.LoginStatus s = HospitalInfo.LoginStatus.values()[status];
		List<Hospital> hospitals = hospitalRepository.getList(pageable, s);

		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> map ;
		for (Hospital hospital : hospitals) {
			map = new HashMap<String, Object>();
			map.put("id", hospital.getId());
			map.put("email", hospital.getEmail());
			map.put("loginStatus", hospital.getHospitalInfo().getLoginStatus().ordinal());
			map.put("name", hospital.getHospitalInfo().getName());
			map.put("createTime", hospital.getHospitalInfo().getCreatedDate()==null?"":hospital.getHospitalInfo().getCreatedDate());
			map.put("genre", hospital.getHospitalInfo().getGenre()==null?"":hospital.getHospitalInfo().getGenre());
			map.put("targetUser", hospital.getHospitalInfo().getTargetUser()==null?"":hospital.getHospitalInfo().getTargetUser());
			//map.put("avatar", hospital.getHospitalInfo().getgetAvatarPath()==null?"":hospital.getHospitalInfo().getAvatarPath());
			//map.put("record", hospital.getHospitalInfo().getRecordPath()==null?"":hospital.getHospitalInfo().getRecordPath());
			map.put("introduce", hospital.getHospitalInfo().getIntroduce()==null?"":hospital.getHospitalInfo().getIntroduce());
			list.add(map);
		}
		
		result.put("success", "1");
		result.put("list", list);
		return result;
	}
}
