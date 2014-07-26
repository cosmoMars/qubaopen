package com.qubaopen.survey.entity.self;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author mars 自测问卷特殊题插入顺序表
 */
@Entity
@Table(name = "self_special_insert")
@Audited
public class SelfSpecialInsert extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 3040012663613377213L;

	/**
	 * 兴趣问卷
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "self_id")
	private Self self;

	/**
	 * 上一题
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "self_question_id")
	private SelfQuestion selfQuestion;

	/**
	 * 该选项被选择后跳转特殊题
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "self_question_option_id")
	private SelfQuestionOption selfQuestionOption;

	/**
	 * 特殊题
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "special_question_id")
	private SelfQuestion specialQuestion;

	public Self getSelf() {
		return self;
	}

	public void setSelf(Self self) {
		this.self = self;
	}

	public SelfQuestion getSelfQuestion() {
		return selfQuestion;
	}

	public void setSelfQuestion(SelfQuestion selfQuestion) {
		this.selfQuestion = selfQuestion;
	}

	public SelfQuestionOption getSelfQuestionOption() {
		return selfQuestionOption;
	}

	public void setSelfQuestionOption(SelfQuestionOption selfQuestionOption) {
		this.selfQuestionOption = selfQuestionOption;
	}

	public SelfQuestion getSpecialQuestion() {
		return specialQuestion;
	}

	public void setSpecialQuestion(SelfQuestion specialQuestion) {
		this.specialQuestion = specialQuestion;
	}

}
