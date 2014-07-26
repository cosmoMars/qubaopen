package com.qubaopen.survey.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.UserUDID;
import com.qubaopen.survey.repository.user.UserUDIDRepository;

@RestController
@RequestMapping("userUDIDs")
public class UserUDIDController extends AbstractBaseController<UserUDID, Long> {

	@Autowired
	private UserUDIDRepository userUDIDRepository;

	@Override
	protected MyRepository<UserUDID, Long> getRepository() {
		return userUDIDRepository;
	}

}
