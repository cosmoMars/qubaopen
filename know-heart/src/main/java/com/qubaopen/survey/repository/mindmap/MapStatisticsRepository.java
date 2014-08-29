package com.qubaopen.survey.repository.mindmap;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.mindmap.MapStatistics;
import com.qubaopen.survey.entity.self.Self;
import com.qubaopen.survey.entity.user.User;

public interface MapStatisticsRepository extends MyRepository<MapStatistics, Long> {

	MapStatistics findByUserAndSelf(User user, Self self);

	@Query("from MapStatistics ms where ms.recommendedValue = (select max(m.recommendedValue) from MapStatistics m)")
	List<MapStatistics> findByMaxRecommendedValue();
	
	@Query("from MapStatistics ms where ms not in (:maps)")
	List<MapStatistics> findMapWithoutExists(@Param("maps") List<MapStatistics> maps);
	
	@Query("from MapStatistics ms where ms.self in (:selfs)")
	List<MapStatistics> findMapBySelfs(@Param("selfs") List<Self> selfs);
	
	@Query("from MapStatistics ms where ms.self.selfGroup is not null")
	List<MapStatistics> findMapByGroupSelfs();
	
}
