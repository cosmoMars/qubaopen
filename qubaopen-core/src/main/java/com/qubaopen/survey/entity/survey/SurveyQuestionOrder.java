package com.qubaopen.survey.entity.survey;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * 顺序表
 * @author mars
 *
 */
@Entity
@Table(name = "survey_question_order")
@Audited
public class SurveyQuestionOrder extends AbstractPersistable<Long>{

	private static final long serialVersionUID = -1463252367505146017L;

	/**
	 * 本题
	 */
	private long questionId;

	/**
	 * 选项
	 */
	private long optionId;

	/**
	 * 下一题
	 */
	private long nextQuestionId;

	/**
	 * 中断
	 */
	private boolean interrupt;

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

	public boolean isInterrupt() {
		return interrupt;
	}

	public void setInterrupt(boolean interrupt) {
		this.interrupt = interrupt;
	}

}
