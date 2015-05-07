package com.qubaopen.doctor.repository.doctor.custom;

import com.qubaopen.survey.entity.doctor.Doctor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mars on 15/5/7.
 */
public class BookingRepositoryImpl implements BookingRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Map<String, Object> countBooking(Doctor doctor) {

        StringBuffer sql = new StringBuffer();

        sql.append("select a.count1, b.count2, c.count3, d.count4, e.count5 ");
        sql.append("from (select count(*) count1 from booking b where b.doctor_id = :doctorId and b.status != 11 ) a ");
        sql.append("left join (select count(*) count2 from booking b where b.doctor_id = :doctorId and b.status in (0,6)) b on 1=1 ");
        sql.append("left join (select count(*) count3 from booking b where b.doctor_id = :doctorId and b.status = 7 and b.doctor_status is null) c on 1=1 ");
        sql.append("left join (select count(*) count4 from booking b where b.doctor_id = :doctorId and b.status in (3,5) ) d on 1=1 ");
        sql.append("left join (select count(*) count5 from booking b where b.doctor_id = :doctorId and b.status = 8) e on 1=1 ");

        Query query = entityManager.createNativeQuery(sql.toString())
                .setParameter("doctorId", doctor.getId());

        Object[] object = (Object[]) query.getSingleResult();

        Map<String, Object> result = new HashMap<>();

        result.put("all", object[0]);
        result.put("toConfirm", object[1]);
        result.put("toConsult", object[2]);
        result.put("record", object[3]);
        result.put("change", object[4]);


        return result;
    }
}
