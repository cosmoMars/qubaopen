package com.qubaopen.survey.repository.interest;

import java.util.List;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.interest.Interest;
import com.qubaopen.survey.entity.interest.InterestQuestion;

public interface InterestQuestionRepository extends MyRepository<InterestQuestion, Long> {

	List<InterestQuestion> findByInterest(Interest interest);
}
