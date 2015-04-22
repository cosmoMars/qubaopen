package com.qubaopen.doctor.repository.comment;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.comment.HelpComment;
import com.qubaopen.survey.entity.comment.HelpCommentGood;
import com.qubaopen.survey.entity.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HelpCommentGoodRepository extends MyRepository<HelpCommentGood, Long> {

	HelpCommentGood findByUserAndHelpComment(User user, HelpComment helpComment);
	
	int countByHelpComment(HelpComment helpComment);

	@Query("from HelpCommentGood hcg where hcg.id in (:ids)")
	List<HelpCommentGood> findByIds(@Param("ids") List<Long> ids);
}
