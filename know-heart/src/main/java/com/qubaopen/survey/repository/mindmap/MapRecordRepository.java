package com.qubaopen.survey.repository.mindmap;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.mindmap.MapRecord;
import com.qubaopen.survey.entity.mindmap.MapStatistics;

public interface MapRecordRepository extends MyRepository<MapRecord, Long> {
	
	int deleteByMapStatistics(MapStatistics mapStatistics);

}
