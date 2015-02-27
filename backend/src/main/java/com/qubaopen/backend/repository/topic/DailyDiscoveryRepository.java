package com.qubaopen.backend.repository.topic;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.topic.DailyDiscovery;

public interface DailyDiscoveryRepository extends MyRepository<DailyDiscovery, Long> {

	@Query("from DailyDiscovery dd order by dd.time asc")
	List<DailyDiscovery> findDailyDiscoveryOrderByTimeAsc();
	
	@Query("from DailyDiscovery dd order by dd.time desc")
	List<DailyDiscovery> findDailyDiscoveryOrderByTimeDesc();
	
	@Query("from DailyDiscovery dd where dd.time = (select max(d.time) from DailyDiscovery d)")
	DailyDiscovery findByMaxTime();

}