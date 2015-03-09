package com.knowheart3.repository.self.custom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.qubaopen.survey.entity.self.SelfGroup;

public class SelfGroupRepositoryImpl implements SelfGroupRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;


	@Override
	public SelfGroup findSpecialSelfGroup() {
		 CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		 CriteriaQuery<SelfGroup> query = builder.createQuery(SelfGroup.class);
		 
		 Root<SelfGroup> root = query.from(SelfGroup.class);
		 
		 query.where(builder.equal(root.get("status"), true)).orderBy(builder.desc((root.get("recommendedValue"))));
		 
		 return entityManager.createQuery(query)
				 .setMaxResults(1)
				 .getSingleResult();
	}
}
