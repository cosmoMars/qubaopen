package com.qubaopen.survey.repository.self;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.Self;

public interface SelfRepository extends MyRepository<Self, Long> {

	@Query("from Self s where s.managementType is not null")
	List<Self> findSelfWithoutManagement();


//	@Query("from Self s where s.managementType is not null and s.id not in (:ids) order by s.id asc limit 0,10")
//	List<Self> findSelfWithoutManagementByPage(@Param("ids") long...ids);
}
