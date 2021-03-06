package com.qubaopen.doctor.repository.selfUserQuestionnaire;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.SelfUserQuestionnaire;
import com.qubaopen.survey.entity.user.User;

public interface SelfUserQuestionnaireRepository extends MyRepository<SelfUserQuestionnaire, Long> {

	@Query("from SelfUserQuestionnaire suq where suq.user = :user and suq.used = true and suq.time in (select max(s.time) from SelfUserQuestionnaire s where s.user = :user and s.used = true group by s.self) order by suq.time desc")
	List<SelfUserQuestionnaire> findByMaxTime(@Param("user") User user);
	
	@Query("from SelfUserQuestionnaire suq where suq.user = :user and suq.used = true and suq.self.selfManagementType.id = :typeId and suq.time in (select max(s.time) from SelfUserQuestionnaire s where s.user = :user and s.used = true group by s.self) order by suq.time desc")
	List<SelfUserQuestionnaire> findByMaxTime(@Param("user") User user, @Param("typeId") long typeId);
}
