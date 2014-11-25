package com.qubaopen.survey.repository.user;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.User;
import com.qubaopen.survey.entity.user.UserHelpComment;
import com.qubaopen.survey.entity.user.UserHelpCommentGood;

public interface UserHelpCommentGoodRepository extends MyRepository<UserHelpCommentGood, Long> {

	UserHelpCommentGood findByUserAndUserHelpComment(User user, UserHelpComment userHelpComment);
	
	int countByUserHelpComment(UserHelpComment userHelpComment);
}
