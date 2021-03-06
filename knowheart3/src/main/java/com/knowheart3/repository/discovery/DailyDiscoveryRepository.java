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

	List<DailyDiscovery> findBySelfId(long id, Pageable pageable);

	@Query("from DailyDiscovery dd where date_format(dd.time, '%Y-%m-%d') = date_format(:date, '%Y-%m-%d')")
	List<DailyDiscovery> findByTimeAndPageable(@Param("date") Date date, Pageable pageable);

	@Query("select count(*) from DailyDiscovery dd where date_format(dd.time, '%Y-%m-%d') < date_format(:date, '%Y-%m-%d')")
	int countByTime(@Param("date") Date date);


}
