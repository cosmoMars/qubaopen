package com.knowheart3.repository.exercise.custom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.qubaopen.survey.entity.topic.Exercise;

public class ExerciseRepositoryImpl implements ExerciseRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Exercise findRandomExercise() {
		
		StringBuilder sql = new StringBuilder();
		sql.append("from Exercise e order by rand()");
		Query query = entityManager.createQuery(sql.toString())
				.setMaxResults(1);
		return (Exercise) query.getSingleResult();
	}

}
