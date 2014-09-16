package com.qubaopen.backend.repository.interest;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qubaopen.survey.entity.interest.Interest;
import com.qubaopen.survey.entity.interest.InterestResult;

public interface InterestResultRepository extends JpaRepository<InterestResult, Long> {

	InterestResult findOneByInterest(Interest interest);
}
