package com.qubaopen.backend.repository.user.custom;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;

import com.qubaopen.backend.vo.UserReportVo;

@Service
public class UserRepositoryImpl implements UserReportRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<UserReportVo> countUserReports() {

		StringBuffer sql = new StringBuffer();

		sql.append("select a.rq as dates,b.cs as register,a.cs as user ");
		sql.append("from (SELECT substring(created_date,1,10) as rq,count(*) as cs FROM user_basic ");
		sql.append("where activated = 1 and created_date is not null group by substring(created_date,1,10)) as a ");
		sql.append("left join (SELECT substring(created_date,1,10) as rq,count(*) as cs FROM user_basic ");
		sql.append("where created_date is not null group by substring(created_date,1,10)) as b on a.rq = b.rq");

		Query query = entityManager.createNativeQuery(sql.toString());

		@SuppressWarnings("unchecked")
		List<Object[]> list = query.getResultList();
		List<UserReportVo> userReports = new ArrayList<UserReportVo>();
		for (Object[] objects : list) {
			UserReportVo ur = new UserReportVo();

			ur.setDate((String) objects[0]);
			ur.setRegisterCount(Integer.parseInt(objects[1].toString()));
			ur.setUserCount(Integer.parseInt(objects[2].toString()));
			userReports.add(ur);
		}

		return userReports;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> countUserInfo() {
		StringBuilder sql = new StringBuilder();
		sql.append("select a.cs as '总用户数量',b.cs as '有头像用户',c.cs as '有昵称用户',d.cs as '有签名用户',e.cs as '有身份验证用户',f.cs as '用过每日心情用户' ");
		sql.append("from (select count(*) as cs from user_basic where activated = 1) as a ");
		sql.append("left join (select count(*) as cs from user_info where avatar_path is not null) as b on 1=1 ");
		sql.append("left join (select count(*) as cs from user_info where nick_name is not null) as c on 1=1 ");
		sql.append("left join (select count(*) as cs from user_info where signature is not null) as d on 1=1 ");
		sql.append("left join (select count(*) as cs from user_id_card_bind) as e on 1=1 ");
		sql.append("left join (select count(*) as cs from(select user_id from user_mood group by user_id) as a) as f on 1=1 ");

		Query query = entityManager.createNativeQuery(sql.toString());
		List<Object[]> list = query.getResultList();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (Object[] objects : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("totalUser", objects[0]);
			map.put("avatarUser", objects[1]);
			map.put("nickNameUser", objects[2]);
			map.put("signatureUser", objects[3]);
			map.put("bindUser", objects[4]);
			map.put("moodUser", objects[5]);
			result.add(map);
		}
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> countFinalMood() {
		StringBuilder sql = new StringBuilder();
		sql.append("select a.user_id as '用户ID',coalesce(d.nick_name,substring(c.phone,8,4)) as '昵称',a.time as '设置时间',case when b.mood_type = 1 then '郁闷' when b.mood_type = 2 then '无聊' when b.mood_type = 3 then '得瑟' ");
		sql.append("when b.mood_type = 4 then '丢人' when b.mood_type = 5 then '求安慰' when b.mood_type = 6 then '纠结' end as '心情' ");
		sql.append("from( ");
		sql.append("select user_id,max(last_time) as time from user_mood group by substring(last_time,1,10),user_id) as a ");
		sql.append("left join user_mood as b on a.user_id = b.user_id and a.time = b.last_time ");
		sql.append("left join user_basic as c on a.user_id = c.id ");
		sql.append("left join user_info as d on a.user_id = d.id ");
		sql.append("order by a.user_id ");

		Query query = entityManager.createNativeQuery(sql.toString());

		List<Object[]> list = query.getResultList();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (Object[] objects : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", objects[0]);
			map.put("nickName", objects[1]);
			map.put("time", DateFormatUtils.format((Date)objects[2], "yyyy-MM-dd HH:mm:ss"));
			map.put("mood", objects[3]);
			result.add(map);
		}
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> countChangeMood() {

		StringBuilder sql = new StringBuilder();
		sql.append("select a.user_id as '用户ID',coalesce(c.nick_name,substring(b.phone,8,4)) as '昵称',a.last_time as '设置时间', ");
		sql.append("case when a.mood_type = 1 then '郁闷' when a.mood_type = 2 then '无聊' when a.mood_type = 3 then '得瑟' ");
		sql.append("when a.mood_type = 4 then '丢人' when a.mood_type = 5 then '求安慰' when a.mood_type = 6 then '纠结' end as '心情' ");
		sql.append("from user_mood as a ");
		sql.append("left join user_basic as b on a.user_id = b.id ");
		sql.append("left join user_info as c on a.user_id = c.id ");
		sql.append("where substring(a.last_time,1,10) = substring(now(),1,10) ");
		sql.append("order by a.user_id ");

		Query query = entityManager.createNativeQuery(sql.toString());

		List<Object[]> list = query.getResultList();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		for (Object[] objects : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", objects[0]);
			map.put("nickName", objects[1]);
			map.put("time", DateFormatUtils.format((Date)objects[2], "yyyy-MM-dd HH:mm:ss"));
			map.put("mood", objects[3]);
			result.add(map);
		}
		return result;
	}
	
	@Override
	public List<Map<String, Object>> countTodayMood() {
		StringBuilder sql = new StringBuilder();
		sql.append("select a.user_id as '用户ID',coalesce(c.nick_name,substring(b.phone,8,4)) as '昵称',a.last_time as '设置时间', ");
		sql.append("case when a.mood_type = 1 then '郁闷' when a.mood_type = 2 then '无聊' when a.mood_type = 3 then '得瑟' ");
		sql.append("when a.mood_type = 4 then '丢人' when a.mood_type = 5 then '求安慰' when a.mood_type = 6 then '纠结' end as '心情' ");
		sql.append("from (select user_id,max(last_time) as time from user_mood where substring(last_time,1,10) = substring(now(),1,10) group by user_id) as d ");
		sql.append("left join user_mood as a on d.user_id = a.user_id and d.time = a.last_time ");
		sql.append("left join user_basic as  b on a.user_id = b.id ");
		sql.append("left join user_info as c on a.user_id = c.id ");
		sql.append("order by a.user_id ");

		Query query = entityManager.createNativeQuery(sql.toString());

		@SuppressWarnings("unchecked")
		List<Object[]> list = query.getResultList();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		for (Object[] objects : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", objects[0]);
			map.put("nickName", objects[1]);
			map.put("time", DateFormatUtils.format((Date)objects[2], "yyyy-MM-dd HH:mm:ss"));
			map.put("mood", objects[3]);
			result.add(map);
		}
		return result;
	}
}
