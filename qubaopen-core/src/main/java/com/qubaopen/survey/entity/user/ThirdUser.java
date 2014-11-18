package com.qubaopen.survey.entity.user;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity2;

@Entity
@Table(name = "third_user")
@Audited
public class ThirdUser extends AbstractBaseEntity2<Long>{

	private static final long serialVersionUID = -3618846295054686658L;
	
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@PrimaryKeyJoinColumn
	private User user;

	/**
	 * token
	 */
	private String token;
	
	/**
	 * 昵称
	 */
	private String nickName;
	
	/**
	 * 头像
	 */
	private String avatarUrl;
	
	/**
	 * 第三方类型
	 */
	private ThirdType thirdType;
	
	public enum ThirdType {
		Sina, WeChat, Qzone
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public ThirdType getThirdType() {
		return thirdType;
	}

	public void setThirdType(ThirdType thirdType) {
		this.thirdType = thirdType;
	}
	
}
