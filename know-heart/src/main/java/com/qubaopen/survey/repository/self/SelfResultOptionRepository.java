package com.qubaopen.survey.repository.self;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.SelfResultOption;

public interface SelfResultOptionRepository extends MyRepository<SelfResultOption, Long> {

//	@Query("from SelfReusultOption sro where sro.name in (:name)")
//	List<SelfResultOption> findAllByName(@Param("name") String name);
}
