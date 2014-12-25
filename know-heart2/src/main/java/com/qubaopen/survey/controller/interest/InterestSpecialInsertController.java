package com.qubaopen.survey.controller.interest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.interest.InterestSpecialInsert;
import com.qubaopen.survey.repository.interest.InterestSpecialInsertRepository;

@RestController
@RequestMapping("interestSpecialInserts")
public class InterestSpecialInsertController extends AbstractBaseController<InterestSpecialInsert, Long> {

	@Autowired
	private InterestSpecialInsertRepository interestSpecialInsertRepository;

	@Override
	protected MyRepository<InterestSpecialInsert, Long> getRepository() {
		return interestSpecialInsertRepository;
	}

}
