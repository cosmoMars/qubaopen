package com.qubaopen.survey.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.UserFriend;
import com.qubaopen.survey.repository.user.UserFriendRepository;

@RestController
@RequestMapping("userFriends")
public class UserFriendController extends AbstractBaseController<UserFriend, Long> {

	@Autowired
	private UserFriendRepository userFriendRepository;

	@Override
	protected MyRepository<UserFriend, Long> getRepository() {
		return userFriendRepository;
	}

}
