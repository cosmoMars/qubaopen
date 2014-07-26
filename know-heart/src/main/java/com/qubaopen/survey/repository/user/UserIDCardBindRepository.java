package com.qubaopen.survey.repository.user;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.UserIDCardBind;

public interface UserIDCardBindRepository extends MyRepository<UserIDCardBind, Long> {

	UserIDCardBind findByUserId(long userId);
}
