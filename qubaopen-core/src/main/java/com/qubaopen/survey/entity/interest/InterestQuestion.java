package com.qubaopen.survey.entity.interest;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author mars 兴趣问卷问题表
 */
@Entity
@Table(name = "interest_question")
@Audited
public class InterestQuestion extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 1224693058157355984L;

	/**
	 * 问卷id
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "interest_id")
	private Interest interest;

//	/**
//	 * 问卷类型
//	 */
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "interest_question_type_id")
//	private InterestQuestionType interestQuestionType;

	/**
	 * 问卷类型  单选 SINGLE, 多选 MULTIPLE, 问答 QA, 排序 SORT, 打分 SCORE
	 */
	@Enumerated
	private Type type;

	private enum Type {
		SINGLE, MULTIPLE, QA, SORT, SCORE, MULQA
	}

	/**
	 * 选项数量
	 */
	private int optionCount;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 题号
	 */
	private int questionNum;

	/**
	 * 是否性格特殊问题
	 */
	private boolean special;

	/**
	 * 答题时间限制
	 */
	private Integer answerTimeLimit = 1;

	private String picPath;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "interestQuestion", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
	private Set<InterestQuestionOption> interestQuestionOptions;

	/**
	 * 问题顺序 x:y:z|x:y:z x本题id，y选项id y＝0时，直接跳转z z下一题id，z＝0时，结束问卷
	 */
	private String qOrder;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private InterestQuestion parent;

	/**
	 * 孩纸
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
	private Set<InterestQuestion> children;

	public Interest getInterest() {
		return interest;
	}

	public void setInterest(Interest interest) {
		this.interest = interest;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public int getOptionCount() {
		return optionCount;
	}

	public void setOptionCount(int optionCount) {
		this.optionCount = optionCount;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getQuestionNum() {
		return questionNum;
	}

	public void setQuestionNum(int questionNum) {
		this.questionNum = questionNum;
	}

	public boolean isSpecial() {
		return special;
	}

	public void setSpecial(boolean special) {
		this.special = special;
	}

	public Integer getAnswerTimeLimit() {
		return answerTimeLimit;
	}

	public void setAnswerTimeLimit(Integer answerTimeLimit) {
		this.answerTimeLimit = answerTimeLimit;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public Set<InterestQuestionOption> getInterestQuestionOptions() {
		return interestQuestionOptions;
	}

	public void setInterestQuestionOptions(Set<InterestQuestionOption> interestQuestionOptions) {
		this.interestQuestionOptions = interestQuestionOptions;
	}

	public String getqOrder() {
		return qOrder;
	}

	public void setqOrder(String qOrder) {
		this.qOrder = qOrder;
	}

	public InterestQuestion getParent() {
		return parent;
	}

	public void setParent(InterestQuestion parent) {
		this.parent = parent;
	}

	public Set<InterestQuestion> getChildren() {
		return children;
	}

	public void setChildren(Set<InterestQuestion> children) {
		this.children = children;
	}

}
