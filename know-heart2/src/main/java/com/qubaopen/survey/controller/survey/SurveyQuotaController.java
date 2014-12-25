package com.qubaopen.survey.controller.survey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.survey.SurveyQuota;
import com.qubaopen.survey.repository.survey.SurveyQuotaRepository;

@RestController
@RequestMapping("surveyQuotas")
public class SurveyQuotaController extends AbstractBaseController<SurveyQuota, Long> {

	@Autowired
	private SurveyQuotaRepository surveyQuotaRepository;

	@Override
	protected MyRepository<SurveyQuota, Long> getRepository() {
		return surveyQuotaRepository;
	}

}
