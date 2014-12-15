package com.qubaopen.survey.repository.self;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.Self;
import com.qubaopen.survey.entity.self.SelfSpecialInsert;

public interface SelfSpecialInsertRepository extends MyRepository<SelfSpecialInsert, Long> {

	List<SelfSpecialInsert> findAllBySelf(Self self);

	@Query("select ssi.questionId from SelfSpecialInsert ssi where ssi.self = :self and ssi.specialQuestionId = :questionId")
	long findBySelfAndSpecialQuestionId(@Param("self") Self self, @Param("questionId") long questionId);
}
