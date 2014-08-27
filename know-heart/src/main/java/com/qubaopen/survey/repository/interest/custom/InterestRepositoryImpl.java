package com.qubaopen.survey.repository.interest.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.Pageable;

import com.qubaopen.survey.entity.interest.Interest;
import com.qubaopen.survey.entity.user.User;

public class InterestRepositoryImpl implements InterestRepositoryCustom {
	
	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> findByFilters(Map<String, Object> filters) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		Pageable pageable = (Pageable) filters.get("pageable");
		Query query = null;
		
		System.out.println(filters.get("sortTypeId") );
		
		if (filters.get("interestTypeId") == null && filters.get("sortTypeId") == null) {
			StringBuilder hql = new StringBuilder();
			hql.append("from Interest i where i not in (select iuq.interest from InterestUserQuestionnaire iuq where iuq.user = :user) ");
			if (filters.get("ids") != null)
				hql.append("and i.id not in (:ids) ");
			hql.append("order by i.recommendedValue desc");
			
			query = entityManager.createQuery(hql.toString());
			query.setParameter("user", filters.get("user"))
				.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
				.setMaxResults(pageable.getPageSize());
			if (filters.get("ids") != null) 
				query.setParameter("ids", filters.get("ids"));
		}
		
		if (filters.get("interestTypeId") != null && filters.get("sortTypeId") == null) {
			StringBuilder hql = new StringBuilder();
			hql.append("from Interest i where i not in (select iuq.interest from InterestUserQuestionnaire iuq where iuq.user = :user) ");
			hql.append("and i.interestType.id = :type ");
			if (filters.get("ids") != null)
				hql.append("and i.id not in (:ids) ");
			hql.append("order by i.recommendedValue desc");
			
			query = entityManager.createQuery(hql.toString());
			query.setParameter("user", filters.get("user"))
				.setParameter("type", filters.get("interestTypeId"))
				.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
				.setMaxResults(pageable.getPageSize());
			if (filters.get("ids") != null) 
				query.setParameter("ids", filters.get("ids"));
		}
		
		if (filters.get("interestTypeId") == null && filters.get("sortTypeId") != null && 2l == (long)filters.get("sortTypeId")) {
			StringBuilder hql = new StringBuilder();
			hql.append("from Interest i where i not in (select iuq.interest from InterestUserQuestionnaire iuq where iuq.user = :user) ");
			if (filters.get("ids") != null)
				hql.append("and i.id not in (:ids) ");
			hql.append("order by i.createdDate desc ");
			
			query = entityManager.createQuery(hql.toString())
				.setParameter("user", filters.get("user"))
				.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
				.setMaxResults(pageable.getPageSize());
			if (filters.get("ids") != null) 
				query.setParameter("ids", filters.get("ids"));
		}
		if (filters.get("interestTypeId") == null && filters.get("sortTypeId") != null && 1l == (long)filters.get("sortTypeId")) {
			StringBuilder sql = new StringBuilder();
			sql.append("select * from interest i left join (select iuq.interest_id interestId,count(iuq.interest_id) countNum ");
			sql.append("from interest_user_questionnaire iuq ");
			sql.append("group by iuq.interest_id) a on i.id = a.interestId ");
			sql.append("where i.id not in (select iu.interest_id from interest_user_questionnaire iu  where iu.user_id = :user) ");
			if (filters.get("ids") != null)
				sql.append("and i.id not in (:ids) ");
			sql.append("order by a.countNum desc,i.recommended_value desc");
			query = entityManager.createNativeQuery(sql.toString(), Interest.class)
				.setParameter("user", ((User)filters.get("user")).getId())
				.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
				.setMaxResults(pageable.getPageSize());
			if (filters.get("ids") != null) 
				query.setParameter("ids", filters.get("ids"));
			
		}
		
		if (filters.get("interestTypeId") != null && filters.get("sortTypeId") != null && 2l == (long)filters.get("sortTypeId")) {
			StringBuilder hql = new StringBuilder();
			hql.append("from Interest i where i not in (select iuq.interest from InterestUserQuestionnaire iuq where iuq.user = :user) ");
			hql.append("and i.interestType.id = :type ");
			if (filters.get("ids") != null)
				hql.append("and i.id not in (:ids) ");
			hql.append("order by i.recommendedValue desc,i.createdDate desc");
			
			query = entityManager.createQuery(hql.toString())
				.setParameter("user", filters.get("user"))
				.setParameter("type", filters.get("interestTypeId"))
				.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
				.setMaxResults(pageable.getPageSize());
			if (filters.get("ids") != null) 
				query.setParameter("ids", filters.get("ids"));
		}
		
		if (filters.get("interestTypeId") != null && filters.get("sortTypeId") != null && 1l == (long)filters.get("sortTypeId")) {
			StringBuilder sql = new StringBuilder();
			sql.append("select * from interest i left join (select iuq.interest_id interestId,count(iuq.interest_id) countNum ");
			sql.append("from interest_user_questionnaire iuq ");
			sql.append("group by iuq.interest_id) a on i.id = a.interestId ");
			sql.append("where i.id not in (select iu.interest_id from interest_user_questionnaire iu  where iu.user_id = :userId)  and i.interest_type_id = :type ");
			if (filters.get("ids") != null)
				sql.append("and i.id not in (:ids) ");
			sql.append("order by a.countNum desc,i.recommended_value desc");
			query = entityManager.createNativeQuery(sql.toString(), Interest.class)
				.setParameter("userId", ((User)filters.get("user")).getId())
				.setParameter("type", filters.get("interestTypeId"))
				.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
				.setMaxResults(pageable.getPageSize());
			if (filters.get("ids") != null) 
				query.setParameter("ids", filters.get("ids"));
		}
		List<Interest> interests = new ArrayList<>();
		try {
			interests = query.getResultList();
		} catch (Exception e) {
		}
		
		result.put("interests", interests);
		return result;
	}

}
