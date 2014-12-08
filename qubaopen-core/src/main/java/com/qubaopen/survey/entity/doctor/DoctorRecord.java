package com.qubaopen.survey.entity.doctor;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity2;

/**
 * @author mars
 * 资质证明
 */
@Entity
@Table(name = "doctor_record")
@Audited
public class DoctorRecord extends AbstractBaseEntity2<Long> {

	private static final long serialVersionUID = 7375534278568897538L;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@PrimaryKeyJoinColumn
	private Doctor doctor;

	/**
	 * 证明
	 */
	private String recordPath;

	/**
	 * 学习开始
	 */
	@Temporal(TemporalType.DATE)
	private Date educationalStart;

	/**
	 * 学习结束
	 */
	@Temporal(TemporalType.DATE)
	private Date educationalEnd;

	/**
	 * 学校
	 */
	private String school;

	/**
	 * 专业
	 */
	private String profession;

	/**
	 * 学位
	 */
	private String degree;

	/**
	 * 学业介绍
	 */
	@Column(length = 1000)
	private String educationalIntroduction;

	/**
	 * 培训开始时间
	 */
	@Temporal(TemporalType.DATE)
	private Date trainStart;

	/**
	 * 培训结束时间
	 */
	@Temporal(TemporalType.DATE)
	private Date trainEnd;

	/**
	 * 课程
	 */
	private String course;

	/**
	 * 机构
	 */
	private String organization;

	/**
	 * 培训介绍
	 */
	@Column(length = 1000)
	private String trainIntroduction;

	/**
	 * 督导
	 */
	private String supervise;

	/**
	 * 取向
	 */
	private String orientation;

	/**
	 * 督导学习时间
	 */
	private int superviseHour;

	/**
	 * 督导联系方式
	 */
	private String contactMethod;

	/**
	 * 督导介绍
	 */
	@Column(length = 1000)
	private String superviseIntroduction;

	/**
	 * 开始时间
	 */
	@Temporal(TemporalType.DATE)
	private Date selfStart;

	/**
	 * 结束时间
	 */
	@Temporal(TemporalType.DATE)
	private Date selfEnd;

	/**
	 * 总小时
	 */
	private int totalHour;

	/**
	 * 自我介绍
	 */
	@Column(length = 1000)
	private String selfIntroduction;

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public String getRecordPath() {
		return recordPath;
	}

	public void setRecordPath(String recordPath) {
		this.recordPath = recordPath;
	}

	public Date getEducationalStart() {
		return educationalStart;
	}

	public void setEducationalStart(Date educationalStart) {
		this.educationalStart = educationalStart;
	}

	public Date getEducationalEnd() {
		return educationalEnd;
	}

	public void setEducationalEnd(Date educationalEnd) {
		this.educationalEnd = educationalEnd;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getEducationalIntroduction() {
		return educationalIntroduction;
	}

	public void setEducationalIntroduction(String educationalIntroduction) {
		this.educationalIntroduction = educationalIntroduction;
	}

	public Date getTrainStart() {
		return trainStart;
	}

	public void setTrainStart(Date trainStart) {
		this.trainStart = trainStart;
	}

	public Date getTrainEnd() {
		return trainEnd;
	}

	public void setTrainEnd(Date trainEnd) {
		this.trainEnd = trainEnd;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getTrainIntroduction() {
		return trainIntroduction;
	}

	public void setTrainIntroduction(String trainIntroduction) {
		this.trainIntroduction = trainIntroduction;
	}

	public String getSupervise() {
		return supervise;
	}

	public void setSupervise(String supervise) {
		this.supervise = supervise;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public int getSuperviseHour() {
		return superviseHour;
	}

	public void setSuperviseHour(int superviseHour) {
		this.superviseHour = superviseHour;
	}

	public String getContactMethod() {
		return contactMethod;
	}

	public void setContactMethod(String contactMethod) {
		this.contactMethod = contactMethod;
	}

	public String getSuperviseIntroduction() {
		return superviseIntroduction;
	}

	public void setSuperviseIntroduction(String superviseIntroduction) {
		this.superviseIntroduction = superviseIntroduction;
	}

	public Date getSelfStart() {
		return selfStart;
	}

	public void setSelfStart(Date selfStart) {
		this.selfStart = selfStart;
	}

	public Date getSelfEnd() {
		return selfEnd;
	}

	public void setSelfEnd(Date selfEnd) {
		this.selfEnd = selfEnd;
	}

	public int getTotalHour() {
		return totalHour;
	}

	public void setTotalHour(int totalHour) {
		this.totalHour = totalHour;
	}

	public String getSelfIntroduction() {
		return selfIntroduction;
	}

	public void setSelfIntroduction(String selfIntroduction) {
		this.selfIntroduction = selfIntroduction;
	}

}
