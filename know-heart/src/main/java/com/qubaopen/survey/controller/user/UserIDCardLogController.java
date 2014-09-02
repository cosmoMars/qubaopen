package com.qubaopen.survey.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.UserIDCardLog;
import com.qubaopen.survey.repository.user.UserIDCardLogRepository;

@RestController
@RequestMapping("userIDCardLogs")
public class UserIDCardLogController extends AbstractBaseController<UserIDCardLog, Long> {

	@Autowired
	private UserIDCardLogRepository userIDCardLogRepository;

	@Override
	protected MyRepository<UserIDCardLog, Long> getRepository() {
		return userIDCardLogRepository;
	}

}
