package com.qubaopen.doctor.repository.comment;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.comment.HelpComment;
import com.qubaopen.survey.entity.comment.HelpCommentGood;
import com.qubaopen.survey.entity.user.User;

public interface HelpCommentGoodRepository extends MyRepository<HelpCommentGood, Long> {

	HelpCommentGood findByUserAndHelpComment(User user, HelpComment helpComment);
	
	int countByHelpComment(HelpComment helpComment);
}