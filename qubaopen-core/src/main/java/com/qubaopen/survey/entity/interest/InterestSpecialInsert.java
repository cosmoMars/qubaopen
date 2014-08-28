package com.qubaopen.survey.entity.interest;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author mars 兴趣问卷特殊题插入顺序表
 */
@Entity
@Table(name = "interest_special_insert")
@Audited
public class InterestSpecialInsert extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -1228043455204471370L;

	/**
	 * 兴趣问卷
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "interest_id")
	private Interest interest;

	/**
	 * 上一题
	 */
	private long questionId;

	/**
	 * 该选项被选择后跳转特殊题
	 */
	private long questionOptionId;

	/**
	 * 特殊题
	 */
	private long specialQuestionId;

	public Interest getInterest() {
		return interest;
	}

	public void setInterest(Interest interest) {
		this.interest = interest;
	}

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public long getQuestionOptionId() {
		return questionOptionId;
	}

	public void setQuestionOptionId(long questionOptionId) {
		this.questionOptionId = questionOptionId;
	}

	public long getSpecialQuestionId() {
		return specialQuestionId;
	}

	public void setSpecialQuestionId(long specialQuestionId) {
		this.specialQuestionId = specialQuestionId;
	}

}
