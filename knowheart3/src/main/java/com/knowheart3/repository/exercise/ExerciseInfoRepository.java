package com.knowheart3.repository.exercise;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.topic.Exercise;
import com.qubaopen.survey.entity.topic.ExerciseInfo;

public interface ExerciseInfoRepository extends MyRepository<ExerciseInfo, Long> {

	@Query("from ExerciseInfo ei where ei.exercise.id = :exerciseId order by abs(ei.number) asc")
	List<ExerciseInfo> findAllByExerciseIdOrderByNumber(@Param("exerciseId") Long exerciseId);
	
	int countByExercise(Exercise exercise);
}
