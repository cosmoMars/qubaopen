package com.qubaopen.backend.repository.exercise;

import java.util.List;

import com.qubaopen.backend.repository.exercise.custom.ExerciseRepositoryCustom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.cash.DoctorTakeCash;
import com.qubaopen.survey.entity.topic.Exercise;

public interface ExerciseRepository extends MyRepository<Exercise, Long>, ExerciseRepositoryCustom {
	@Query("from Exercise e order by e.createdDate desc,e.id desc")
	List<Exercise> findExercise(Pageable pageable);
}
