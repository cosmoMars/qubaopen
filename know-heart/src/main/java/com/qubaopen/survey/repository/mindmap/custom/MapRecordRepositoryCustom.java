package com.qubaopen.survey.repository.mindmap.custom;

import java.util.List;

import com.qubaopen.survey.entity.mindmap.MapRecord;


public interface MapRecordRepositoryCustom {

	List<MapRecord> findEveryDayMapRecord();
}
