package com.qubaopen.survey.entity.hospital;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.cash.Bank;

@Entity
@Table(name = "hospital_take_cash")
@Audited
public class HospitalTakeCash extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -3197353607810476849L;

	@ManyToOne(fetch = FetchType.LAZY)
	private Hospital hospital;

	/**
	 * 提现金额
	 */
	private double cash;

	/**
	 * 支付宝帐号
	 */
	private String alipayNum;

	/**
	 * 银行卡号
	 */
	private String bankCard;

	/**
	 * 银行类型
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Bank bank;

	/**
	 * 交易状态
	 */
	@Enumerated
	private Status status;

	public enum Status {
		Auditing, Success, Failure
	}

	/**
	 * 交易类型
	 */
	@Enumerated
	private Type type;

	public enum Type {
		Alipay, BackCard
	}

	/**
	 * 交易号
	 */
	private String transactionNum;

	/**
	 * 失败原因
	 */
	private String failureReason;

	public Hospital getHospital() {
		return hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}

	public double getCash() {
		return cash;
	}

	public void setCash(double cash) {
		this.cash = cash;
	}

	public String getAlipayNum() {
		return alipayNum;
	}

	public void setAlipayNum(String alipayNum) {
		this.alipayNum = alipayNum;
	}

	public String getBankCard() {
		return bankCard;
	}

	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getTransactionNum() {
		return transactionNum;
	}

	public void setTransactionNum(String transactionNum) {
		this.transactionNum = transactionNum;
	}

	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}

}
