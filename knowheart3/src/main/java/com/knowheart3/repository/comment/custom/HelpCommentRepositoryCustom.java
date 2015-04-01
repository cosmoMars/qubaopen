package com.knowheart3.repository.comment.custom;


import com.qubaopen.survey.entity.comment.Help;
import com.qubaopen.survey.entity.comment.HelpComment;
import com.qubaopen.survey.entity.vo.HelpCommentVo;

import java.util.List;

public interface HelpCommentRepositoryCustom {

	List<HelpComment> findLimitComment(Help help);

	List<HelpCommentVo> findLimitCommentByGood(Help help);

}
