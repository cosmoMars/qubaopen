package com.qubaopen.backend.repository.genre;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.doctor.TargetUser;

public interface TargetUserRepository extends MyRepository<TargetUser, Long> {
	@Query("from TargetUser t where t.id in (:ids)")
	List<TargetUser> findByIds(@Param("ids")List<Long> ids);
}
