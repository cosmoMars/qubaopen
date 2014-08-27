package com.qubaopen.survey.repository.self;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.Self;
import com.qubaopen.survey.repository.self.custom.SelfRepositoryCustom;

public interface SelfRepository extends MyRepository<Self, Long>, SelfRepositoryCustom {

	@Query("from Self s where s.managementType is not null")
	List<Self> findSelfWithoutManagement();

	// @Query("from Self s where s.managementType is not null and s.id not in (:ids) order by s.id asc limit 0,10")
	// List<Self> findSelfWithoutManagementByPage(@Param("ids") long...ids);

	@Query("from Self s where s not in :selfs and s.managementType is not null")
	List<Self> findSelfWithoutExistSelf(@Param("selfs") List<Self> selfs);

	List<Self> findByManagementType(Self.ManagementType managementType);

	@Query("from Self s where s.managementType != :managementType")
	List<Self> findByWithOutManagementType(@Param("managementType") Self.ManagementType managementType);

	Self findByManagementTypeAndIntervalTime(Self.ManagementType managementType, int intervalTime);

	List<Self> findBySelfTypeName(String name);

	@Query("from Self s where s not in (:selfs)")
	List<Self> findRandomSelfs(@Param("selfs") List<Self> selfs);
}
