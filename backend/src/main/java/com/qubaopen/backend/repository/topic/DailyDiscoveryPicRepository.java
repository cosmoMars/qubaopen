package com.qubaopen.backend.repository.topic;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.topic.DailyDiscovery;
import com.qubaopen.survey.entity.topic.DailyDiscoveryPic;

public interface DailyDiscoveryPicRepository  extends MyRepository<DailyDiscoveryPic, Long>{

	@Query("from DailyDiscoveryPic dd order by dd.createdDate asc")
	List<DailyDiscoveryPic> findDailyDiscoveryPicsOrderByTimeAsc(Pageable pageable);
	
	@Query("from DailyDiscoveryPic dd order by dd.createdDate desc")
	List<DailyDiscoveryPic> findDailyDiscoveryPicsOrderByTimeDesc(Pageable pageable);
	
	@Query("from DailyDiscoveryPic dd where date_format(dd.startTime, '%Y-%m-%d') = date_format(:time, '%Y-%m-%d')")
	DailyDiscoveryPic findByTime(@Param("time") Date time);
}
