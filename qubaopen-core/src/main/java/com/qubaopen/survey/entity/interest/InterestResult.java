package com.qubaopen.survey.entity.interest;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author mars 兴趣问卷结果表
 */
@Entity
@Table(name = "interest_result")
@Audited
public class InterestResult extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 692329675843837712L;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "interest_id", nullable = false)
	private Interest interest;

//	/**
//	 * 问卷类型
//	 */
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "interest_result_type_id")
//	private InterestResultType interestResultType;

	/**
	 * 问卷类型 ORDER 顺序得答案, SCORE 积分得答案
	 */
	@Enumerated
	private ResultType resultType;

	private enum ResultType {
		OREDER, SCORE
	}

	/**
	 * 标题
	 */
	private String title;

	public Interest getInterest() {
		return interest;
	}

	public void setInterest(Interest interest) {
		this.interest = interest;
	}

	public ResultType getResultType() {
		return resultType;
	}

	public void setResultType(ResultType resultType) {
		this.resultType = resultType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
