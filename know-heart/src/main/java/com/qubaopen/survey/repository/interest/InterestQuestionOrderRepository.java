package com.qubaopen.survey.repository.interest;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.interest.InterestQuestionOrder;

public interface InterestQuestionOrderRepository extends MyRepository<InterestQuestionOrder, Long> {

//	@Query("from InterestQuestionOrder iqo where iqo.interestQuestion in (:interestQuestions)")
//	List<InterestQuestionOrder> findByInterestQuestion(@Param("interestQuestions") List<InterestQuestion> interestQuestions);
}
