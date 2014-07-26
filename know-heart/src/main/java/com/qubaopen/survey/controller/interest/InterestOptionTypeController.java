package com.qubaopen.survey.controller.interest;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.interest.InterestOptionType;
import com.qubaopen.survey.repository.interest.InterestOptionTypeRepository;

@RestController
@RequestMapping("interestOptionTypes")
public class InterestOptionTypeController extends AbstractBaseController<InterestOptionType, Long> {

	@Autowired
	private InterestOptionTypeRepository interestOptionTypeRepository;

	@Override
	protected MyRepository<InterestOptionType, Long> getRepository() {
		return interestOptionTypeRepository;
	}

}
