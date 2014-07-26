package com.qubaopen.survey.controller.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.UserContact
import com.qubaopen.survey.repository.user.UserContactRepository

@RestController
@RequestMapping("userContacts")
public class UserContactController extends AbstractBaseController<UserContact, Long> {

	@Autowired
	UserContactRepository userContactRepository

	@Override
	protected MyRepository<UserContact, Long> getRepository() {
		userContactRepository
	}

}
