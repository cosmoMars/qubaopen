package com.qubaopen.survey.repository.doctor.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.Pageable;

import com.qubaopen.survey.entity.doctor.DoctorInfo;

public class DoctorInfoRepositoryImpl implements DoctorInfoRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<DoctorInfo> findByFilter(Map<String, Object> filters) {

		Pageable pageable = (Pageable) filters.get("pageable");

		Query query = null;
		StringBuilder hql = new StringBuilder();

		hql.append("from DoctorInfo di ");

		if (filters.get("genreId") != null) {
			hql.append("join fetch di.genres g ");
		}
		if (filters.get("targetId") != null) {
			hql.append("join fetch di.targetUsers tu ");
		}

		if (filters != null) {
			if (filters.size() > 1) {
				hql.append("where ");
			}
			List<String> where = new ArrayList<String>();

			if (filters.get("genreId") != null) {
				where.add("g.id = :genreId");
			}
			if (filters.get("targetId") != null) {
				where.add("tu.id = :targetId");
			}
			if (filters.get("areaCode") != null) {
				where.add("di.areaCode.code = :areaCode");
			}
			if (filters.get("faceToFace") != null) {
				where.add("di.faceToFace = :faceToFace");
			}
			if (filters.get("video") != null) {
				where.add("di.video = :video");
			}
			if (filters.get("ids") != null) {
				where.add("di.id not in (:ids)");
			}
			StringBuilder whereFilters = new StringBuilder();
			for (int i = 0; i < where.size(); i++) {
				whereFilters.append(where.get(i));
				if (i < where.size() - 1) {
					whereFilters.append(" and ");
				}
			}
			
//			String whereFilters = String.join(" and ", where);
			
			hql.append(whereFilters.toString());
			System.out.println(hql.toString());
		}

		query = entityManager.createQuery(hql.toString());
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
				.setMaxResults(pageable.getPageSize());

		if (filters.get("genreId") != null)
			query.setParameter("genreId", filters.get("genreId"));
		if (filters.get("targetId") != null)
			query.setParameter("targetId", filters.get("targetId"));
		if (filters.get("areaCode") != null)
			query.setParameter("areaCode", filters.get("areaCode"));
		if (filters.get("faceToFace") != null)
			query.setParameter("faceToFace", filters.get("faceToFace"));
		if (filters.get("video") != null)
			query.setParameter("video", filters.get("video"));
		if (filters.get("ids") != null)
			query.setParameter("ids", filters.get("ids"));

		@SuppressWarnings("unchecked")
		List<DoctorInfo> doctorInfos = query.getResultList();
		
		return doctorInfos;
	}

}
