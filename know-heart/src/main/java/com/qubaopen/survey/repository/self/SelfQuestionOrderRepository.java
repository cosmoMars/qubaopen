package com.qubaopen.survey.repository.self;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.SelfQuestionOrder;

public interface SelfQuestionOrderRepository extends MyRepository<SelfQuestionOrder, Long> {

//	@Query("from SelfQuestionOrder sqo where sqo.selfQuestion in (:questions)")
//	List<SelfQuestionOrder> findAllBySelfQuestions(@Param("questions") List<SelfQuestion> selfQuestions);
}
