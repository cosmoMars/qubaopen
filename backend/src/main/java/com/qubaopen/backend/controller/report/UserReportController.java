package com.qubaopen.backend.controller.report;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.backend.repository.user.UserRepository;
import com.qubaopen.backend.vo.UserReport;

@RestController
@RequestMapping("userReport")
public class UserReportController {
	
	@Autowired
	private UserRepository userRepository;

	private static final Logger logger = LoggerFactory.getLogger(UserReportController.class);
	
	@RequestMapping(value = "getUserReports", method = RequestMethod.GET)
	public List<UserReport> getUserReports(){
		logger.trace(" -- 用户统计 -- ");
		List<UserReport> userReports = userRepository.calUserReports();
		return userReports;
	}
}
