package com.qubaopen.survey.entity.self;

import javax.persistence.Column;
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

	private static final long serialVersionUID = 5978096878117787206L;

	/**
	 * 结果名称
	 */
	private String name;

	/**
	 * 内容
	 */
	@Column(columnDefinition = "TEXT")
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
	private Integer lowestScore;

	/**
	 * 最高分数
	 */
	private Integer highestScore;
	
	/**
	 * 图片地址
	 */
	private String picPath;

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

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

}
