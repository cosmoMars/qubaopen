package com.qubaopen.survey.entity.interest;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author mars 兴趣问卷结果表
 */
@Entity
@Table(name = "interest_result")
@Audited
public class InterestResult extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 692329675843837712L;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "interest_id")
	private Interest interest;

//	/**
//	 * 问卷类型
//	 */
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "interest_result_type_id")
//	private InterestResultType interestResultType;

	/**
	 * 问卷类型
	 */
	@Enumerated
	private ResultType resultType;

	private enum ResultType {
		OREDER, POINTS
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
