package com.qubaopen.survey.repository.self.custom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class SelfRepositoryImpl implements SelfRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

}
