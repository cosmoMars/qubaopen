package com.qubaopen.survey.entity.interest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author mars 兴趣问卷结果选项表
 */
@Entity
@Table(name = "interest_result_option")
@Audited
public class InterestResultOption extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 2900030333236440728L;

	/**
	 * 内容
	 */
	@Column(columnDefinition = "LONGTEXT")
	private String content;

	/**
	 * 结果
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "interest_result_id")
	private InterestResult interestResult;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 结果题号
	 */
	private String resultNum;

	/**
	 * 最低分数
	 */
	private Integer lowestScore;

	/**
	 * 最高分数
	 */
	private Integer highestScore;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public InterestResult getInterestResult() {
		return interestResult;
	}

	public void setInterestResult(InterestResult interestResult) {
		this.interestResult = interestResult;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getResultNum() {
		return resultNum;
	}

	public void setResultNum(String resultNum) {
		this.resultNum = resultNum;
	}

	public Integer getLowestScore() {
		return lowestScore;
	}

	public void setLowestScore(Integer lowestScore) {
		this.lowestScore = lowestScore;
	}

	public Integer getHighestScore() {
		return highestScore;
	}

	public void setHighestScore(Integer highestScore) {
		this.highestScore = highestScore;
	}

}
