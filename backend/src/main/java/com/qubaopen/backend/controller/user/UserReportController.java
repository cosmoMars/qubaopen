package com.qubaopen.backend.controller.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.backend.repository.user.UserRepository;
import com.qubaopen.backend.vo.UserReportVo;

@RestController
@RequestMapping("userReport")
public class UserReportController {
	
	@Autowired
	private UserRepository userRepository;

	private static final Logger logger = LoggerFactory.getLogger(UserReportController.class);
	
	@RequestMapping(value = "getUserReports", method = RequestMethod.GET)
	public Map<String, Object> getUserReports(){
		logger.trace(" -- 用户统计 -- ");
		List<UserReportVo> userReports = userRepository.calUserReports();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", "1");
		map.put("reports", userReports);
		return map;
	}
}
