package com.qubaopen.survey.repository.self;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.Self;
import com.qubaopen.survey.entity.self.SelfManagementType;
import com.qubaopen.survey.repository.self.custom.SelfRepositoryCustom;

public interface SelfRepository extends MyRepository<Self, Long>, SelfRepositoryCustom {

	@Query("from Self s where s.selfManagementType is not null")
	List<Self> findSelfWithoutManagement();

	// @Query("from Self s where s.managementType is not null and s.id not in (:ids) order by s.id asc limit 0,10")
	// List<Self> findSelfWithoutManagementByPage(@Param("ids") long...ids);

	@Query("from Self s where s not in :selfs and s.selfManagementType is not null")
	List<Self> findSelfWithoutExistSelf(@Param("selfs") List<Self> selfs);

	@Query("from Self s where s not in (:selfs)")
	List<Self> findWithoutExists(@Param("selfs") List<Self> selfs);
	
	@Query("from Self s where s.selfManagementType = :selfManagementType and s not in (:exists)")
	List<Self> findByTypeWithoutExists(@Param("selfManagementType") SelfManagementType selfManagementType, @Param("exists") List<Self> exists);
	
	@Query("from Self s where s.recommendedValue = (select max(ss.recommendedValue) from Self ss)")
	Self findByMaxRecommendedValue();
}
