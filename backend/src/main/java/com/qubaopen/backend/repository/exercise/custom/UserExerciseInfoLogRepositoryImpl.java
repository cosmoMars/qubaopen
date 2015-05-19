package com.qubaopen.backend.repository.exercise.custom;

import com.qubaopen.survey.entity.topic.UserExerciseInfoLog;
import com.qubaopen.survey.entity.user.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * Created by mars on 15/4/9.
 */
public class UserExerciseInfoLogRepositoryImpl implements UserExerciseInfoLogRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserExerciseInfoLog findLastLogByUserAndTime(User user, Date date) {

        StringBuffer sql = new StringBuffer();
        sql.append("select * from user_exercise_info_log u ");
        sql.append("where date_format(u.created_date, '%Y-%m-%d') >= date_format(:date, '%Y-%m-%d') ");
        sql.append("and user_id = :user order by u.created_date asc");

        Query query = entityManager.createNativeQuery(sql.toString(), UserExerciseInfoLog.class)
                .setParameter("date", date)
                .setParameter("user", user.getId())
                .setMaxResults(1);

        List resultList = query.getResultList();
        if (resultList.size() > 0) {
            return (UserExerciseInfoLog) resultList.get(0);
        }

        return null;

    }
}
