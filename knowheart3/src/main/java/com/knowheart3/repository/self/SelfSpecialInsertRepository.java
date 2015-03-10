package com.knowheart3.repository.self;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.Self;
import com.qubaopen.survey.entity.self.SelfSpecialInsert;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SelfSpecialInsertRepository extends MyRepository<SelfSpecialInsert, Long> {

	List<SelfSpecialInsert> findAllBySelf(Self self);

	@Query("select ssi.questionId from SelfSpecialInsert ssi where ssi.self = :self and ssi.specialQuestionId = :questionId")
	long findBySelfAndSpecialQuestionId(@Param("self") Self self, @Param("questionId") long questionId);
}
