package com.knowheart3.repository.exercise;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.topic.UserExercise;
import com.qubaopen.survey.entity.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface UserExerciseRepository extends MyRepository<UserExercise, Long> {

    @Query("from UserExercise ue where ue.user = :user and date_format(ue.time, '%Y-%m-%d') = date_format(:time, '%Y-%m-%d')")
    List<UserExercise> findCompleteExerciseByUserAndTime(@Param("user") User user, @Param("time") Date time);

}
