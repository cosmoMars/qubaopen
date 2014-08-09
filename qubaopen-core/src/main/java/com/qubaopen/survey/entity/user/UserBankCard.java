package com.qubaopen.survey.entity.user;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author mars 用户银行卡信息
 */
@Entity
@Table(name = "user_bank_card")
@Audited
public class UserBankCard extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -469527906871111122L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	/**
	 * 用户提现银行
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_withdraw_bank_type_id")
	private UserWithdrawBankType userWithdrawBankType;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 卡号标示
	 */
	private String cardIdentity;

	/**
	 * 是否启用
	 */
	private Boolean enabled;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCardIdentity() {
		return cardIdentity;
	}

	public void setCardIdentity(String cardIdentity) {
		this.cardIdentity = cardIdentity;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public UserWithdrawBankType getUserWithdrawBankType() {
		return userWithdrawBankType;
	}

	public void setUserWithdrawBankType(UserWithdrawBankType userWithdrawBankType) {
		this.userWithdrawBankType = userWithdrawBankType;
	}

}
