package com.qubaopen.backend.repository.exercise;

import com.qubaopen.backend.repository.exercise.custom.UserExerciseInfoLogRepositoryCustom;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.topic.UserExerciseInfoLog;

public interface UserExerciseInfoLogRepository extends MyRepository<UserExerciseInfoLog, Long>, UserExerciseInfoLogRepositoryCustom {

}
