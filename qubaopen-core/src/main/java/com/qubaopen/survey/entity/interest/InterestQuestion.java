package com.qubaopen.survey.entity.interest;

import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author mars 兴趣问卷问题表
 */
@Entity
@Table(name = "interest_question")
@Audited
public class InterestQuestion extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -275996218159783931L;

	/**
	 * 问卷id
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "interest_id")
	private Interest interest;

	/**
	 * 问卷类型
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "interest_question_type_id")
	private InterestQuestionType interestQuestionType;

	/**
	 * 问卷类型  单选 SINGLE, 多选 MULTIPLE, 问答 QA, 排序 SORT, 打分 SCORE
	 */
	@Enumerated
	private Type type;

	private enum Type {
		SINGLE, MULTIPLE, QA, SORT, SCORE
	}


	/**
	 * 选项数量
	 */
	private Integer optionCount;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 题号
	 */
	private Integer questionNum;

	/**
	 * 是否性格特殊问题
	 */
	private boolean isSpecial;

	/**
	 * 答题时间限制
	 */
	private Integer answerTimeLimit;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private byte[] pic;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "interestQuestion", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
	private Set<InterestQuestionOption> interestQuestionOptions;

	public Interest getInterest() {
		return interest;
	}

	public void setInterest(Interest interest) {
		this.interest = interest;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getQuestionNum() {
		return questionNum;
	}

	public void setQuestionNum(Integer questionNum) {
		this.questionNum = questionNum;
	}

	public InterestQuestionType getInterestQuestionType() {
		return interestQuestionType;
	}

	public void setInterestQuestionType(InterestQuestionType interestQuestionType) {
		this.interestQuestionType = interestQuestionType;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Integer getOptionCount() {
		return optionCount;
	}

	public void setOptionCount(Integer optionCount) {
		this.optionCount = optionCount;
	}

	public Integer getAnswerTimeLimit() {
		return answerTimeLimit;
	}

	public void setAnswerTimeLimit(Integer answerTimeLimit) {
		this.answerTimeLimit = answerTimeLimit;
	}

	public boolean isSpecial() {
		return isSpecial;
	}

	public void setSpecial(boolean isSpecial) {
		this.isSpecial = isSpecial;
	}
	public byte[] getPic() {
		return pic;
	}

	public void setPic(byte[] pic) {
		this.pic = pic;
	}

	public Set<InterestQuestionOption> getInterestQuestionOptions() {
		return interestQuestionOptions;
	}

	public void setInterestQuestionOptions(Set<InterestQuestionOption> interestQuestionOptions) {
		this.interestQuestionOptions = interestQuestionOptions;
	}
}
