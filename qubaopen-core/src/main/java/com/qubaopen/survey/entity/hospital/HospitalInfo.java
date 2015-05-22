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
	@OrderBy(value = "id asc")
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

    /**
     * 复审
     */
    private boolean review;

    /**
     * 复审原因
     */
    private String reviewReason;

	/**
	 * 每月结算日
	 */
	private int clearDay = 1;

	/**
	 * 称号
	 */
	private String appellation;

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HospitalInfo that = (HospitalInfo) o;

        if (faceToFace != that.faceToFace) return false;
        if (maxCharge != that.maxCharge) return false;
        if (minCharge != that.minCharge) return false;
        if (phoneConsult != that.phoneConsult) return false;
        if (video != that.video) return false;
        if (wordsConsult != that.wordsConsult) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (areaCode != null ? !areaCode.equals(that.areaCode) : that.areaCode != null) return false;
        if (establishTime != null ? !establishTime.equals(that.establishTime) : that.establishTime != null)
            return false;
        if (genre != null ? !genre.equals(that.genre) : that.genre != null) return false;
        if (hospitalAvatar != null ? !hospitalAvatar.equals(that.hospitalAvatar) : that.hospitalAvatar != null)
            return false;
        if (hospitalRecordPath != null ? !hospitalRecordPath.equals(that.hospitalRecordPath) : that.hospitalRecordPath != null)
            return false;
        if (introduce != null ? !introduce.equals(that.introduce) : that.introduce != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
        if (qq != null ? !qq.equals(that.qq) : that.qq != null) return false;
        if (targetUser != null ? !targetUser.equals(that.targetUser) : that.targetUser != null) return false;
        if (urgentPhone != null ? !urgentPhone.equals(that.urgentPhone) : that.urgentPhone != null) return false;

        return true;
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

    public boolean isReview() {
        return review;
    }

    public void setReview(boolean review) {
        this.review = review;
    }

    public String getReviewReason() {
        return reviewReason;
    }

    public void setReviewReason(String reviewReason) {
        this.reviewReason = reviewReason;
    }

	public int getClearDay() {
		return clearDay;
	}

	public void setClearDay(int clearDay) {
		this.clearDay = clearDay;
	}

	public String getAppellation() {
		return appellation;
	}

	public void setAppellation(String appellation) {
		this.appellation = appellation;
	}
}
