package com.knowheart3.repository.self;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.Self;
import com.qubaopen.survey.entity.self.SelfQuestionOrder;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SelfQuestionOrderRepository extends MyRepository<SelfQuestionOrder, Long> {

//	@Query("from SelfQuestionOrder sqo where sqo.selfQuestion in (:questions)")
//	List<SelfQuestionOrder> findAllBySelfQuestions(@Param("questions") List<SelfQuestion> selfQuestions);

	@Query("from SelfQuestionOrder sqo where sqo.self = :self and sqo.questionId in (:questionIds)")
	List<SelfQuestionOrder> findByQuestionIds(@Param("self") Self self, @Param("questionIds") long... questionIds);

	List<SelfQuestionOrder> findBySelfAndQuestionId(@Param("self") Self self, @Param("questionId") long questionId);

	List<SelfQuestionOrder> findBySelf(Self self);

}
