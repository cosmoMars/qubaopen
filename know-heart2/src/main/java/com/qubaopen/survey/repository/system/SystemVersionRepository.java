package com.qubaopen.survey.repository.system;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.system.SystemVersion;
public interface SystemVersionRepository extends MyRepository<SystemVersion, Long> {
	SystemVersion findByType(SystemVersion.Type type);
}
