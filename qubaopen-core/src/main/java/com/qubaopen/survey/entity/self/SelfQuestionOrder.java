package com.qubaopen.survey.entity.self;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author mars 自测问题顺序
 */
@Entity
@Table(name = "self_question_order")
@Audited
public class SelfQuestionOrder extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -2921365144134328167L;

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