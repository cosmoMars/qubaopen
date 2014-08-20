package com.qubaopen.survey.entity.interest;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author mars 兴趣问卷问题选项表
 */
@Entity
@Table(name = "interest_question_option")
@Audited
public class InterestQuestionOption extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 4683285061937405102L;

	/**
	 * 问卷id
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "interest_question_id")
	private InterestQuestion interestQuestion;

	/**
	 * 问卷id
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "interest_question_option_type_id")
	private InterestQuestionOptionType interestQuestionOptionType;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 分数
	 */
	private int score;

	/**
	 * 题号
	 */
	private int optionNum;

	/**
	 * 图片
	 */
	private String picPath;

	public InterestQuestion getInterestQuestion() {
		return interestQuestion;
	}

	public void setInterestQuestion(InterestQuestion interestQuestion) {
		this.interestQuestion = interestQuestion;
	}

	public InterestQuestionOptionType getInterestQuestionOptionType() {
		return interestQuestionOptionType;
	}

	public void setInterestQuestionOptionType(InterestQuestionOptionType interestQuestionOptionType) {
		this.interestQuestionOptionType = interestQuestionOptionType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getOptionNum() {
		return optionNum;
	}

	public void setOptionNum(int optionNum) {
		this.optionNum = optionNum;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

}
