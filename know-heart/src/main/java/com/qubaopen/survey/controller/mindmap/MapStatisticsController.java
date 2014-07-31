package com.qubaopen.survey.controller.mindmap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.mindmap.MapStatistics;
import com.qubaopen.survey.repository.mindmap.MapStatisticsRepository;

@RestController
@RequestMapping("MapStatistics")
public class MapStatisticsController extends AbstractBaseController<MapStatistics, Long> {

	@Autowired
	private MapStatisticsRepository mapStatisticsRepository;

	@Override
	protected MyRepository<MapStatistics, Long> getRepository() {
		return mapStatisticsRepository;
	}

}
