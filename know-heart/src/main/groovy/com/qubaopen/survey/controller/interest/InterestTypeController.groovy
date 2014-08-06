package com.qubaopen.survey.controller.interest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.interest.InterestType
import com.qubaopen.survey.repository.interest.InterestTypeRepository

@RestController
@RequestMapping('interestTypes')
public class InterestTypeController extends AbstractBaseController<InterestType, Long> {

	@Autowired
	InterestTypeRepository interestTypeRepository

	@Override
	protected MyRepository<InterestType, Long> getRepository() {
		interestTypeRepository
	}

}
