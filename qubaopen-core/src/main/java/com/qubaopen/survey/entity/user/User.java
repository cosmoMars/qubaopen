package com.qubaopen.survey.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.qubaopen.core.entity.AbstractBaseEntity;

/**
 * @author mars 用户表
 */
@Entity
@Table(name = "user_basic")
@Audited
public class User extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -6865482202586788603L;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 密码
	 */
	@Size(min = 6, max = 16, message = "{\"success\" : 0, \"message\": \"密码长度为6-16个字符\"}")
	private String password;

	/**
	 * 电话
	 */
	@NotEmpty(message = "{\"success\" : 0, \"errCode\": \"101\", \"message\": \"手机号码为空或格式不正确\"}")
	@Pattern(regexp = "^1[3458][0-9]{9}$", message = "{\"success\" : 0, \"message\": \"手机号码为空或格式不正确\"}")
	@Column(unique = true, length = 11)
	private String phone;

	/**
	 * 邮箱
	 */
	@Email
	@Column(unique = true)
	private String email;

	/**
	 * 是否激活
	 */
	private boolean isActivated;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isActivated() {
		return isActivated;
	}

	public void setActivated(boolean isActivated) {
		this.isActivated = isActivated;
	}

}
