package com.knowheart3.repository.comment;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.knowheart3.repository.comment.custom.HelpCommentRepositoryCustom;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.comment.Help;
import com.qubaopen.survey.entity.comment.HelpComment;

public interface HelpCommentRepository extends MyRepository<HelpComment, Long>, HelpCommentRepositoryCustom {

	@Query("from HelpComment hc where hc.help = :help order by hc.createdDate asc")
	List<HelpComment> findByHelp(@Param("help") Help help, Pageable pageable);
	
	@Query("from HelpComment hc where hc.help = :help and hc.id not in (:ids) order by hc.createdDate asc")
	List<HelpComment> findByHelp(@Param("help") Help Help, @Param("ids") List<Long> ids, Pageable pageable);

	int countByHelp(Help help);

	@Query("from HelpComment hc where hc.id in (:ids) ")
	List<HelpComment> findByIds(@Param("ids") List<Long> ids);
}
