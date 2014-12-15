package com.qubaopen.survey.controller.self;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.SelfUserQuestionnaire;
import com.qubaopen.survey.repository.self.SelfUserQuestionnaireRepository;

@RestController
@RequestMapping("selfUserQuestionnaires")
public class SelfUserQuestionnaireController extends AbstractBaseController<SelfUserQuestionnaire, Long> {

	@Autowired
	private SelfUserQuestionnaireRepository selfUserQuestionnaireRepository;

	@Override
	protected MyRepository<SelfUserQuestionnaire, Long> getRepository() {
		return selfUserQuestionnaireRepository;
	}

}
