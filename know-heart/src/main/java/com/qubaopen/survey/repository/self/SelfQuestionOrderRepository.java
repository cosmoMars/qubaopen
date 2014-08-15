package com.qubaopen.survey.repository.self;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.SelfQuestionOrder;

public interface SelfQuestionOrderRepository extends MyRepository<SelfQuestionOrder, Long> {

//	@Query("from SelfQuestionOrder sqo where sqo.selfQuestion in (:questions)")
//	List<SelfQuestionOrder> findAllBySelfQuestions(@Param("questions") List<SelfQuestion> selfQuestions);

	@Query("from SelfQuestionOrder sqo where sqo.questionId in (:questionIds)")
	List<SelfQuestionOrder> findAllByQuestions(@Param("questionIds") long... questionIds);

	List<SelfQuestionOrder> findByQuestionId(@Param("questionId") long questionId);

}
