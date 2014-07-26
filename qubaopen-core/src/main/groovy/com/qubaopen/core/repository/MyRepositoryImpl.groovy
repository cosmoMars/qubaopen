package com.qubaopen.core.repository

import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders

import javax.persistence.EntityManager
import javax.persistence.TypedQuery
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate

import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.query.QueryUtils
import org.springframework.data.jpa.repository.support.SimpleJpaRepository


final class MyRepositoryImpl<T, ID extends Serializable> extends
		SimpleJpaRepository<T, ID> implements MyRepository<T, ID> {

	private final EntityManager entityManager

	MyRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
		super(domainClass, entityManager)

		this.entityManager = entityManager
	}

	@Override
	public T findOneByFilters(Map filters) {
		getQuery(filters, null).singleResult
	}

	@Override
	public List<T> findAll(Map filters) {
		getQuery(filters, null).resultList
	}

	@Override
	Page<T> findAll(Map filters, Pageable pageable) {
		def query = getQuery(filters, pageable?.sort)
		!pageable ? new PageImpl(query.resultList) : readPage(query, pageable, filters)
	}

	protected Page<T> readPage(TypedQuery<T> query, Pageable pageable, Map filters) {

		query.firstResult = pageable.offset
		query.maxResults = pageable.pageSize

		Long total = QueryUtils.executeCountQuery(getCountQuery(filters))
		def content = total > pageable.offset ? query.resultList : []

		new PageImpl(content, pageable, total)
	}

	protected TypedQuery<T> getQuery(Map filters, Sort sort) {

		def builder = entityManager.criteriaBuilder
		def query = builder.createQuery getDomainClass()

		def root =  query.from getDomainClass()
		query.select(root)

		applyFiltersToCriteria(filters, query)

		if (sort) {
			query.orderBy(toOrders(sort, root, builder))
		}

		entityManager.createQuery(query)
	}

	protected TypedQuery<Long> getCountQuery(Map filters) {

		def builder = entityManager.criteriaBuilder
		def query = builder.createQuery Long.class

		def root =  query.from getDomainClass()

		applyFiltersToCriteria(filters, query)

		if (query.distinct) {
			query.select(builder.countDistinct(root))
		} else {
			query.select(builder.count(root))
		}

		entityManager.createQuery(query)
	}

	private void applyFiltersToCriteria(Map filters, CriteriaQuery query) {


		def builder = entityManager.criteriaBuilder
		def root = query.roots[0]

		def predicates = []
		filters.each { k, v ->

			def prop  = StringUtils.substringBefore(k, '_'),
				oper  = StringUtils.substringAfter(k, '_'),
				props = StringUtils.split(prop, '.')

			if (oper == 'fetch') {
				if (query.selection) {
					def fetch = root.fetch(props[0])
					for (def i = 1;  i < props.length; i++) {
						fetch = fetch.fetch(props[i])
					}
				}
			} else {
				def path = root.get(props[0])
				for (def i = 1;  i < props.length; i++) {
					path = path.get(props[i])
				}

				if (v == 0 || v) {
					if (oper == 'like') {
						v = '%' + v + '%'
					} else if (path.javaType == Date) {
						v = DateUtils.parseDate(v, 'yyyy-MM-dd')
					}
					predicates << builder."$oper"(path, v)
				} else {
					predicates << builder."$oper"(path)
				}
			}

		}

		if (predicates) {
			query.where(predicates as Predicate[])
		}

		root
	}

}
