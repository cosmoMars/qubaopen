package com.qubaopen.survey.controller.self;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.SelfGroup;
import com.qubaopen.survey.repository.self.SelfGroupRepository;

@RestController
@RequestMapping("selfGroups")
public class SelfGroupController extends AbstractBaseController<SelfGroup, Long> {

	@Autowired
	private SelfGroupRepository selfGroupRepository;

	@Override
	protected MyRepository<SelfGroup, Long> getRepository() {
		return selfGroupRepository;
	}

}
