package com.knowheart3.repository.mindmap;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.mindmap.MapStatisticsType;

public interface MapStatisticsTypeRepository extends MyRepository<MapStatisticsType, Long> {

	MapStatisticsType findByName(String name);
}
