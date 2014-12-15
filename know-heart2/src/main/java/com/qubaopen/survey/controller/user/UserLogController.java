package com.qubaopen.survey.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.UserLog;
import com.qubaopen.survey.repository.user.UserLogRepository;

@RestController
@RequestMapping("userLogs")
public class UserLogController extends AbstractBaseController<UserLog, Long> {

	@Autowired
	private UserLogRepository userLogRepository;

	@Override
	protected MyRepository<UserLog, Long> getRepository() {
		return userLogRepository;
	}

}
