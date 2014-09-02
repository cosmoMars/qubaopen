package com.qubaopen.survey.repository.user;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.User;
import com.qubaopen.survey.entity.user.UserIDCardLog;

public interface UserIDCardLogRepository extends MyRepository<UserIDCardLog, Long> {

	
	@Query("from UserIDCardLog u where month(u.createdDate) = month(now()) and u.user = :user and u.status in ('0','1','2','local','used')")
	List<UserIDCardLog> findCurrentMonthIdCardLogByUser(@Param("user") User user);
	
}
