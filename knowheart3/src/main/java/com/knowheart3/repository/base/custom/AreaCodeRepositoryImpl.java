package com.knowheart3.repository.base.custom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by mars on 15/4/8.
 */
public class AreaCodeRepositoryImpl implements AreaCodeRepostioryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<String> findExistDoctorAreaCode() {

        StringBuffer sql = new StringBuffer();

        sql.append("select ac.code, ac.id from area_code ac ");
        sql.append("left join doctor_info di on ac.id = di.area_code_id ");
        sql.append("where di.login_status = 3 ");
        sql.append("union ");
        sql.append("select ac.code, ac.id from area_code ac ");
        sql.append("left join hospital_info hi on ac.id = hi.area_code_id ");
        sql.append("where hi.login_status = 3 ");


        Query query =  entityManager.createNativeQuery(sql.toString());

        List<Object[]> list = query.getResultList();

//        List<String> codes = new ArrayList<>();
        HashSet<String> codes = new HashSet<>();
        for (Object[] objects : list) {
            codes.add(objects[0].toString());
        }

        return new ArrayList<>(codes);
    }
}
