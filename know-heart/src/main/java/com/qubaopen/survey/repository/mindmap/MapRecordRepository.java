package com.qubaopen.survey.repository.mindmap;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.mindmap.MapRecord;
import com.qubaopen.survey.entity.mindmap.MapStatistics;
import com.qubaopen.survey.repository.mindmap.custom.MapRecordRepositoryCustom;

public interface MapRecordRepository extends MyRepository<MapRecord, Long>, MapRecordRepositoryCustom{
	
	int deleteByMapStatistics(MapStatistics mapStatistics);
	
	@Query("from MapRecord mr where mr.mapStatistics = :mapStatistics group by DATE_FORMAT(mr.createdDate,'%Y-%m-%d') order by mr.createdDate desc")
	List<MapRecord> findEveryDayMapRecords(@Param("mapStatistics") MapStatistics mapStatistics);

}
