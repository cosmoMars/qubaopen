package com.qubaopen.survey.repository.user.custom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.qubaopen.survey.entity.user.UserMood;

public class UserMoodRepositoryImpl implements UserMoodRepositoryCustom {
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public UserMood getLastUserMood(long userId) {
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<UserMood> query = builder.createQuery(UserMood.class);
		
		Root<UserMood> root = query.from(UserMood.class);
		
		query.where(
			builder.equal(root.get("user").get("id"), userId)
		).orderBy(builder.desc(root.get("createdDate")));
		
		return entityManager.createQuery(query)
				.setMaxResults(1)
				.getSingleResult();
	}

}
