package com.qubaopen.survey.controller.interest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.interest.InterestResult;
import com.qubaopen.survey.repository.interest.InterestResultRepository;

@RestController
@RequestMapping("interestResults")
public class InterestResultController extends AbstractBaseController<InterestResult, Long> {

	@Autowired
	private InterestResultRepository interestResultRepository;

	@Override
	protected MyRepository<InterestResult, Long> getRepository() {
		return interestResultRepository;
	}

}
