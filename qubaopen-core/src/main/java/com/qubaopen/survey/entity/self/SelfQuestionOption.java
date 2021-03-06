package com.qubaopen.survey.entity.self;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author mars 自测问题选项表
 */
@Entity
@Table(name = "self_question_option")
@Audited
public class SelfQuestionOption extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 1074121001257283241L;

	/**
	 * 问卷id
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "self_question_id")
	private SelfQuestion selfQuestion;

	/**
	 * 问题类型
	 */
	@ManyToOne
	@JoinColumn(name = "self_question_option_type_id")
	private SelfQuestionOptionType selfQuestionOptionType;

//	/**
//	 * 类型题的答案
//	 */
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "self_result_option_id")
//	private SelfResultOption selfResultOption;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 分数
	 */
	private int score;

	/**
	 * 选项号
	 */
	private int optionNum;

	private String picPath;

	public SelfQuestion getSelfQuestion() {
		return selfQuestion;
	}

	public void setSelfQuestion(SelfQuestion selfQuestion) {
		this.selfQuestion = selfQuestion;
	}

	public SelfQuestionOptionType getSelfQuestionOptionType() {
		return selfQuestionOptionType;
	}

	public void setSelfQuestionOptionType(SelfQuestionOptionType selfQuestionOptionType) {
		this.selfQuestionOptionType = selfQuestionOptionType;
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
