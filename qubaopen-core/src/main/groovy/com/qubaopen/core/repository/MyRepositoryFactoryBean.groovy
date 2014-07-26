package com.qubaopen.core.repository

import javax.persistence.EntityManager

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean
import org.springframework.data.repository.Repository
import org.springframework.data.repository.core.RepositoryMetadata
import org.springframework.data.repository.core.support.RepositoryFactorySupport

class MyRepositoryFactoryBean extends JpaRepositoryFactoryBean {

	@Override
	protected RepositoryFactorySupport createRepositoryFactory(
			EntityManager entityManager) {

		new MyRepositoryFactory(entityManager)
	}

	private static class MyRepositoryFactory extends JpaRepositoryFactory {

		private final EntityManager entityManager

		public MyRepositoryFactory(EntityManager entityManager) {
			super(entityManager)

			this.entityManager = entityManager
		}

		@Override
		protected Object getTargetRepository(RepositoryMetadata metadata) {
			new MyRepositoryImpl<>(metadata.getDomainType(), entityManager)
		}

		@Override
		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
			MyRepository.class
		}

	}

}
