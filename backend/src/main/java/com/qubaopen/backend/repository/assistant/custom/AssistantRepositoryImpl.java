package com.qubaopen.backend.repository.assistant.custom;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by mars on 15/3/25.
 */
public class AssistantRepositoryImpl implements AssistantRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public long findSpaceAssistant() {

        StringBuffer sql = new StringBuffer();

        sql.append("select a.id from assistant a ");
        sql.append("left join (select di.assistant_id id, count(di.assistant_id) count " +
                "from doctor_info di group by di.assistant_id) dia ");
        sql.append(" on a.id = dia.id order by count ");

        List<Long> ids = entityManager.createNativeQuery(sql.toString())
                .setMaxResults(1)
                .getResultList();

        if (ids.size() > 0) {
            return ids.get(0);
        }
        return 0;
    }
}
