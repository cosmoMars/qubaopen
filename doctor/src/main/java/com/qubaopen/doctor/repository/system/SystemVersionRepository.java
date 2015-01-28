package com.qubaopen.doctor.repository.system;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.system.SystemVersion;
public interface SystemVersionRepository extends MyRepository<SystemVersion, Long> {
	
	SystemVersion findByType(SystemVersion.Type type);
	
	@Query("from SystemVersion sv where sv.publishTime = (select max(s.publishTime) from SystemVersion s where s.type = :type and s.useObject = :objectIdx) and sv.type = :type and sv.useObject = :objectIdx")
	SystemVersion findByTypeAndObjectIdx(@Param("type") SystemVersion.Type type, @Param("objectIdx") SystemVersion.UseObject objectIdx);
}
