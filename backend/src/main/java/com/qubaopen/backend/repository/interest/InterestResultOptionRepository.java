package com.qubaopen.backend.repository.interest;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qubaopen.survey.entity.interest.InterestResult;
import com.qubaopen.survey.entity.interest.InterestResultOption;

public interface InterestResultOptionRepository extends JpaRepository<InterestResultOption, Long> {

	List<InterestResultOption> findAllByInterestResult(InterestResult interestResult);
}
