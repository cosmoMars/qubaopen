package com.qubaopen.survey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.QuestionnaireTagType;
import com.qubaopen.survey.repository.QuestionnaireTagTypeRepository;

@RestController
@RequestMapping("questionnaireTagTypes")
public class QuestionnaireTagTypeController extends AbstractBaseController<QuestionnaireTagType, Long> {

	@Autowired
	private QuestionnaireTagTypeRepository questionnaireTagTypeRepository;

	@Override
	protected MyRepository<QuestionnaireTagType, Long> getRepository() {
		return questionnaireTagTypeRepository;
	}

}
