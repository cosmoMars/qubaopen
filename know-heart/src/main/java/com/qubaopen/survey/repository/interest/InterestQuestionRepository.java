package com.qubaopen.survey.repository.interest;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.interest.Interest;
import com.qubaopen.survey.entity.interest.InterestQuestion;

public interface InterestQuestionRepository extends MyRepository<InterestQuestion, Long> {

//	@Query("from InterestQuestion iq left join fetch iq.interestQuestionOptions where iq.interest = :interest ")
	List<InterestQuestion> findByInterest(@Param("interest") Interest interest);

}
