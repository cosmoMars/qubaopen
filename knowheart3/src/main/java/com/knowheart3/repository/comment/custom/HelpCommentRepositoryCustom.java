package com.knowheart3.repository.comment.custom;

import com.knowheart3.vo.HelpCommentVo;
import com.qubaopen.survey.entity.comment.Help;
import com.qubaopen.survey.entity.comment.HelpComment;

import java.util.List;

public interface HelpCommentRepositoryCustom {

	List<HelpComment> findLimitComment(Help help);

	List<HelpCommentVo> findLimitCommentByGood(Help help);

}
