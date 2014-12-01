package com.qubaopen.survey.entity.doctor;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.user.User;

@Entity
@Table(name = "doctor_booking")
@Audited
public class DoctorBooking extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 6872015169540197635L;

	@ManyToOne(fetch = FetchType.LAZY)
	private Doctor doctor;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 性别
	 */
	@Enumerated
	private Sex sex;

	private enum Sex {
		Male, Female, Other
	}

	@Temporal(TemporalType.DATE)
	private Date birthday;
	
	/**
	 * 职业
	 */
	private String profession;

	/**
	 * 上海
	 */
	private String city;

	/**
	 * 是否已婚
	 */
	private boolean married;

	/**
	 * 是否有孩子
	 */
	private boolean haveChildren;

	/**
	 * 求助原因
	 */
	private String helpReason;

	/**
	 * 其他问题
	 */
	private String otherProblem;

	/**
	 * 是否接受过治疗
	 */
	private boolean treatmented;

	/**
	 * 是否咨询过
	 */
	private boolean haveConsulted;

	/**
	 * 拒绝原因
	 */
	private String refusalReason;

	@Temporal(TemporalType.TIMESTAMP)
	private Date time;

	private boolean quick;

	@Enumerated
	private ConsultType consultType;

	private enum ConsultType {
		Facetoface, Video
	}

	@Enumerated
	private Status status;

	/**
	 * @author mars
	 * 预约，接受，拒绝，已咨询，未咨询，已约下次
	 */
	public enum Status {
		Booking, Accept, Refusal, Consulted, Consulting, Next
	}

	private int money;

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public boolean isMarried() {
		return married;
	}

	public void setMarried(boolean married) {
		this.married = married;
	}

	public boolean isHaveChildren() {
		return haveChildren;
	}

	public void setHaveChildren(boolean haveChildren) {
		this.haveChildren = haveChildren;
	}

	public String getHelpReason() {
		return helpReason;
	}

	public void setHelpReason(String helpReason) {
		this.helpReason = helpReason;
	}

	public String getOtherProblem() {
		return otherProblem;
	}

	public void setOtherProblem(String otherProblem) {
		this.otherProblem = otherProblem;
	}

	public boolean isTreatmented() {
		return treatmented;
	}

	public void setTreatmented(boolean treatmented) {
		this.treatmented = treatmented;
	}

	public boolean isHaveConsulted() {
		return haveConsulted;
	}

	public void setHaveConsulted(boolean haveConsulted) {
		this.haveConsulted = haveConsulted;
	}

	public String getRefusalReason() {
		return refusalReason;
	}

	public void setRefusalReason(String refusalReason) {
		this.refusalReason = refusalReason;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public boolean isQuick() {
		return quick;
	}

	public void setQuick(boolean quick) {
		this.quick = quick;
	}

	public ConsultType getConsultType() {
		return consultType;
	}

	public void setConsultType(ConsultType consultType) {
		this.consultType = consultType;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

}
