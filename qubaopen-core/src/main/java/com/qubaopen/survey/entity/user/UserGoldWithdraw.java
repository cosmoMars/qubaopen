package com.qubaopen.survey.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;

/**
 * 用户金币提现 Created by duel on 2014/6/30.
 */
@Entity
@Table(name = "user_gold_withdraw")
@Audited
public class UserGoldWithdraw extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -8177942165673603334L;

	/**
	 * 提现用户
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id")
	private User user;

	/**
	 * 提现数量
	 */
	private int amount;

	/**
	 * 提现账号
	 */
	private String account;

	/**
	 * 提现审核状态 0审核中 1成功 2失败
	 */
	@Enumerated
	private Status status;

	/**
	 * ADUITING 0 审核中, SUCCEED 1 成功, FAILURE 2 失败
	 */
	private enum Status {
		ADUITING, SUCCEED, FAILURE
	}

	/**
	 * 提现方式 0银行卡 1支付宝
	 */
	@Enumerated
	private Way way;

	/**
	 * BANKCARD 0 银行卡, ALIPAY 1 支付宝
	 */
	private enum Way {
		BANKCARD, ALIPAY
	}

	/**
	 * 交易号 提现成功时使用
	 */
	private String transactionNumber;

	/**
	 * 失败原因 提现失败时使用
	 */
	private String failureReason;

	/**
	 * 实际到账金额 般指扣除手续费以后的金额
	 */
	@Column(columnDefinition = "DECIMAL(7,2)")
	private Double receivedValue;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Way getWay() {
		return way;
	}

	public void setWay(Way way) {
		this.way = way;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}

	public Double getReceivedValue() {
		return receivedValue;
	}

	public void setReceivedValue(Double receivedValue) {
		this.receivedValue = receivedValue;
	}

}
