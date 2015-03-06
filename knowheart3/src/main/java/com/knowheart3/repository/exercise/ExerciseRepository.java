package com.knowheart3.repository.exercise;

import com.knowheart3.repository.exercise.custom.ExerciseRepositoryCustom;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.topic.Exercise;

public interface ExerciseRepository extends MyRepository<Exercise, Long>, ExerciseRepositoryCustom {

}
