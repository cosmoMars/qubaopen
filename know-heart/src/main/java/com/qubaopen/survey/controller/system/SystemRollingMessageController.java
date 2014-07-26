package com.qubaopen.survey.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.system.SystemRollingMessage;
import com.qubaopen.survey.repository.system.SystemRollingMessageRepository;

@RestController
@RequestMapping("systemRollingMessages")
public class SystemRollingMessageController extends AbstractBaseController<SystemRollingMessage, Long> {

	@Autowired
	private SystemRollingMessageRepository systemRollingMessageRepository;

	@Override
	protected MyRepository<SystemRollingMessage, Long> getRepository() {
		return systemRollingMessageRepository;
	}

}
