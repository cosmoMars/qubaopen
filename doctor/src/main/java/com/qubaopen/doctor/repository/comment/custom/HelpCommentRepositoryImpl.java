package com.qubaopen.doctor.repository.comment.custom;

import com.qubaopen.survey.entity.comment.Help;
import com.qubaopen.survey.entity.comment.HelpComment;
import com.qubaopen.survey.entity.doctor.Doctor;
import com.qubaopen.survey.entity.vo.HelpCommentVo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HelpCommentRepositoryImpl implements HelpCommentRepositoryCustom {

	@Autowired
	private EntityManager entityManager;
	
	@Override
	public List<HelpComment> findLimitComment(Help help) {
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<HelpComment> query = builder.createQuery(HelpComment.class);
		
		Root<HelpComment> root = query.from(HelpComment.class);
		query.where(builder.equal(root.get("help"), help)).orderBy(builder.asc((root.get("createdDate"))));
		
		return entityManager.createQuery(query)
				.setMaxResults(5).getResultList();
	}

	@Override
	public List<HelpCommentVo> findLimitCommentByGood(Help help) {

		StringBuffer sql = new StringBuffer();

		sql.append("select hc.id hcid, hc.doctor_id, di.name dname, hc.hospital_id hid, hi.name hname, di.avatar_path dpath, ");
		sql.append("hi.hospital_avatar, hc.content, hc.time, a.hsum , a.uid uid, hc.user_id userid, ui.nick_name, ui.avatar_path ");
		sql.append("from help_comment hc ");
		sql.append("left join doctor_info di on hc.doctor_id = di.id ");
		sql.append("left join hospital_info hi on hc.hospital_id = hi.id ");
		sql.append("left join user_info ui on ui.id = hc.user_id ");
		sql.append("left join ( " );
		sql.append("select help_comment_id id, count(help_comment_id) hsum, user_id uid ");
		sql.append("from help_comment_good group by help_comment_id) a ");
		sql.append("on a.id = hc.id ");
		sql.append("where help_id = :helpId ");
		sql.append("order by a.hsum desc ");
//		,hc.created_date desc

		Query query = entityManager.createNativeQuery(sql.toString())
				.setParameter("helpId", help.getId())
				.setMaxResults(5);

		List<Object[]> list = query.getResultList();
		List<HelpCommentVo> result = new ArrayList<>();
		for (Object[] objects : list) {
			HelpCommentVo vo = new HelpCommentVo();
			vo.setCommentId(objects[0] != null ? objects[0].toString() : null);
			vo.setDoctorId(objects[1] != null ? objects[1].toString() : null);
			vo.setDoctorName(objects[2] != null ? objects[2].toString() : null);
			vo.setHospitalId(objects[3] != null ? objects[3].toString() : null);
			vo.setHospitalName(objects[4] != null ? objects[4].toString() : null);
			vo.setDoctorPath(objects[5] != null ? objects[5].toString() : null);
			vo.setHospitalPath(objects[6] != null ? objects[6].toString() : null);
			vo.setCommentContent(objects[7] != null ? objects[7].toString() : null);
			vo.setCommentTime((Date) objects[8]);
			vo.setgSize(objects[9] != null ? Integer.parseInt(objects[9].toString()): 0);
			vo.setUserId(objects[10] != null ? objects[10].toString() : null);
			vo.setHcUserId(objects[11] != null ? objects[11].toString() : null);
			vo.setNickName(objects[12] != null ? objects[12].toString() : null);
			vo.setUserPath(objects[13] != null ? objects[13].toString() : null);
			result.add(vo);
		}

		return result;
	}

	@Override
	public List<HelpCommentVo> findLimitCommentByGood(Help help, Doctor doctor) {

		StringBuffer sql = new StringBuffer();

		sql.append("select hc.id hcid, hc.doctor_id, di.name dname, hc.hospital_id hid, hi.name hname, di.avatar_path dpath, ");
		sql.append("hi.hospital_avatar, hc.content, hc.time, a.hsum , a.uid uid, hc.user_id userid, ui.nick_name, ui.avatar_path ");
		sql.append("from help_comment hc ");
		sql.append("left join doctor_info di on hc.doctor_id = di.id ");
		sql.append("left join hospital_info hi on hc.hospital_id = hi.id ");
		sql.append("left join user_info ui on ui.id = hc.user_id ");
		sql.append("left join ( " );
		sql.append("select help_comment_id id, count(help_comment_id) hsum, user_id uid ");
		sql.append("from help_comment_good group by help_comment_id) a ");
		sql.append("on a.id = hc.id ");
		sql.append("where help_id = :helpId and di.id = :doctorId ");
		sql.append("order by a.hsum desc,hc.created_date desc ");

		Query query = entityManager.createNativeQuery(sql.toString())
				.setParameter("helpId", help.getId())
				.setParameter("doctorId", doctor.getId())
				.setMaxResults(5);

		List<Object[]> list = query.getResultList();
		List<HelpCommentVo> result = new ArrayList<>();
		for (Object[] objects : list) {
			HelpCommentVo vo = new HelpCommentVo();
			vo.setCommentId(objects[0] != null ? objects[0].toString() : null);
			vo.setDoctorId(objects[1] != null ? objects[1].toString() : null);
			vo.setDoctorName(objects[2] != null ? objects[2].toString() : null);
			vo.setHospitalId(objects[3] != null ? objects[3].toString() : null);
			vo.setHospitalName(objects[4] != null ? objects[4].toString() : null);
			vo.setDoctorPath(objects[5] != null ? objects[5].toString() : null);
			vo.setHospitalPath(objects[6] != null ? objects[6].toString() : null);
			vo.setCommentContent(objects[7] != null ? objects[7].toString() : null);
			vo.setCommentTime((Date) objects[8]);
			vo.setgSize(objects[9] != null ? Integer.parseInt(objects[9].toString()): 0);
			vo.setUserId(objects[10] != null ? objects[10].toString() : null);
			vo.setHcUserId(objects[11] != null ? objects[11].toString() : null);
			vo.setNickName(objects[12] != null ? objects[12].toString() : null);
			vo.setUserPath(objects[13] != null ? objects[13].toString() : null);
			result.add(vo);
		}

		return result;
	}

}
