package com.qubaopen.backend.repository.topic;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.topic.Topic;

public interface TopicRepository extends MyRepository<Topic, Long> {

	@Query("from Topic t where t.id not in (select dd.topic.id from DailyDiscovery dd) order by t.createdDate desc")
	List<Topic>	findTopicOrderBycreatedDateDesc();
}
