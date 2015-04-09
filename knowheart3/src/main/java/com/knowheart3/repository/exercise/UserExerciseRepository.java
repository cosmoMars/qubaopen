package com.knowheart3.repository.exercise;

import com.knowheart3.repository.exercise.custom.UserExerciseRepositoryCustom;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.topic.UserExercise;
import com.qubaopen.survey.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface UserExerciseRepository extends MyRepository<UserExercise, Long>, UserExerciseRepositoryCustom {

    @Query("from UserExercise ue where ue.user = :user and date_format(ue.time, '%Y-%m-%d') = date_format(:time, '%Y-%m-%d')")
    List<UserExercise> findCompleteExerciseByUserAndTime(@Param("user") User user, @Param("time") Date time);

    @Query("from UserExercise ue where ue.user = :user and ue.complete = true")
    Page<UserExercise> findCompleteExerciseByUser(@Param("user") User user, Pageable pageable);

}
