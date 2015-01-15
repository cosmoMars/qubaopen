package com.qubaopen.survey.entity.booking;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.doctor.Doctor;
import com.qubaopen.survey.entity.hospital.Hospital;
import com.qubaopen.survey.entity.user.User;

/**
 * @author mars 订单详情
 */
@Entity
@Table(name = "booking")
@Audited
public class Booking extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 6872015169540197635L;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	private Doctor doctor;

	@ManyToOne(fetch = FetchType.LAZY)
	private Hospital hospital;

	/**
	 * 订单号
	 */
	@Column(unique = true)
	private String tradeNo;

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 手机
	 */
	private String phone;

	/**
	 * 性别
	 */
	@Enumerated
	private Sex sex;

	public enum Sex {
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

	/**
	 * 下单时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date time;

	/**
	 * 支付时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date payTime;

	private boolean quick;

	@Enumerated
	private ConsultType consultType;

	public enum ConsultType {
		Facetoface, Video
	}

	@Enumerated
	private Status status;

	/**
	 * 1 预约，2 接受，拒绝，3 已咨询，未咨询，4 已约下次, 5 已付款
	 */
	public enum Status {
		Booking, Accept, Refusal, Consulted, Consulting, Next, Paid, PayAccept, ChangeDate
	}

	private double money;

	private enum BookStatus {
		Consulted, NoConsult
	}

	/**
	 * 用户订单状态
	 */
	@Enumerated
	private BookStatus userStatus;

	/**
	 * 医师订单状态
	 */
	@Enumerated
	private BookStatus doctorStatus;

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
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

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public BookStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(BookStatus userStatus) {
		this.userStatus = userStatus;
	}

	public BookStatus getDoctorStatus() {
		return doctorStatus;
	}

	public void setDoctorStatus(BookStatus doctorStatus) {
		this.doctorStatus = doctorStatus;
	}

}
