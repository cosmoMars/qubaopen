package com.qubaopen.survey.repository.user;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.UserThird;

public interface ThirdUserRepository extends MyRepository<UserThird, Long> {

	UserThird findByToken(String token);
}
