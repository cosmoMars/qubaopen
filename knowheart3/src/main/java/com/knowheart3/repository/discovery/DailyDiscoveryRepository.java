package com.knowheart3.repository.discovery;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.topic.DailyDiscovery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface DailyDiscoveryRepository extends MyRepository<DailyDiscovery, Long> {

	@Query("from DailyDiscovery dd where date_format(dd.time, '%Y-%m-%d') = date_format(:date, '%Y-%m-%d')")
	DailyDiscovery findByTime(@Param("date") Date date);

//    @Query("from DailyDiscovery dd order by dd.time desc")
//    List<DailyDiscovery> findOneByIndex(Pageable pageable);

}
