package com.qubaopen.survey.entity.self;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author mars 自测问题选项表
 */
@Entity
@Table(name = "self_question_option")
@Audited
public class SelfQuestionOption extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -649468427522404510L;

	/**
	 * 问卷id
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "self_question_id")
	private SelfQuestion selfQuestion;

	/**
	 * 类型题的答案
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "self_result_option_id")
	private SelfResultOption selfResultOption;

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
	private String questionNum;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private byte[] pic;

	public SelfQuestion getSelfQuestion() {
		return selfQuestion;
	}

	public void setSelfQuestion(SelfQuestion selfQuestion) {
		this.selfQuestion = selfQuestion;
	}

	public SelfResultOption getSelfResultOption() {
		return selfResultOption;
	}

	public void setSelfResultOption(SelfResultOption selfResultOption) {
		this.selfResultOption = selfResultOption;
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

	public String getQuestionNum() {
		return questionNum;
	}

	public void setQuestionNum(String questionNum) {
		this.questionNum = questionNum;
	}

	public byte[] getPic() {
		return pic;
	}

	public void setPic(byte[] pic) {
		this.pic = pic;
	}

}
