package com.qubaopen.survey.repository.self;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.SelfResultOption;

public interface SelfResultOptionRepository extends MyRepository<SelfResultOption, Long> {

	@Query("from SelfReusultOption sro where sro.name in (:name)")
	List<SelfResultOption> findAllByName(@Param("name") String name);
}
