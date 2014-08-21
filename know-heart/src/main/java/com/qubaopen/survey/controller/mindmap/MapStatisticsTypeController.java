package com.qubaopen.survey.controller.mindmap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.mindmap.MapStatisticsType;
import com.qubaopen.survey.repository.mindmap.MapStatisticsTypeRepository;

@RestController
@RequestMapping("mapStatisticsTypes")
public class MapStatisticsTypeController extends AbstractBaseController<MapStatisticsType, Long> {

	@Autowired
	private MapStatisticsTypeRepository mapStatisticsTypeRepository;

	@Override
	protected MyRepository<MapStatisticsType, Long> getRepository() {
		return mapStatisticsTypeRepository;
	}

}
