package com.qubaopen.survey.entity.user;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;

/**
 * @author mars 用户身份证绑定关系表，将用户ID和其认证过的身份证ID进行绑定
 *
 */
@Entity
@Table(name = "user_id_card_bind")
@Audited
public class UserIDCardBind extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -8702109493101811229L;

	/**
	 * 用户身份证id
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id_card", nullable = false, unique = true)
	private UserIDCard userIDCard;

	/**
	 * 用户
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private User user;

	public UserIDCard getUserIDCard() {
		return userIDCard;
	}

	public void setUserIDCard(UserIDCard userIDCard) {
		this.userIDCard = userIDCard;
	}

}
