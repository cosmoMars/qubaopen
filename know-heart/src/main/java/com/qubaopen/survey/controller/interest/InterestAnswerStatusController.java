package com.qubaopen.survey.controller.interest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.interest.InterestAnswerStatus;
import com.qubaopen.survey.repository.interest.InterestAnswerStatusRepository;

@RestController
@RequestMapping("interestAnswerStatus")
public class InterestAnswerStatusController extends AbstractBaseController<InterestAnswerStatus, Long> {

	@Autowired
	private InterestAnswerStatusRepository interestAnswerStatusRepository;

	@Override
	protected MyRepository<InterestAnswerStatus, Long> getRepository() {
		return interestAnswerStatusRepository;
	}

}
