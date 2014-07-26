package com.qubaopen.survey.entity.self;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

import com.qubaopen.survey.entity.user.User;

/**
 * @author mars 自测问卷用户答题表
 */
@Entity
@Table(name = "self_user_answer")
@Audited
public class SelfUserAnswer extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -3316101548331370909L;

	/**
	 * 用户
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	/**
	 * 兴趣问卷用户答卷
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "self_user_questionnaire_id")
	private SelfUserQuestionnaire selfUserQuestionnaire;

	/**
	 * 所答的选项
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "self_question_option_id")
	private SelfQuestionOption selfQuestionOption;

	/**
	 * 用户答卷的问题
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "self_question_id")
	private SelfQuestion selfQuestion;

	/**
	 * 问答题内容
	 */
	private String content;

	/**
	 * 顺序
	 */
	private Integer turn;

	/**
	 * 分数
	 */
	private Integer score;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public SelfUserQuestionnaire getSelfUserQuestionnaire() {
		return selfUserQuestionnaire;
	}

	public void setSelfUserQuestionnaire(SelfUserQuestionnaire selfUserQuestionnaire) {
		this.selfUserQuestionnaire = selfUserQuestionnaire;
	}

	public SelfQuestionOption getSelfQuestionOption() {
		return selfQuestionOption;
	}

	public void setSelfQuestionOption(SelfQuestionOption selfQuestionOption) {
		this.selfQuestionOption = selfQuestionOption;
	}

	public SelfQuestion getSelfQuestion() {
		return selfQuestion;
	}

	public void setSelfQuestion(SelfQuestion selfQuestion) {
		this.selfQuestion = selfQuestion;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getTurn() {
		return turn;
	}

	public void setTurn(Integer turn) {
		this.turn = turn;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

}
