package com.qubaopen.survey.entity.doctor;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.hospital.Hospital;

/**
 * @author mars 医师信息
 */
@Entity
@Table(name = "doctor_info")
@Audited
public class DoctorInfo extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 2535242382334786060L;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@PrimaryKeyJoinColumn
	private Doctor doctor;

	@ManyToOne(fetch = FetchType.LAZY)
	private Hospital hospital;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 手机
	 */
	private String phone;

	/**
	 * 性别
	 */
	private Sex sex;

	private enum Sex {
		Male, Female
	}

	/**
	 * 生日
	 */
	@Temporal(TemporalType.DATE)
	private Date birthday;

	/**
	 * 资质
	 */
	private String experience;

	/**
	 * 领域
	 */
	private String field;

	/**
	 * qq
	 */
	private String qq;

	/**
	 * 面对面
	 */
	private boolean faceToFace;

	/**
	 * 视频
	 */
	private boolean video;

	/**
	 * 对象
	 */
	private String targetUser;

	/**
	 * 流派
	 */
	private String genre;

	/**
	 * 预约时间
	 */
	private String bookingTime;

	/**
	 * 文字咨询
	 */
	private boolean commentConsult;

	/**
	 * 电话咨询
	 */
	private boolean phoneConsult;

	/**
	 * 是否加急
	 */
	private boolean quick;

	/**
	 * 自我介绍
	 */
	private String introduce;

	/**
	 * 头像
	 */
	private String avatarPath;

	/**
	 * 资质证书
	 */
	private String recordPath;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public boolean isFaceToFace() {
		return faceToFace;
	}

	public void setFaceToFace(boolean faceToFace) {
		this.faceToFace = faceToFace;
	}

	public boolean isVideo() {
		return video;
	}

	public void setVideo(boolean video) {
		this.video = video;
	}

	public String getTargetUser() {
		return targetUser;
	}

	public void setTargetUser(String targetUser) {
		this.targetUser = targetUser;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getBookingTime() {
		return bookingTime;
	}

	public void setBookingTime(String bookingTime) {
		this.bookingTime = bookingTime;
	}

	public boolean isCommentConsult() {
		return commentConsult;
	}

	public void setCommentConsult(boolean commentConsult) {
		this.commentConsult = commentConsult;
	}

	public boolean isPhoneConsult() {
		return phoneConsult;
	}

	public void setPhoneConsult(boolean phoneConsult) {
		this.phoneConsult = phoneConsult;
	}

	public boolean isQuick() {
		return quick;
	}

	public void setQuick(boolean quick) {
		this.quick = quick;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getAvatarPath() {
		return avatarPath;
	}

	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}

	public String getRecordPath() {
		return recordPath;
	}

	public void setRecordPath(String recordPath) {
		this.recordPath = recordPath;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public Hospital getHospital() {
		return hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}

}
