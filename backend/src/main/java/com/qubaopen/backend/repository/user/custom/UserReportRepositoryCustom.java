package com.qubaopen.backend.repository.user.custom;

import java.util.List;
import java.util.Map;

import com.qubaopen.backend.vo.UserReportVo;

public interface UserReportRepositoryCustom {
	
	/**
	 * @return 每日注册情况
	 */
	List<UserReportVo> countUserReports();
	
	/**
	 * @return 知心用户情况
	 */
	List<Map<String, Object>> countUserInfo();

	/**
	 * @return 每日最终心情
	 */
	List<Map<String, Object>> countFinalMood();
	
	/**
	 * @return 今日心情变化
	 */
	List<Map<String, Object>> countChangeMood();
	
	/**
	 * @return 今日心情
	 */
	List<Map<String, Object>> countTodayMood();
	
	/**
	 * @return 今日心情（汇总）
	 */
	List<Map<String, Object>> countDailyMood();
	
}
