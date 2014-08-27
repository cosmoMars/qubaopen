package com.qubaopen.survey.service.system

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qubaopen.survey.entity.system.SystemVersion;
import com.qubaopen.survey.repository.system.SystemVersionRepository;

@Service
class SystemVersionService {
	@Autowired
	SystemVersionRepository systemVersionRepository;

	@Transactional
	SystemVersion getUrl(String type,String version){
		def systemVersion;
		def v=Double.parseDouble(version);
		def path="";
		if(type.equals("0")){
			systemVersion=systemVersionRepository.findByType(SystemVersion.Type.ANDROID);
			if(systemVersion!=null){
				def va=Double.parseDouble(systemVersion.getVersion());
				if(va>v)
					return systemVersion;
			}
		}else if(type.equals("1")){
			systemVersion=systemVersionRepository.findByType(SystemVersion.Type.IOS);
			if(systemVersion!=null){
				def va=Double.parseDouble(systemVersion.getVersion());
				if(va>v)
					return systemVersion;
			}
		}
		return null;
	}
}
