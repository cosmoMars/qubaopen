package com.knowheart3.repository.user;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.User;
import com.qubaopen.survey.entity.user.UserMoodRecord;

public interface UserMoodRecordRepository extends MyRepository<UserMoodRecord, Long> {

	@Query("from UserMoodRecord umr where umr.user = :user and date_format(umr.time, '%Y-%m-%d') >= date_format(:time, '%Y-%m-%d') order by umr.time asc")
	List<UserMoodRecord> findByUserAndTime(@Param("user") User user, @Param("time") Date time);

	@Query("from UserMoodRecord umr where umr.user = :user and umr.time = (select max(r.time) FROM UserMoodRecord r where r.user = :user)")
	UserMoodRecord findByMaxTime(@Param("user") User user);

	// List<UserMoodRecord> findByUserOrderByTimeAsc();
}
