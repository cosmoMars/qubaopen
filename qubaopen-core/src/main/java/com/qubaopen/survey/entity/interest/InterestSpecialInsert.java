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
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "interest_question_id")
	private InterestQuestion interestQuestion;

	/**
	 * 该选项被选择后跳转特殊题
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "interest_question_option_id")
	private InterestQuestionOption interestQuestionOption;

	/**
	 * 特殊题
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "special_question_id")
	private InterestQuestion specialQuestion;

	public InterestQuestion getInterestQuestion() {
		return interestQuestion;
	}

	public void setInterestQuestion(InterestQuestion interestQuestion) {
		this.interestQuestion = interestQuestion;
	}

	public InterestQuestion getSpecialQuestion() {
		return specialQuestion;
	}

	public void setSpecialQuestion(InterestQuestion specialQuestion) {
		this.specialQuestion = specialQuestion;
	}

	public Interest getInterest() {
		return interest;
	}

	public void setInterest(Interest interest) {
		this.interest = interest;
	}

	public InterestQuestionOption getInterestQuestionOption() {
		return interestQuestionOption;
	}

	public void setInterestQuestionOption(InterestQuestionOption interestQuestionOption) {
		this.interestQuestionOption = interestQuestionOption;
	}

}
