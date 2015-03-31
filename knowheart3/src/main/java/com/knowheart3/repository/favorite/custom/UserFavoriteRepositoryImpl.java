package com.knowheart3.repository.favorite.custom;

import com.qubaopen.survey.entity.user.UserFavorite;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by mars on 15/3/30.
 */
public class UserFavoriteRepositoryImpl implements UserFavoriteRepositoryCustom{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserFavorite> findByType(String type, Pageable pageable) {

        StringBuffer sql = new StringBuffer();
        sql.append("select * from user_favorite uf ");
        sql.append("left join daily_discovery dd on uf.self_id = dd.self_id ");
        if ("0".equals(type)) {
            sql.append("where uf.topic_id is null ");
        } else if ("1".equals(type)) {
            sql.append("where uf.self_id is null ");
        }
        sql.append("order by dd.time desc ");

        Query query = entityManager.createNativeQuery(sql.toString(), UserFavorite.class);
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }
}
