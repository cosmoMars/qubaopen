package com.qubaopen.survey.repository.survey;

import java.util.List;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.survey.Survey;
import com.qubaopen.survey.entity.survey.SurveyQuestion;

public interface SurveyQuestionRepository extends MyRepository<SurveyQuestion, Long> {

	List<SurveyQuestion> findAllBySurvey(Survey survey);

}
