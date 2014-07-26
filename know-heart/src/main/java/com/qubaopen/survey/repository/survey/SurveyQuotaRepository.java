package com.qubaopen.survey.repository.survey;

import java.util.List;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.survey.SurveyQuota;

public interface SurveyQuotaRepository extends MyRepository<SurveyQuota, Long> {

	 List<SurveyQuota> findAllByIsActivated(boolean isActivated);
}
