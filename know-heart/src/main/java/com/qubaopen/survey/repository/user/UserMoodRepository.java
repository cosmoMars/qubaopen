package com.qubaopen.survey.repository.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.User;
import com.qubaopen.survey.entity.user.UserMood;

public interface UserMoodRepository  extends MyRepository<UserMood, Long>{
	@Query("from UserMood where user = :user order by createdDate desc")
	UserMood getLastUserMood(@Param("user") User user);
	
	@Query(nativeQuery=true,value=("SELECT * FROM survey.user_mood where user_id=:userId order by created_date desc limit 0,1"))
	UserMood getLastUserMood(@Param("userId") long userId);
}
