package com.qubaopen.survey.repository.mindmap.custom;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.qubaopen.survey.entity.mindmap.MapRecord;

public class MapRecordRepositoryImpl implements MapRecordRepositoryCustom{
	

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<MapRecord> findEveryDayMapRecord() {
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MapRecord> query = builder.createQuery(MapRecord.class);
		
		Root<MapRecord> root = query.from(MapRecord.class);
		
		query.where().groupBy(root.get("DATE_FORMAT(createdDate,'%Y-%m-%d')")).orderBy(builder.desc(root.get("createdDate")));
		

		return null;
	}

//	@Override
//	public Self findSpecialSelf() {
//		 CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//		 CriteriaQuery<Self> query = builder.createQuery(Self.class);
//		 
//		 Root<Self> root = query.from(Self.class);
//		 
//		 query.where().orderBy(builder.desc((root.get("recommendedValue"))));
//		 
//		 return entityManager.createQuery(query)
//				 .setMaxResults(1)
//				 .getSingleResult();
//	}
}
