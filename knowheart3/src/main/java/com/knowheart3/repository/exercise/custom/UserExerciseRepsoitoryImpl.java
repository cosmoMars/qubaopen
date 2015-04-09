package com.knowheart3.repository.exercise.custom;

import com.qubaopen.survey.entity.topic.UserExercise;
import com.qubaopen.survey.entity.user.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Created by mars on 15/4/9.
 */
public class UserExerciseRepsoitoryImpl implements UserExerciseRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public UserExercise findCompleteExerciseByUser(User user) {

        StringBuffer sql = new StringBuffer();
        sql.append("select ue from UserExercise ue where ue.user = :user and ue.complete = true order by time desc ");

        Query query = entityManager.createQuery(sql.toString(), UserExercise.class)
                .setMaxResults(1);

        return (UserExercise) query.getSingleResult();
    }
}
