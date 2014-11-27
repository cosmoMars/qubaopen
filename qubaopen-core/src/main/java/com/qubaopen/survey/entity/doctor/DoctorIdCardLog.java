package com.qubaopen.survey.entity.doctor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;

@Entity
@Table(name = "doctor_id_card_log")
@Audited
public class DoctorIdCardLog extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -4049046969753150834L;

	/**
	 * 用户
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Doctor doctor;

	/**
	 * 身份证
	 */
	private String idCard;

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 照片
	 */
	private String photoPath;

	/**
	 * 验证状态 0 处理成功 compStatus 3 一致 2 不一致 1 库中无此号 local 绑定本地数据 used 已经被绑定
	 */
	private String status;

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
