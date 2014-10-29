package com.qubaopen.backend.repository.user.custom;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.qubaopen.backend.vo.UserReportVo;

public class UserRepositoryImpl implements UserReportRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<UserReportVo> calUserReports() {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("select a.rq as dates,b.cs as register,a.cs as user ");
		sql.append("from (SELECT substring(created_date,1,10) as rq,count(*) as cs FROM user_basic " );
		sql.append("where activated = 1 and created_date is not null group by substring(created_date,1,10)) as a ");
		sql.append("left join (SELECT substring(created_date,1,10) as rq,count(*) as cs FROM user_basic ");
		sql.append("where created_date is not null group by substring(created_date,1,10)) as b on a.rq = b.rq");
		
		Query query = entityManager.createNativeQuery(sql.toString());
		
		@SuppressWarnings("unchecked")
		List<Object[]> list = query.getResultList();
		List<UserReportVo> userReports = new ArrayList<UserReportVo>();
		for (Object[] objects : list) {
			UserReportVo ur = new UserReportVo();
			
			ur.setDate((String)objects[0]);
			ur.setRegisterCount(Integer.parseInt(objects[1].toString()));
			ur.setUserCount(Integer.parseInt(objects[2].toString()));
			userReports.add(ur);
		}
		
		return userReports;
	}
}
