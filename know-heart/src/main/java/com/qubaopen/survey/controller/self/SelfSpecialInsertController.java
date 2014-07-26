package com.qubaopen.survey.controller.self;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.SelfSpecialInsert;
import com.qubaopen.survey.repository.self.SelfSpecialInsertRepository;

@RestController
@RequestMapping("selfSpecialInserts")
public class SelfSpecialInsertController extends AbstractBaseController<SelfSpecialInsert, Long> {

	@Autowired
	private SelfSpecialInsertRepository selfSpecialInsertRepository;

	@Override
	protected MyRepository<SelfSpecialInsert, Long> getRepository() {
		return selfSpecialInsertRepository;
	}

}
