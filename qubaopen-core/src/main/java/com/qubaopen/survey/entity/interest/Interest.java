package com.qubaopen.survey.entity.interest;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qubaopen.core.entity.AbstractBaseEntity;

/**
 * @author mars 兴趣问卷
 */
@Entity
@Table(name = "interest")
@Audited
public class Interest extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 2686784453446880940L;

	/**
	 * 问卷类型
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "interest_type_id")
	private InterestType interestType;

	/**
	 * 标题
	 */
	private String titile;

	/**
	 * 金币
	 */
	private Integer golds;

	/**
	 * 状态 问卷状态值: 0 初始状态 1 上线状态 2 关闭状态
	 */
	@Enumerated
	private Status status;

	/**
	 * INITIAL 0 初始状态, ONLINE, 1 上线状态, CLOSED 2 关闭状态
	 */
	private enum Status {
		INITIAL, ONLINE, CLOSED
	}

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 答题总人数
	 */
	private Integer totalRespondentsCount;

	/**
	 * 兴趣问卷内容类型表
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "interest_Option_type_id")
	private InterestOptionType interestOptionType;

	/**
	 * 推荐值
	 */
	private Integer recommendedValue;

	// @OneToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "interest_result_id", nullable = false, unique = true)
	// private InterestResult interestResult;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private byte[] pic;

	public String getTitile() {
		return titile;
	}

	public void setTitile(String titile) {
		this.titile = titile;
	}

	public Integer getGolds() {
		return golds;
	}

	public void setGolds(Integer golds) {
		this.golds = golds;
	}

	public InterestType getInterestType() {
		return interestType;
	}

	public void setInterestType(InterestType interestType) {
		this.interestType = interestType;
	}

	public Integer getRecommendedValue() {
		return recommendedValue;
	}

	public void setRecommendedValue(Integer recommendedValue) {
		this.recommendedValue = recommendedValue;
	}

	public Integer getTotalRespondentsCount() {
		return totalRespondentsCount;
	}

	public void setTotalRespondentsCount(Integer totalRespondentsCount) {
		this.totalRespondentsCount = totalRespondentsCount;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	// public InterestResult getInterestResult() {
	// return interestResult;
	// }
	//
	// public void setInterestResult(InterestResult interestResult) {
	// this.interestResult = interestResult;
	// }

	public byte[] getPic() {
		return pic;
	}

	public void setPic(byte[] pic) {
		this.pic = pic;
	}

	public InterestOptionType getInterestOptionType() {
		return interestOptionType;
	}

	public void setInterestOptionType(InterestOptionType interestOptionType) {
		this.interestOptionType = interestOptionType;
	}

}
