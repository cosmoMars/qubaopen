package com.qubaopen.survey.repository.user;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.UserHelp;
import com.qubaopen.survey.entity.user.UserHelpComment;

public interface UserHelpCommentRepository extends MyRepository<UserHelpComment, Long> {
	
	@Query("from UserHelpComment uhc where uhc.userHelp = :help order by uhc.createdDate asc")
	List<UserHelpComment> findByUserHelp(@Param("help") UserHelp userHelp);
}
