package com.qubaopen.backend.repository.interest;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qubaopen.survey.entity.interest.Interest;
import com.qubaopen.survey.entity.interest.InterestQuestion;

public interface InterestQuestionRepository extends JpaRepository<InterestQuestion, Long> {

	List<InterestQuestion> findByInterest(Interest interest);
}
