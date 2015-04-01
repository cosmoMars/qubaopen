package com.qubaopen.backend.repository.topic;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.topic.Exercise;
import com.qubaopen.survey.entity.topic.ExerciseInfo;

public interface ExerciseInfoRepository extends MyRepository<ExerciseInfo, Long> {
	@Query("from ExerciseInfo e where e.exercise=:exercise order by e.createdDate desc,e.id desc")
	List<ExerciseInfo> findExerciseInfo(Pageable pageable,@Param("exercise") Exercise exercise);
}
