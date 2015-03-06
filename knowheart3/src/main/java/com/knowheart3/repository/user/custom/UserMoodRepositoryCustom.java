package com.knowheart3.repository.user.custom;

import org.springframework.data.repository.query.Param;

import com.qubaopen.survey.entity.user.UserMood;

public interface UserMoodRepositoryCustom {
	
	UserMood getLastUserMood(@Param("userId") long userId);

}
