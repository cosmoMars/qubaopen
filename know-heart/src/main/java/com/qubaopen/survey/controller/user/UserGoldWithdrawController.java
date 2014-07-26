package com.qubaopen.survey.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.UserGoldWithdraw;
import com.qubaopen.survey.repository.user.UserGoldWithdrawRepository;

@RestController
@RequestMapping("userGoldWithdraws")
public class UserGoldWithdrawController extends AbstractBaseController<UserGoldWithdraw, Long> {

	@Autowired
	private UserGoldWithdrawRepository userGoldWithdrawRepository;

	@Override
	protected MyRepository<UserGoldWithdraw, Long> getRepository() {
		return userGoldWithdrawRepository;
	}

}
