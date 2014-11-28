package com.qubaopen.doctor.repository.comment;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.comment.Help;
import com.qubaopen.survey.entity.comment.HelpComment;

public interface HelpCommentRepository extends MyRepository<HelpComment, Long> {

	@Query("from HelpComment hc where hc.help = :help order by hc.createdDate asc")
	List<HelpComment> findByHelp(@Param("help") Help help);
}
