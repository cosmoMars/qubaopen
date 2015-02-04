package com.qubaopen.survey.repository.user;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.User;
import com.qubaopen.survey.entity.user.UserMood;
import com.qubaopen.survey.repository.user.custom.UserMoodRepositoryCustom;

public interface UserMoodRepository extends MyRepository<UserMood, Long>, UserMoodRepositoryCustom {

	@Query("from UserMood suq where suq.user = :user and suq.id = (select max(s.id) from UserMood s where s.user = :user ) ")
	UserMood findLastByTime(@Param("user") User user);
	
//	select * from user_mood um where user_id =6 and 
//			um.last_time in (select max(u.last_time) from user_mood u where user_id =6 group by dayofmonth(u.last_time))
	@Query("from UserMood um where um.user = :user and um.lastTime in (select max(u.lastTime) from UserMood u where u.user = :user and month(u.lastTime) = :month group by dayofmonth(u.lastTime))")
	List<UserMood> retrieveUserMoodByMonth(@Param("user") User user, @Param("month") int month);
	
	//DATE_FORMAT(b.time,'%Y-%m-%d %H:%i')
	@Query("from UserMood um where um.user = :user and date_format(um.lastTime, '%Y-%m') = date_format(:month, '%Y-%m') order by um.lastTime desc")
	List<UserMood> findMonthMood(@Param("month") Date month, @Param("user") User user);
}
