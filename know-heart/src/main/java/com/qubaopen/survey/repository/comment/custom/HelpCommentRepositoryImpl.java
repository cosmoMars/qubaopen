package com.qubaopen.survey.repository.comment.custom;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;

import com.qubaopen.survey.entity.comment.Help;
import com.qubaopen.survey.entity.comment.HelpComment;

public class HelpCommentRepositoryImpl implements HelpCommentRepositoryCustom{

	@Autowired
	private EntityManager entityManager;
	
	@Override
	public List<HelpComment> findLimitComment(Help help) {
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<HelpComment> query = builder.createQuery(HelpComment.class);
		
		Root<HelpComment> root = query.from(HelpComment.class);
		query.where(builder.equal(root.get("help"), help)).orderBy(builder.asc((root.get("createdDate"))));
		
		return entityManager.createQuery(query)
				.setMaxResults(5).getResultList();
	}

	
}
