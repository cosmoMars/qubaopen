package com.qubaopen.backend.repository.topic.custom;

import com.qubaopen.backend.vo.FavoriteVo;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserFavoriteRepositoryImpl implements UserFavoriteRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<FavoriteVo> findTopicVos(Pageable pageable) {

		StringBuilder sql = new StringBuilder();
		sql.append("select t.id topicId ,t.name, t.content,t.created_date topicCreateDate, dd.time, t.pic_url picUrl ");
		sql.append("from topic t ");
		sql.append("left join daily_discovery dd on t.id = dd.topic_id ");
		sql.append("order by dd.time asc, t.created_date desc ");

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
			vo.setPicUrl(objects[5] != null ? objects[5].toString() : null);
			favoriteVos.add(vo);
		}

		return favoriteVos;
	}

	@Override
	public List<FavoriteVo> findSelfVos(Pageable pageable) {
		
		StringBuilder sql = new StringBuilder();
	
		sql.append("select s.id, s.title, s.remark, s.created_date selfCreateDate, dd.time from self s ");
		sql.append("left join daily_discovery dd on s.id = dd.self_id ");
		sql.append("order by dd.time asc, s.created_date desc ");
		
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
