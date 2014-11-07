package com.qubaopen.survey.repository.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.SelfUserQuestionnaire;
import com.qubaopen.survey.entity.user.User;
import com.qubaopen.survey.entity.user.UserMood;
import com.qubaopen.survey.repository.user.custom.UserMoodRepositoryCustom;

public interface UserMoodRepository extends MyRepository<UserMood, Long>, UserMoodRepositoryCustom {

	@Query("from UserMood suq where suq.user = :user and suq.id = (select max(s.id) from UserMood s where s.user = :user ) ")
	UserMood findLastByTime(@Param("user") User user);
}
