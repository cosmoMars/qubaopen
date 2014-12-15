package com.qubaopen.survey.repository.survey;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.survey.SurveyLogic;
import com.qubaopen.survey.entity.survey.SurveyQuestion;

public interface SurveyLogicRepository extends MyRepository<SurveyLogic, Long> {

	@Query("from SurveyLogic sl where sl.surveyQuestion in (:surveyQuestions)")
	List<SurveyLogic> findAlLBySurveyQuestions(@Param("surveyQuestions") List<SurveyQuestion> surveyQuestions);

}
