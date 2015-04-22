package com.knowheart3.controller.system

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import com.knowheart3.repository.system.SystemVersionRepository
import com.knowheart3.service.SystemVersionService
import com.qubaopen.core.controller.AbstractBaseController
import com.qubaopen.core.repository.MyRepository
import com.qubaopen.survey.entity.system.SystemVersion

@RestController
@RequestMapping("systemVersions")
public class SystemVersionController extends AbstractBaseController<SystemVersion, Long> {

	@Autowired
	private SystemVersionRepository systemVersionRepository;
	
	@Autowired
	SystemVersionService systemVersionService; 

	@Override
	protected MyRepository<SystemVersion, Long> getRepository() {
		return systemVersionRepository;
	}

	@RequestMapping(value = 'getUpdateInfo', method = RequestMethod.GET)
	getUpdateInfo(@RequestParam(required = false) String type,
				  @RequestParam(required = false) String version,
				  @RequestParam(required = false) Integer resource) {

		if (type == null || type.equals("")) {
			return '{"success": "0", "message": "类型为空"}'
		}
		if (version == null || version.equals("")) {
			return '{"success": "0", "message": "版本号为空"}'
		}


		if (resource == null) {
			resource = 0
		}
		def systemVersion = systemVersionService.getUrl(type, version, resource);
		if (systemVersion) {
			return '{"success": "1", "path": "' + systemVersion.downloadUrl + '","message":"' + systemVersion.detail + '"}'
		} else {
			return '{"success": "0","message":"已是最新版本"}'
		}
	}

}
