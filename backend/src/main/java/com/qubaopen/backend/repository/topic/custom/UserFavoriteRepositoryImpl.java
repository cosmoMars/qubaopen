package com.qubaopen.backend.repository.topic.custom;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.Pageable;

import com.qubaopen.backend.vo.FavoriteVo;

public class UserFavoriteRepositoryImpl implements UserFavoriteRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<FavoriteVo> findTopicVos(Pageable pageable) {

		StringBuilder sql = new StringBuilder();
		sql.append("select t.id topicId ,t.name, t.content,t.created_date topicCreateDate, u.created_date favoriteCreateDate ");
		sql.append("from topic t ");
		sql.append("left join user_favorite u on t.id = u.topic_id ");
		sql.append("order by u.created_date asc, t.created_date desc ");

		Query query = entityManager.createNativeQuery(sql.toString());

		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()).
			setMaxResults(pageable.getPageSize());

		@SuppressWarnings("unchecked")
		List<Object[]> list = query.getResultList();
		List<FavoriteVo> favoriteVos = new ArrayList<FavoriteVo>();
		for (Object[] objects : list) {
			FavoriteVo vo = new FavoriteVo();
			vo.setId(Long.valueOf(objects[0].toString()));
			vo.setName(objects[1] != null ? objects[1].toString() : null);
			vo.setContent(objects[2] != null ? objects[2].toString() : null);
			if (objects[3] != null)
				vo.setCreateDate((Date) (objects[3]));
			if (objects[4] != null)
				vo.setFavoriteCreateDate((Date) objects[4]);
			favoriteVos.add(vo);
		}

		return favoriteVos;
	}

	@Override
	public List<FavoriteVo> findSelfVos(Pageable pageable) {
		
		StringBuilder sql = new StringBuilder();
	
		sql.append("select s.id, s.title, s.remark, s.created_date selfCreateDate, u.created_date favoriteCreateDate from self s ");
		sql.append("left join user_favorite u on s.id = u.self_id ");
		sql.append("order by u.created_date asc, s.created_date desc ");
		
		Query query = entityManager.createNativeQuery(sql.toString());
		
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
			.setMaxResults(pageable.getPageSize());
		
		@SuppressWarnings("unchecked")
		List<Object[]> list = query.getResultList();
		List<FavoriteVo> favoriteVos = new ArrayList<FavoriteVo>();
		for (Object[] objects : list) {
			FavoriteVo vo = new FavoriteVo();
			vo.setId(Long.valueOf(objects[0].toString()));
			vo.setName(objects[1].toString());
			vo.setContent(objects[2].toString());
			if (objects[3] != null)
				vo.setCreateDate((Date)objects[3]);
			if (objects[4] != null) {
				vo.setFavoriteCreateDate((Date)objects[4]);
			}
			favoriteVos.add(vo);
		}

		return favoriteVos;
	}

}
