package com.qubaopen.survey.repository.mindmap;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.mindmap.MapRecord;
import com.qubaopen.survey.entity.mindmap.MapStatistics;
import com.qubaopen.survey.entity.self.Self;
import com.qubaopen.survey.entity.user.User;

public interface MapRecordRepository extends MyRepository<MapRecord, Long> {

	int deleteByMapStatistics(MapStatistics mapStatistics);

	@Query("from MapRecord m where m.mapStatistics = :mapStatistics and m.createdDate in (select max(createdDate) from MapRecord mr where mr.mapStatistics = :mapStatistics group by DATE_FORMAT(mr.createdDate,'%Y-%m-%d'))")
	List<MapRecord> findEveryDayMapRecords(@Param("mapStatistics") MapStatistics mapStatistics);
	
	@Query("select count(*) from MapRecord m where m.mapStatistics = :mapStatistics and m.createdDate in (select max(createdDate) from MapRecord mr where mr.mapStatistics = :mapStatistics group by DATE_FORMAT(mr.createdDate,'%Y-%m-%d'))")
	int countEveryDayMapRecords(@Param("mapStatistics") MapStatistics mapStatistics);
	
	@Query("from MapRecord m where m.createdDate = (select max(mr.createdDate) from MapRecord mr where mr.mapStatistics.user = :user and mr.mapStatistics.self = :self)")
	MapRecord findMaxRecordBySpecialSelf(@Param("self") Self self, @Param("user") User user);

	@Query("from MapRecord m where m.mapStatistics.id = (select ms.id from MapStatistics ms where ms.self = :self and ms.user = :user)")
	List<MapRecord> findBySelfAndUser(@Param("self") Self self, @Param("user") User user);
	
}
