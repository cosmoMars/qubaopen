package com.qubaopen.survey.entity.user;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity2;

@Entity
@Table(name = "user_third")
@Audited
public class UserThird extends AbstractBaseEntity2<Long>{

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
	private Integer thirdType;

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

	public Integer getThirdType() {
		return thirdType;
	}

	public void setThirdType(Integer thirdType) {
		this.thirdType = thirdType;
	}

}
