package com.qubaopen.survey.controller.interest;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.interest.InterestUserQuestionnaire;
import com.qubaopen.survey.repository.interest.InterestUserQuestionnaireRepository;

@RestController
@RequestMapping("interestUserQuestionnaires")
public class InterestUserQuestionnaireController extends AbstractBaseController<InterestUserQuestionnaire, Long> {

	@Autowired
	private InterestUserQuestionnaireRepository interestUserQuestionnaireRepository;
	
	@Override
	protected MyRepository<InterestUserQuestionnaire, Long> getRepository() {
		return interestUserQuestionnaireRepository;
	}

}
