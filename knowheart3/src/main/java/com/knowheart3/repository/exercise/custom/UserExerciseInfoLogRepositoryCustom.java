package com.knowheart3.repository.exercise.custom;

import com.qubaopen.survey.entity.topic.UserExerciseInfoLog;
import com.qubaopen.survey.entity.user.User;

import java.util.Date;

/**
 * Created by mars on 15/4/9.
 */
public interface UserExerciseInfoLogRepositoryCustom {

    UserExerciseInfoLog findLastLogByUserAndTime(User user, Date date);

}
