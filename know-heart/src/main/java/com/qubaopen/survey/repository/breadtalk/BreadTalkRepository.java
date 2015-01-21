package com.qubaopen.survey.repository.breadtalk;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.bread.BreadTalk;

public interface BreadTalkRepository extends MyRepository<BreadTalk, Long> {

	List<BreadTalk> findByUsedOrderByIdAsc(boolean used, Pageable pageable);
	
	BreadTalk findByCode(String code);
}
