package com.knowheart3.repository.exercise;

import com.knowheart3.repository.exercise.custom.UserExerciseInfoLogRepositoryCustom;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.topic.UserExerciseInfoLog;

public interface UserExerciseInfoLogRepository extends MyRepository<UserExerciseInfoLog, Long>, UserExerciseInfoLogRepositoryCustom {

}
