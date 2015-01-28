package com.qubaopen.survey.service.system

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qubaopen.survey.entity.system.SystemVersion;
import com.qubaopen.survey.repository.system.SystemVersionRepository;

@Service
class SystemVersionService {
	@Autowired
	SystemVersionRepository systemVersionRepository

	@Transactional
	SystemVersion getUrl(String type, String version){
		
		def sv = systemVersionRepository.findByTypeAndObjectIdx(SystemVersion.Type.values()[type as int], SystemVersion.UseObject.User)
		
		def v = version as double
		
		if (sv) {
			def sVersion = sv.version as double
			if (sVersion > v) {
				return sv
			}
		}
		return null;
	}
}
