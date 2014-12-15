package com.qubaopen.survey.controller.mindmap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.mindmap.MapRecord;
import com.qubaopen.survey.repository.mindmap.MapRecordRepository;

@RestController
@RequestMapping("mapRecords")
public class MapRecordController extends AbstractBaseController<MapRecord, Long> {

	@Autowired
	private MapRecordRepository mapRecordRepository;

	@Override
	protected MyRepository<MapRecord, Long> getRepository() {
		return mapRecordRepository;
	}

}
