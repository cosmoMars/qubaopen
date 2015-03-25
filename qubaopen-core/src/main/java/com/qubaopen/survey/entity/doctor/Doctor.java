package com.qubaopen.survey.entity.doctor;

import com.qubaopen.core.entity.AbstractBaseEntity;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author mars 医师
 */
@Entity
@Table(name = "doctor_basic")
@Audited
public class Doctor extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -5656757531925963346L;

	/**
	 * 手机
	 */
	private String phone;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 邮箱
	 */
	private String email;

	// /**
	// * token
	// */
	// private String token;

	// /**
	// * 第三方类型
	// */
	// @Enumerated
	// private ThirdType thirdType;
	//
	// private enum ThirdType {
	// Sina, WeChat, Qzone
	// }
	//
	/**
	 * 启用
	 */
	private boolean activated;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "doctor")
	private DoctorInfo doctorInfo;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "doctor")
	private DoctorIdCardBind doctorIdCardBind;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Doctor doctor = (Doctor) o;

        if (email != null ? !email.equals(doctor.email) : doctor.email != null) return false;
        if (password != null ? !password.equals(doctor.password) : doctor.password != null) return false;
        if (phone != null ? !phone.equals(doctor.phone) : doctor.phone != null) return false;

        return true;
    }

    public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public DoctorInfo getDoctorInfo() {
		return doctorInfo;
	}

	public void setDoctorInfo(DoctorInfo doctorInfo) {
		this.doctorInfo = doctorInfo;
	}

	public DoctorIdCardBind getDoctorIdCardBind() {
		return doctorIdCardBind;
	}

	public void setDoctorIdCardBind(DoctorIdCardBind doctorIdCardBind) {
		this.doctorIdCardBind = doctorIdCardBind;
	}

}
