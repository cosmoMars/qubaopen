package com.qubaopen.backend.repository.doctor.custom;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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

        Query query = entityManager.createNativeQuery(sql.toString())
                .setMaxResults(1);

        return (long) query.getSingleResult();
    }
}
