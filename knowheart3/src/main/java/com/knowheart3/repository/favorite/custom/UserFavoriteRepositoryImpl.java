package com.knowheart3.repository.favorite.custom;

import com.qubaopen.survey.entity.user.User;
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
    public List<UserFavorite> findByTypeAndUser(String type, User user, Pageable pageable) {

        StringBuffer sql = new StringBuffer();
        sql.append("select * from user_favorite uf ");
        sql.append("left join daily_discovery dd on uf.self_id = dd.self_id ");
        sql.append("where uf.user_id = :user ");
        if ("0".equals(type)) {
            sql.append("and uf.topic_id is null ");
        } else if ("1".equals(type)) {
            sql.append("and uf.self_id is null ");
        }
        sql.append("order by uf.created_date desc ");

        Query query = entityManager.createNativeQuery(sql.toString(), UserFavorite.class);
        query.setParameter("user" ,user.getId()).
                setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }
}
