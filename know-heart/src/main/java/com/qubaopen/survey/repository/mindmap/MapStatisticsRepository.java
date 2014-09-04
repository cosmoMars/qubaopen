package com.qubaopen.survey.repository.mindmap;

import java.util.List;

import org.hibernate.annotations.Parent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.mindmap.MapStatistics;
import com.qubaopen.survey.entity.self.Self;
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
	
	@Query("from MapStatistics ms where ms not in (:maps) and ms.selfManagementType = :type and ms.user = :user")
	List<MapStatistics> findMapWithoutExists(@Param("maps") List<MapStatistics> maps, @Param("type") SelfManagementType selfManagementType, @Param("user") User user);
	
	@Query("from MapStatistics ms where ms.self in (:selfs)")
	List<MapStatistics> findMapBySelfs(@Param("selfs") List<Self> selfs);
	
	@Query("from MapStatistics ms where ms.self.selfGroup is not null and ms.user = :user")
	List<MapStatistics> findMapByGroupSelfs(@Param("user") User user);
	
	@Query("from MapStatistics ms where ms.self.selfGroup is not null and ms.selfManagementType = :type and ms.user = :user")
	List<MapStatistics> findMapByGroupSelfs(@Param("type") SelfManagementType selfManagementType, @Param("user") User user);
	
	List<MapStatistics> findBySelfManagementTypeAndUser(SelfManagementType selfManagementType, User user);
	
	
}
