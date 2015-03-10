package com.knowheart3.repository.interest;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.interest.Interest;
import com.qubaopen.survey.entity.interest.InterestQuestionOrder;

import java.util.List;

public interface InterestQuestionOrderRepository extends MyRepository<InterestQuestionOrder, Long> {

//	@Query("from InterestQuestionOrder iqo where iqo.interestQuestion in (:interestQuestions)")
//	List<InterestQuestionOrder> findByInterestQuestion(@Param("interestQuestions") List<InterestQuestion> interestQuestions);

	List<InterestQuestionOrder> findByInterestAndNextQuestionId(Interest interest, long nextQuestionId);

	List<InterestQuestionOrder> findByInterest(Interest interest);
}
