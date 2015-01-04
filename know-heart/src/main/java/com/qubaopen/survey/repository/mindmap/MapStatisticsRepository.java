package com.qubaopen.survey.repository.mindmap;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.mindmap.MapStatistics;
import com.qubaopen.survey.entity.self.Self;
import com.qubaopen.survey.entity.self.SelfGroup;
import com.qubaopen.survey.entity.self.SelfManagementType;
import com.qubaopen.survey.entity.user.User;

public interface MapStatisticsRepository extends MyRepository<MapStatistics, Long> {

	MapStatistics findByUserAndSelf(User user, Self self);

	@Query("from MapStatistics ms where ms.recommendedValue = (select max(m.recommendedValue) from MapStatistics m) and ms.user = :user")
	List<MapStatistics> findByMaxRecommendedValue(@Param("user") User user);
	
	@Query("from MapStatistics ms where ms.recommendedValue = (select max(m.recommendedValue) from MapStatistics m) and ms in (:maps) and ms.user = :user")
	List<MapStatistics> findByMaxRecommendedValue(@Param("maps") List<MapStatistics> maps, @Param("user") User user);
	
	@Query("from MapStatistics ms where ms not in (:maps) and ms.user = :user")
	List<MapStatistics> findMapWithoutExists(@Param("maps") List<MapStatistics> maps, @Param("user") User user);
	
	@Query("from MapStatistics ms where ms.user = :user")
	List<MapStatistics> findMapWithoutExists(@Param("user") User user);
	
	@Query("from MapStatistics ms where ms not in (:maps) and ms.selfManagementType = :type and ms.user = :user")
	List<MapStatistics> findMapWithoutExists(@Param("maps") List<MapStatistics> maps, @Param("type") SelfManagementType selfManagementType, @Param("user") User user);
	
	@Query("from MapStatistics ms where ms.self in (:selfs)")
	List<MapStatistics> findMapBySelfs(@Param("selfs") List<Self> selfs);
	
	@Query("from MapStatistics ms where ms.self.selfGroup is not null and ms.user = :user")
	List<MapStatistics> findMapByGroupSelfs(@Param("user") User user);
	
	@Query("from MapStatistics ms where ms.self.selfGroup is not null and ms.selfManagementType = :type and ms.user = :user")
	List<MapStatistics> findMapByGroupSelfs(@Param("type") SelfManagementType selfManagementType, @Param("user") User user);
	
	@Query("from MapStatistics ms where ms.selfManagementType = :type and ms.user = :user and ms.self.selfGroup != :specialGroup and ms.self.selfGroup.status = :status")
	List<MapStatistics> findMapWithoutSpecialGroup(@Param("type") SelfManagementType selfManagementType, @Param("user") User user, @Param("specialGroup") SelfGroup specialGroup, @Param("status") boolean status);
	
	@Query("from MapStatistics ms where ms.user = :user and ms.self.selfGroup != :specialGroup and ms.self.selfGroup.status = :status")
	List<MapStatistics> findMapWithoutSpecialGroup(@Param("user") User user, @Param("specialGroup") SelfGroup specialGroup, @Param("status") boolean status);
	
	List<MapStatistics> findBySelfManagementTypeAndUser(SelfManagementType selfManagementType, User user);
	
	
}
