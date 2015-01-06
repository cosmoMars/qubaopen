package com.qubaopen.survey.controller.targetuser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.doctor.TargetUser;
import com.qubaopen.survey.repository.targetuser.TargetUserRepository;

@RestController
@RequestMapping("targetUser")
@SessionAttributes("currentUser")
public class TargetUserController extends AbstractBaseController<TargetUser, Long> {

	private static Logger logger = LoggerFactory.getLogger(TargetUserController.class);
	
	@Autowired
	private TargetUserRepository targetUserRepository;
	
	@Override
	protected MyRepository<TargetUser, Long> getRepository() {
		return targetUserRepository;
	}

	/**
	 * @return
	 * 获取擅长人群
	 */
	@RequestMapping(value = "retrieveTargetUser", method = RequestMethod.GET)
	Map<String, Object> retieveTargetUser() {
		
		logger.trace("-- 获取擅长人群 --");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<TargetUser> targetUsers = targetUserRepository.findAll();
		map.put("success", "1");
		map.put("targetUsers", targetUsers);
		
		return map;
	}
	
}
