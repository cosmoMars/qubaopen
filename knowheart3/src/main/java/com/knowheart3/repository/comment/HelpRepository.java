package com.knowheart3.repository.comment;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.comment.Help;
import com.qubaopen.survey.entity.user.User;

public interface HelpRepository extends MyRepository<Help, Long> {
	
	@Query("from Help h where h.user = :user and h.id not in (:ids)")
	List<Help> findByUser(@Param("user") User user, @Param("ids") List<Long> ids, Pageable pageable);
	
}
