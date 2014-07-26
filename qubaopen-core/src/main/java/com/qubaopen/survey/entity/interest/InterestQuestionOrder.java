package com.qubaopen.survey.entity.interest;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author mars 兴趣问卷问题顺序
 */
@Entity
@Table(name = "interest_question_order")
@Audited
public class InterestQuestionOrder extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 2218196538615957102L;

	/**
	 * 问题id
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "interest_question_id")
	private InterestQuestion interestQuestion;

	/**
	 * 题号
	 */
	private String questionNum;

	/**
	 * 该选项的选项号
	 */
	private String optionNum;

	/**
	 * 下题的题号
	 */
	private String nextOptionNum;

	/**
	 * 是否跳过
	 */
	private boolean isJump;

	public String getQuestionNum() {
		return questionNum;
	}

	public void setQuestionNum(String questionNum) {
		this.questionNum = questionNum;
	}

	public String getOptionNum() {
		return optionNum;
	}

	public void setOptionNum(String optionNum) {
		this.optionNum = optionNum;
	}

	public String getNextOptionNum() {
		return nextOptionNum;
	}

	public void setNextOptionNum(String nextOptionNum) {
		this.nextOptionNum = nextOptionNum;
	}

	public InterestQuestion getInterestQuestion() {
		return interestQuestion;
	}

	public void setInterestQuestion(InterestQuestion interestQuestion) {
		this.interestQuestion = interestQuestion;
	}

	public boolean isJump() {
		return isJump;
	}

	public void setJump(boolean isJump) {
		this.isJump = isJump;
	}

}
