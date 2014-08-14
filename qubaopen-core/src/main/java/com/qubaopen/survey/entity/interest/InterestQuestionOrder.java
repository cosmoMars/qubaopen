package com.qubaopen.survey.entity.interest;

import javax.persistence.Entity;
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

	private static final long serialVersionUID = -5275365571762144278L;

	/**
	 * 问题
	 */
	private long questionId;

	/**
	 * 本题选项结果
	 */
	private long optionId;

	/**
	 * 下一题
	 */
	private long nextQuestionId;

	/**
	 * 是否跳过
	 */
	private boolean jump;

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public long getOptionId() {
		return optionId;
	}

	public void setOptionId(long optionId) {
		this.optionId = optionId;
	}

	public long getNextQuestionId() {
		return nextQuestionId;
	}

	public void setNextQuestionId(long nextQuestionId) {
		this.nextQuestionId = nextQuestionId;
	}

	public boolean isJump() {
		return jump;
	}

	public void setJump(boolean jump) {
		this.jump = jump;
	}

}
