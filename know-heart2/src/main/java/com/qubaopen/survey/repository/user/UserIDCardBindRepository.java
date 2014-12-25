package com.qubaopen.survey.repository.user;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.User;
import com.qubaopen.survey.entity.user.UserIDCard;
import com.qubaopen.survey.entity.user.UserIDCardBind;

public interface UserIDCardBindRepository extends MyRepository<UserIDCardBind, Long> {

	UserIDCardBind findByUserId(long userId);

	UserIDCardBind findByUser(User user);
	
	UserIDCardBind findByUserIDCard(UserIDCard userIDCard);
}
