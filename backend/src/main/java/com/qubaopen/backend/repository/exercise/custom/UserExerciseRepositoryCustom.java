package com.qubaopen.backend.repository.exercise.custom;

import com.qubaopen.survey.entity.topic.UserExercise;
import com.qubaopen.survey.entity.user.User;

/**
 * Created by mars on 15/4/9.
 */
public interface UserExerciseRepositoryCustom {

    UserExercise findCompleteExerciseByUser(User user);

}
