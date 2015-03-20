package com.qubaopen.survey.entity.hospital;

import com.qubaopen.core.entity.AbstractBaseEntity2;
import com.qubaopen.survey.entity.base.AreaCode;
import com.qubaopen.survey.entity.doctor.Genre;
import com.qubaopen.survey.entity.doctor.TargetUser;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "hospital_info")
@Audited
public class HospitalInfo extends AbstractBaseEntity2<Long> {

	private static final long serialVersionUID = -4979217651297221804L;

	/**
	 * 医院
	 */
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@PrimaryKeyJoinColumn
	private Hospital hospital;

	/**
	 * 企业名
	 */
	private String name;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 成立时间
	 */
	@Temporal(TemporalType.DATE)
	private Date establishTime;

	/**
	 * 预约时间
	 */
	private String bookingTime;

	/**
	 * 联系电话
	 */
	private String phone;

	/**
	 * 紧急电话
	 */
	private String urgentPhone;

	/**
	 * qq
	 */
	private String qq;

	/**
	 * 介绍
	 */
	private String introduce;

	/**
	 * 文字咨询
	 */
	private boolean wordsConsult;
	
	/**
	 * 电话咨询
	 */
	private boolean phoneConsult;

	/**
	 * 最低收费
	 */
	private int minCharge;

	/**
	 * 最大收费
	 */
	private int maxCharge;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "hospitalInfo", cascade = { CascadeType.REMOVE, CascadeType.PERSIST })
	private Set<HospitalDoctorRecord> hospitalDoctorRecords;

    /**
     * 诊所证书
     */
    private String hospitalRecordPath;

	/**
	 * 审计状态
	 */
	@Enumerated
	private LoginStatus loginStatus;

	public enum LoginStatus {
		Unaudited, Auditing, Refusal, Audited
	}

	/**
	 * 拒绝理由
	 */
	private String refusalReason;
	

	/**
	 * 对象
	 */
	private String targetUser;

	/**
	 * 流派
	 */
	private String genre;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private AreaCode areaCode;
	
	/**
	 * 擅长人群
	 */
	@ManyToMany
	@JoinTable(name = "hospital_target_relation", joinColumns = @JoinColumn(name = "hospital_id"), inverseJoinColumns = @JoinColumn(name = "target_id"))
	private Set<TargetUser> targetUsers;

	/**
	 * 流派
	 */
	@ManyToMany
	@JoinTable(name = "hospital_genre_relation", joinColumns = @JoinColumn(name = "hospital_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
	private Set<Genre> genres;

    /**
     * 面对面
     */
    private boolean faceToFace;

    /**
     * 视频
     */
    private boolean video;

    /**
     * 诊所头像
     */
    private String hospitalAvatar;

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getEstablishTime() {
		return establishTime;
	}

	public void setEstablishTime(Date establishTime) {
		this.establishTime = establishTime;
	}

	public String getBookingTime() {
		return bookingTime;
	}

	public void setBookingTime(String bookingTime) {
		this.bookingTime = bookingTime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUrgentPhone() {
		return urgentPhone;
	}

	public void setUrgentPhone(String urgentPhone) {
		this.urgentPhone = urgentPhone;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public boolean isWordsConsult() {
		return wordsConsult;
	}

	public void setWordsConsult(boolean wordsConsult) {
		this.wordsConsult = wordsConsult;
	}

	public boolean isPhoneConsult() {
		return phoneConsult;
	}

	public void setPhoneConsult(boolean phoneConsult) {
		this.phoneConsult = phoneConsult;
	}

	public int getMinCharge() {
		return minCharge;
	}

	public void setMinCharge(int minCharge) {
		this.minCharge = minCharge;
	}

	public int getMaxCharge() {
		return maxCharge;
	}

	public void setMaxCharge(int maxCharge) {
		this.maxCharge = maxCharge;
	}

	public Set<HospitalDoctorRecord> getHospitalDoctorRecords() {
		return hospitalDoctorRecords;
	}

	public void setHospitalDoctorRecords(Set<HospitalDoctorRecord> hospitalDoctorRecords) {
		this.hospitalDoctorRecords = hospitalDoctorRecords;
	}

	public String getHospitalRecordPath() {
		return hospitalRecordPath;
	}

	public void setHospitalRecordPath(String hospitalRecordPath) {
		this.hospitalRecordPath = hospitalRecordPath;
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

    public String getHospitalAvatar() {
        return hospitalAvatar;
    }

    public void setHospitalAvatar(String hospitalAvatar) {
        this.hospitalAvatar = hospitalAvatar;
    }
}
