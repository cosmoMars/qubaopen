package com.knowheart3.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.knowheart3.repository.system.SystemVersionRepository
import com.qubaopen.survey.entity.system.SystemVersion

@Service
class SystemVersionService {
	@Autowired
	SystemVersionRepository systemVersionRepository

	@Transactional
	SystemVersion getUrl(String type, String version, int resource){
		
		def sv = systemVersionRepository.findByTypeAndObjectIdx(SystemVersion.Type.values()[type as int], SystemVersion.UseObject.values()[resource])
		
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
