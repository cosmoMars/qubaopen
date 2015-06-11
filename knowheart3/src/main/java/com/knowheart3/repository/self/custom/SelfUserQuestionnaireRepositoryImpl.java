package com.knowheart3.repository.self.custom;

import com.knowheart3.vo.SelfResultVo;
import com.qubaopen.survey.entity.self.Self;
import com.qubaopen.survey.entity.self.SelfUserQuestionnaire;
import com.qubaopen.survey.entity.user.User;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mars on 15/3/30.
 */
public class SelfUserQuestionnaireRepositoryImpl implements SelfUserQuestionnairerRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Long> findGroupId(User user, Pageable pageable) {

        StringBuffer sql = new StringBuffer();
        sql.append("select distinct s.self_group_id groupId ");
        sql.append("from self_user_questionnaire suq ");
        sql.append("left join self s on suq.self_id = s.id ");
        sql.append("where suq.used = true and suq.user_id = :userId ");
        sql.append("order by suq.time desc ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("userId", user.getId());
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize());

        List<Object> list = query.getResultList();
        List<Long> ids = new ArrayList<>();
        for (Object o : list) {
            ids.add(Long.parseLong(o.toString()));
        }
        return ids;
    }

    @Override
    public List<SelfResultVo> findSelfResultVo(User user, List<Long> groupIds) {



//        select distinct suq.id suqid ,s.id selfid,s.title, sg.id groupid , sg.content sgContent , dd.time ddTime
//        from self_user_questionnaire suq
//        left join self s on suq.self_id = s.id
//        left join self_group sg on sg.id = s.self_group_id
//        inner join daily_discovery dd on suq.self_id = dd.self_id
//
//        where suq.used = true and suq.user_id = 1
//        and s.self_group_id in (
//                select  a.id from (
//                select distinct s.self_group_id id
//        from self_user_questionnaire suq
//        left join self s on suq.self_id = s.id
//
//        where suq.used = true and suq.user_id = 1
//        limit 20) a
//        )
//        order by dd.time desc

        StringBuffer sql = new StringBuffer();
        sql.append("select distinct suq.id suqid, suq.time suqTime, s.id selfid, s.title, sg.id groupid, sg.content sgContent, dd.time ddTime ");
        sql.append("from self_user_questionnaire suq ");
        sql.append("left join self s on suq.self_id = s.id ");
        sql.append("left join self_group sg on sg.id = s.self_group_id ");
        sql.append("inner join daily_discovery dd on suq.self_id = dd.self_id ");
        sql.append("where suq.used = true and suq.user_id = :userId ");
        if (groupIds.size() > 0) {
            sql.append("and s.self_group_id in (:groupIds) ");
        }
        sql.append("order by suq.time desc ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("userId", user.getId());
        if (groupIds.size() > 0) {
            query.setParameter("groupIds", groupIds);
        }

        List<Object[]> list = query.getResultList();
        List<SelfResultVo> result = new ArrayList<>();
        for (Object[] objects : list) {
            SelfResultVo vo = new SelfResultVo();
            vo.setSuqId(objects[0].toString());
            vo.setSuqTime((Date) objects[1]);
            vo.setSelfId(objects[2].toString());
            vo.setSelfTitle((String) objects[3]);
            vo.setGroupId(objects[4].toString());
            vo.setGroupName((String) objects[5]);
            vo.setDdTime((Date) objects[6]);
            vo.setDone(true);

            result.add(vo);
        }
        return result;
    }

    @Override
    public SelfUserQuestionnaire findByUserAndSelfAndUsed(User user, Self self, boolean used) {

        StringBuffer sql = new StringBuffer();
        sql.append("from SelfUserQuestionnaire suq ");
        sql.append("where suq.user.id = :userId ");
        sql.append("and suq.self.id = :selfId ");
        sql.append("and suq.used = :used ");
        sql.append("order by suq.time desc");

        System.out.println(sql.toString());
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("userId", user.getId())
                .setParameter("selfId", self.getId())
                .setParameter("used" , used)
                .setMaxResults(1);

        List<SelfUserQuestionnaire> results = query.getResultList();
        if (results.size() > 0) {
            return results.get(0);
        }
        return null;
    }
}
