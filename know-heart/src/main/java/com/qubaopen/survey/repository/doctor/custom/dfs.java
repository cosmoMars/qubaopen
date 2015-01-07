package com.qubaopen.survey.repository.doctor.custom;

import java.util.ArrayList;
import java.util.List;

public class dfs {
	public static void main(String[] args) {
		List<String> where = new ArrayList<String>();
		StringBuilder hql = new StringBuilder();
		hql.append("from DoctorInfo di ");
		hql.append("join fetch di.genres g ");
		hql.append("join fetch di.targetUsers tu ");
		hql.append("where ");
			where.add("g.id = :genre");
			where.add("tu.id = :target");
			where.add("di.areaCode.id = :areaId");
			where.add("di.faceToFace = :faceToFace");
			where.add("di.video = :video");
		String whereFilters = String.join(" and ", where);
		hql.append(whereFilters);
		System.out.println(hql.toString());
	}
}
