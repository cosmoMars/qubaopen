package com.qubaopen.survey.repository.self.custom;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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

		sql.append("select * from self s where s.id not in (:ids) order by rand() limit :limit");

		Query query = entityManager.createNativeQuery(sql.toString(), Self.class);
		query.setParameter("ids", ids)
			.setParameter("limit", limit);

		return query.getResultList();
	}

}
