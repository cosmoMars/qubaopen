package com.qubaopen.survey.entity.self;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

	private static final long serialVersionUID = 7097435449332156303L;

	/**
	 * 问题id
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "self_question_id")
	private SelfQuestion selfQuestion;

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
	private boolean jump;

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

	public SelfQuestion getSelfQuestion() {
		return selfQuestion;
	}

	public void setSelfQuestion(SelfQuestion selfQuestion) {
		this.selfQuestion = selfQuestion;
	}

	public boolean isJump() {
		return jump;
	}

	public void setJump(boolean jump) {
		this.jump = jump;
	}

}