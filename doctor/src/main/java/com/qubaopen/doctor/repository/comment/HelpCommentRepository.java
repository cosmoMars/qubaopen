package com.qubaopen.doctor.repository.comment;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.doctor.repository.comment.custom.HelpCommentRepositoryCustom;
import com.qubaopen.survey.entity.comment.Help;
import com.qubaopen.survey.entity.comment.HelpComment;
import com.qubaopen.survey.entity.doctor.Doctor;

public interface HelpCommentRepository extends MyRepository<HelpComment, Long>, HelpCommentRepositoryCustom {

	@Query("from HelpComment hc where hc.help = :help order by hc.createdDate asc")
	List<HelpComment> findByHelp(@Param("help") Help help);
	
	@Query("from HelpComment hc where hc.help = :help order by hc.createdDate asc")
	List<HelpComment> findByHelp(@Param("help") Help help, Pageable pageable);
	
	@Query("from HelpComment hc where hc.help = :help and hc.id not in (:ids) order by hc.createdDate asc")
	List<HelpComment> findByHelp(@Param("help") Help Help, @Param("ids") List<Long> ids, Pageable pageable);
	
	@Query("from HelpComment hc where hc.help = :help and hc.doctor = :doctor order by hc.createdDate asc")
	List<HelpComment> findByHelpAndDoctor(@Param("help") Help help, @Param("doctor") Doctor doctor);
}
