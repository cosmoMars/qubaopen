package com.qubaopen.doctor.repository.comment.custom;

import java.util.List;

import com.qubaopen.survey.entity.comment.Help;
import com.qubaopen.survey.entity.comment.HelpComment;

public interface HelpCommentRepositoryCustom {

	List<HelpComment> findLimitComment(Help help);
	
}
