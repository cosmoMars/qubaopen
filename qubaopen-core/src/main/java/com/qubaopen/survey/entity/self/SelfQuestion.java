package com.qubaopen.survey.entity.self;

import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
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
@Table(name = "self_question")
@Audited
public class SelfQuestion extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -8135012000102117725L;

	/**
	 * 问卷id
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "self_id")
	private Self self;

	/**
	 * 问卷类型
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "self_question_type_id")
	private SelfQuestionType selfQuestionType;

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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "selfQuestion", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
	private Set<SelfQuestionOption> selfQuestionOptions;

	public Self getSelf() {
		return self;
	}

	public void setSelf(Self self) {
		this.self = self;
	}

	public SelfQuestionType getSelfQuestionType() {
		return selfQuestionType;
	}

	public void setSelfQuestionType(SelfQuestionType selfQuestionType) {
		this.selfQuestionType = selfQuestionType;
	}

	public Integer getOptionCount() {
		return optionCount;
	}

	public void setOptionCount(Integer optionCount) {
		this.optionCount = optionCount;
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

	public boolean isSpecial() {
		return isSpecial;
	}

	public void setSpecial(boolean isSpecial) {
		this.isSpecial = isSpecial;
	}

	public Integer getAnswerTimeLimit() {
		return answerTimeLimit;
	}

	public void setAnswerTimeLimit(Integer answerTimeLimit) {
		this.answerTimeLimit = answerTimeLimit;
	}

	public byte[] getPic() {
		return pic;
	}

	public void setPic(byte[] pic) {
		this.pic = pic;
	}

	public Set<SelfQuestionOption> getSelfQuestionOptions() {
		return selfQuestionOptions;
	}

	public void setSelfQuestionOptions(Set<SelfQuestionOption> selfQuestionOptions) {
		this.selfQuestionOptions = selfQuestionOptions;
	}

}
