package com.qubaopen.survey.repository.self.custom;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.qubaopen.survey.entity.self.Self;

public class SelfRepositoryImpl implements SelfRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<Self> findRandomSelfs(List<Self> exists, int limit) {

		List<Long> ids = new ArrayList<>();
		for (Self s : exists) {
			ids.add(s.getId());
		}

		StringBuilder sql = new StringBuilder();

//		sql.append("select * from self s where s.id not in (:ids) order by rand() limit :limit");
//
//		Query query = entityManager.createNativeQuery(sql.toString(), Self.class);
//		query.setParameter("ids", ids)
//			.setParameter("limit", limit);
		sql.append("from Self s where s not in (:exists) order by rand()");
		Query query = entityManager.createQuery(sql.toString())
				.setParameter("exists", exists)
				.setMaxResults(limit);

		return query.getResultList();
	}

	@Override
	public Self findSpecialSelf() {
		 CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		 CriteriaQuery<Self> query = builder.createQuery(Self.class);
		 
		 Root<Self> root = query.from(Self.class);
		 
		 query.where().orderBy(builder.desc((root.get("recommendedValue"))));
		 
		 return entityManager.createQuery(query)
				 .setMaxResults(1)
				 .getSingleResult();
	}

}
