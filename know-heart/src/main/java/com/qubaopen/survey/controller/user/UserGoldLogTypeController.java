package com.qubaopen.survey.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.UserGoldLogType;
import com.qubaopen.survey.repository.user.UserGoldLogTypeRepository;

@RestController
@RequestMapping("userGoldlogTypes")
public class UserGoldLogTypeController extends AbstractBaseController<UserGoldLogType, Long> {

	@Autowired
	private UserGoldLogTypeRepository userGoldLogTypeRepository;

	@Override
	protected MyRepository<UserGoldLogType, Long> getRepository() {
		return userGoldLogTypeRepository;
	}

}
