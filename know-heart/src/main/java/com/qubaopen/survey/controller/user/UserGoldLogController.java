package com.qubaopen.survey.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.UserGoldLog;
import com.qubaopen.survey.repository.user.UserGoldLogRepository;

@RestController
@RequestMapping("userGoldLogs")
public class UserGoldLogController extends AbstractBaseController<UserGoldLog, Long> {

	@Autowired
	private UserGoldLogRepository userGoldLogRepository;

	@Override
	protected MyRepository<UserGoldLog, Long> getRepository() {
		return userGoldLogRepository;
	}

}
