package com.qubaopen.survey.repository.self.custom;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.qubaopen.survey.entity.self.Self;

public class SelfRepositoryCustomImpl implements SelfRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

//	@Override
//	public List<Self> getRandomSelfs(List<Long> ids, int limit) {
//		StringBuilder sql = new StringBuilder();
//		
////		sql.append("select ")
//		
//		return null;
//	}


}
