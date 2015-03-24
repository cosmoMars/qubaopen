package com.qubaopen.survey.entity.user;

import com.qubaopen.core.entity.AbstractBaseEntity;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Date;

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
	// @Pattern(regexp = "^[a-zA-Z0-9_]{8,30}$", message =
	// "{\"success\" : 0, \"message\": \"密码格式不正确\"}")
	private String password;

	/**
	 * 电话
	 */
//	@NotEmpty(message = "{\"success\" : 0, \"message\": \"err003\"}")
	@Pattern(regexp = "^1[0-9]{10}$", message = "{\"success\" : 0, \"message\": \"err003\"}")
	@Column(unique = true, length = 11)
	private String phone;

	/**
	 * 邮箱
	 */
	@Email(message = "{\"success\" : 0, \"message\": \"err005\"}")
	@Column(unique = true)
	private String email;

	/**
	 * 是否激活
	 */
	private boolean activated;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
	private UserInfo userInfo;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
	private UserIDCardBind userIdCardBind;
	
	/**
	 * 第三方token
	 */
	private String token;
	
	/**
	 * 第三方类型
	 */
	private ThirdType thirdType;

    @Temporal(TemporalType.DATE)
    private Date loginDate;
	
	public enum ThirdType {
		Sina, WeChat, Qzone
	}

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
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public UserIDCardBind getUserIdCardBind() {
		return userIdCardBind;
	}

	public void setUserIdCardBind(UserIDCardBind userIdCardBind) {
		this.userIdCardBind = userIdCardBind;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public ThirdType getThirdType() {
		return thirdType;
	}

	public void setThirdType(ThirdType thirdType) {
		this.thirdType = thirdType;
	}

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }
}
