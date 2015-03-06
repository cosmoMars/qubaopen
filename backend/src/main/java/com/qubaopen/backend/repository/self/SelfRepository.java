package com.qubaopen.backend.repository.self;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.Self;

public interface SelfRepository extends MyRepository<Self, Long> {

	@Query("from Self s where s.id not in (select dd.self.id from DailyDiscovery dd) order by s.createdDate desc")
	List<Self> findSelfOrderByCreatedDateDesc(Pageable pageable);
}
