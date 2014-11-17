package com.qubaopen.survey.repository.user;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.ThirdUser;

public interface ThirdUserRepository extends MyRepository<ThirdUser, Long> {

	ThirdUser findByToken(String token);
	
}
