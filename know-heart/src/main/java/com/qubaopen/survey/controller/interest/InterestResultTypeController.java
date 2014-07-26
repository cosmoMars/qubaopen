package com.qubaopen.survey.controller.interest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.interest.InterestResultType;
import com.qubaopen.survey.repository.interest.InterestResultTypeRepository;

@RestController
@RequestMapping("interestResultTypes")
public class InterestResultTypeController extends AbstractBaseController<InterestResultType, Long> {

	@Autowired
	private InterestResultTypeRepository interestResultTypeRepository;

	@Override
	protected MyRepository<InterestResultType, Long> getRepository() {
		return interestResultTypeRepository;
	}

}
