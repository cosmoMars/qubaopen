package com.qubaopen.survey.entity.self;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author mars 自测问卷结果选项表
 */
@Entity
@Table(name = "self_result_option")
@Audited
public class SelfResultOption extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 5574360831856505640L;

	/**
	 * 结果名称
	 */
	private String name;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 结果
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "self_result_id")
	private SelfResult selfResult;

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
	private int lowestScore;

	/**
	 * 最高分数
	 */
	private int highestScore;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public SelfResult getSelfResult() {
		return selfResult;
	}

	public void setSelfResult(SelfResult selfResult) {
		this.selfResult = selfResult;
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

	public int getLowestScore() {
		return lowestScore;
	}

	public void setLowestScore(int lowestScore) {
		this.lowestScore = lowestScore;
	}

	public int getHighestScore() {
		return highestScore;
	}

	public void setHighestScore(int highestScore) {
		this.highestScore = highestScore;
	}

}
