package com.knowheart3.repository.interest;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.interest.Interest;
import com.qubaopen.survey.entity.interest.InterestSpecialInsert;

import java.util.List;

public interface InterestSpecialInsertRepository extends MyRepository<InterestSpecialInsert, Long> {

	List<InterestSpecialInsert> findByInterest(Interest interest);
}
