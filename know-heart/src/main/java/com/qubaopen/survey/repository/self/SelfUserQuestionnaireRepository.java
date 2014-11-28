package com.qubaopen.survey.repository.self;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.Self;
import com.qubaopen.survey.entity.self.SelfUserQuestionnaire;
import com.qubaopen.survey.entity.user.User;

public interface SelfUserQuestionnaireRepository extends MyRepository<SelfUserQuestionnaire, Long> {

	@Query("from SelfUserQuestionnaire suq where suq.user = :user and suq.used = true and suq.time in (select max(s.time) from SelfUserQuestionnaire s where s.user = :user and s.used = true group by s.self) order by suq.time desc")
	List<SelfUserQuestionnaire> findByMaxTime(@Param("user") User user);
	
	@Query("from SelfUserQuestionnaire suq where suq.user = :user and suq.self.analysis = :analysis  and suq.used = true group by self_id ")
	List<SelfUserQuestionnaire> findByAnalysis(@Param("user") User user,@Param("analysis") boolean analysis);

	@Query("from SelfUserQuestionnaire sq where sq.self = :self and sq.time = (select max(s.time) from SelfUserQuestionnaire s where s.user = :user and s.used = true and s.self = :self ) ")
	SelfUserQuestionnaire findRecentQuestionnarie(@Param("user") User user, @Param("self") Self self);
	
	@Query("from SelfUserQuestionnaire suq where suq.self.id in (10,13,14) and suq.user = :user and suq.used = true and suq.time in (select max(s.time) from SelfUserQuestionnaire s where s.user = :user and s.used = true group by s.self) order by suq.time desc")
	List<SelfUserQuestionnaire> findByMentalStatus(@Param("user") User user);

	@Query("from SelfUserQuestionnaire suq where suq.user = :user and suq.used = true  and suq.id = (select max(s.id) from SelfUserQuestionnaire s where s.user = :user and s.used = true) ")
	SelfUserQuestionnaire findLastByTime(@Param("user") User user);
	
	@Query("from SelfUserQuestionnaire suq where suq.user = :user and suq.used = true  and suq.id = (select max(s.id) from SelfUserQuestionnaire s where s.user = :user and s.used = true) and suq.self.selfManagementType.id = 2l")
	SelfUserQuestionnaire findLastByTimeAndSelfManagementType(@Param("user") User user);
	
}
