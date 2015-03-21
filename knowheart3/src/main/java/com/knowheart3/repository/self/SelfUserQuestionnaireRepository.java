package com.knowheart3.repository.self;

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
	
	@Query("from SelfUserQuestionnaire suq where suq.user = :user and suq.self.selfManagementType.id = :typeId and suq.used = true and suq.time in (select max(s.time) from SelfUserQuestionnaire s where s.user = :user and s.used = true group by s.self) order by suq.time desc")
	List<SelfUserQuestionnaire> findByMaxTimeAndType(@Param("user") User user, @Param("typeId") long typeId);
	
	@Query("from SelfUserQuestionnaire suq where suq.user = :user and suq.self.analysis = :analysis and suq.used = true group by self_id ")
	List<SelfUserQuestionnaire> findByAnalysis(@Param("user") User user,@Param("analysis") boolean analysis);

	@Query("from SelfUserQuestionnaire sq where sq.self = :self and sq.time = (select max(s.time) from SelfUserQuestionnaire s where s.user = :user and s.used = true and s.self = :self ) ")
	SelfUserQuestionnaire findRecentQuestionnarie(@Param("user") User user, @Param("self") Self self);
	
	@Query("from SelfUserQuestionnaire suq where suq.self.id in (10,13,14) and suq.user = :user and suq.used = true and suq.time in (select max(s.time) from SelfUserQuestionnaire s where s.user = :user and s.used = true group by s.self) order by suq.time desc")
	List<SelfUserQuestionnaire> findByMentalStatus(@Param("user") User user);

	@Query("from SelfUserQuestionnaire suq where suq.user = :user and suq.used = true  and suq.id = (select max(s.id) from SelfUserQuestionnaire s where s.user = :user and s.used = true) ")
	SelfUserQuestionnaire findLastByTime(@Param("user") User user);
	
	@Query("from SelfUserQuestionnaire suq where suq.user = :user and suq.used = true  and suq.id = (select max(s.id) from SelfUserQuestionnaire s where s.user = :user and s.used = true) and suq.self.selfManagementType.id = 2l")
	SelfUserQuestionnaire findLastByTimeAndSelfManagementType(@Param("user") User user);
	
	SelfUserQuestionnaire findByUserAndSelfAndUsed(User user, Self self, boolean used);
	
	List<SelfUserQuestionnaire> findBySelfAndUserOrderByTimeAsc(Self self, User user);
	
	@Query("from SelfUserQuestionnaire suq where DATE_FORMAT(suq.time,'%Y-%m-%d') = :time and suq.self.selfManagementType.id = :typeId and suq.self != :self and suq.used = true and suq.user = :user")
	List<SelfUserQuestionnaire> findByTimeAndTypeIdWithOutSpecial(@Param("time") String time, @Param("typeId") long typeId, @Param("self") Self self, @Param("user") User user);
	
	int countByUser(User user);

    int countBySelfAndUser(Self self, User user);

    SelfUserQuestionnaire findByUserAndUsed(User user, Boolean used);
	
}
