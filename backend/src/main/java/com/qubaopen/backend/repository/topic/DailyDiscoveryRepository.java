package com.qubaopen.backend.repository.topic;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.topic.DailyDiscovery;

public interface DailyDiscoveryRepository extends MyRepository<DailyDiscovery, Long> {

	@Query("from DailyDiscovery dd order by dd.time asc")
	List<DailyDiscovery> findDailyDiscoveryOrderByTimeAsc(Pageable pageable);
	
	@Query("from DailyDiscovery dd order by dd.time desc")
	List<DailyDiscovery> findDailyDiscoveryOrderByTimeDesc(Pageable pageable);
	
	@Query("from DailyDiscovery dd where dd.time = (select max(d.time) from DailyDiscovery d)")
	DailyDiscovery findByMaxTime();
	
	@Query("from DailyDiscovery dd where date_format(dd.time, '%Y-%m-%d') = date_format(:time, '%Y-%m-%d')")
	DailyDiscovery findByTime(@Param("time") Date time);

}
