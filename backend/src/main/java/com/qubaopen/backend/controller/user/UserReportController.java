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
	public Map<String, Object> getUserReports() {
		logger.trace(" -- 每日注册情况 -- ");
		List<UserReportVo> userReports = userRepository.countUserReports();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", "1");
		map.put("reports", userReports);
		return map;
	}

	@RequestMapping(value = "countUserInfo", method = RequestMethod.GET)
	public Map<String, Object> countUserInfo() {
		
		logger.trace(" -- 知心用户情况 -- ");
		List<Map<String, Object>> userInfoReports = userRepository.countUserInfo();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", "1");
		map.put("userInfoReports", userInfoReports);
		return map;
	}
	
	@RequestMapping(value = "countFinalMood", method = RequestMethod.GET)
	public Map<String, Object> countFinalMood() {
		
		logger.trace(" -- 每日最终心情 -- ");
		List<Map<String, Object>> finalMoodReports = userRepository.countFinalMood();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", "1");
		map.put("finalMoodReports", finalMoodReports);
		return map;
	}
	
	@RequestMapping(value = "conutChangeMood", method = RequestMethod.GET)
	public Map<String, Object> conutChangeMood() {
		logger.trace(" -- 今日心情变化 -- ");
		List<Map<String, Object>> changeMoodReports = userRepository.countChangeMood();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", "1");
		map.put("changeMoodReports", changeMoodReports);
		return map;
	}
	
	@RequestMapping(value = "conutTodayMood", method = RequestMethod.GET)
	public Map<String, Object> conutTodayMood() {
		logger.trace(" -- 今日心情 -- ");
		List<Map<String, Object>> todayMoodReports = userRepository.countTodayMood();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", "1");
		map.put("todayMoodReports", todayMoodReports);
		return map;
	}
	
	@RequestMapping(value = "countDailyMood", method = RequestMethod.GET)
	public Map<String, Object> countDailyMood() {
		logger.trace(" -- 今日心情（汇总） -- ");
		List<Map<String, Object>> dailyMoodReports = userRepository.countDailyMood();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", "1");
		map.put("dailyMoodReports", dailyMoodReports);
		return map;
	}
}
