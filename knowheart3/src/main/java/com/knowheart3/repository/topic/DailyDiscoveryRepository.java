package com.knowheart3.repository.topic;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.topic.DailyDiscovery;

public interface DailyDiscoveryRepository extends MyRepository<DailyDiscovery, Long> {

	@Query("from DailyDiscovery dd where date_format(dd.time, '%Y-%m-%d') = date_format(:date, '%Y-%m-%d')")
	DailyDiscovery findByTime(@Param("date") Date date);

}
