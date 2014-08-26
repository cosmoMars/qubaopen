package com.qubaopen.survey.repository.user.custom;

import org.springframework.data.repository.query.Param;

import com.qubaopen.survey.entity.user.UserMood;

public interface UserMoodRepositoryCustom {
	
	UserMood getLastUserMood(@Param("userId") long userId);

}
