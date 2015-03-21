package com.qubaopen.survey.entity.doctor;

import com.qubaopen.core.entity.AbstractBaseEntity2;
import com.qubaopen.survey.entity.base.AreaCode;
import com.qubaopen.survey.entity.hospital.Hospital;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * @author mars 医师信息
 */
@Entity
@Table(name = "doctor_info")
@Audited
public class DoctorInfo extends AbstractBaseEntity2<Long> {

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

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 审计状态
	 */
	@Enumerated
	public LoginStatus loginStatus;

	public enum LoginStatus {
		Unaudited, Auditing, Refusal, Audited
	}

	/**
	 * 拒绝理由
	 */
	private String refusalReason;

	@ManyToOne(fetch = FetchType.LAZY)
	private AreaCode areaCode;

	/**
	 * 在线咨询费用
	 */
	private double onlineFee;

	/**
	 * 线下咨询费用
	 */
	private double offlineFee;

	/**
	 * 擅长人群
	 */
	@ManyToMany
	@JoinTable(name = "doctor_target_relation", joinColumns = @JoinColumn(name = "doctor_id"), inverseJoinColumns = @JoinColumn(name = "target_id"))
	private Set<TargetUser> targetUsers;

	/**
	 * 流派
	 */
	@ManyToMany
	@JoinTable(name = "doctor_genre_relation", joinColumns = @JoinColumn(name = "doctor_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
	private Set<Genre> genres;

    /**
     * 复审
     */
    private boolean review;

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LoginStatus getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(LoginStatus loginStatus) {
		this.loginStatus = loginStatus;
	}

	public String getRefusalReason() {
		return refusalReason;
	}

	public void setRefusalReason(String refusalReason) {
		this.refusalReason = refusalReason;
	}

	public AreaCode getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(AreaCode areaCode) {
		this.areaCode = areaCode;
	}

	public Set<TargetUser> getTargetUsers() {
		return targetUsers;
	}

	public void setTargetUsers(Set<TargetUser> targetUsers) {
		this.targetUsers = targetUsers;
	}

	public Set<Genre> getGenres() {
		return genres;
	}

	public void setGenres(Set<Genre> genres) {
		this.genres = genres;
	}

	public double getOnlineFee() {
		return onlineFee;
	}

	public void setOnlineFee(double onlineFee) {
		this.onlineFee = onlineFee;
	}

	public double getOfflineFee() {
		return offlineFee;
	}

	public void setOfflineFee(double offlineFee) {
		this.offlineFee = offlineFee;
	}

    public boolean isReview() {
        return review;
    }

    public void setReview(boolean review) {
        this.review = review;
    }
}
