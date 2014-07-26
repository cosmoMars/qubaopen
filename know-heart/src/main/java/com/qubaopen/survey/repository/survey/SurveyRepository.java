package com.qubaopen.survey.repository.survey;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.survey.Survey;

public interface SurveyRepository extends MyRepository<Survey, Long> {

	@Query("from Survey s where s.quotas is empty")
	List<Survey> findSurveyWithoutQuota();

}
