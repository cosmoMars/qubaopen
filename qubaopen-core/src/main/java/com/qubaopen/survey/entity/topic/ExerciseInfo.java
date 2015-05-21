package com.qubaopen.survey.entity.topic;

import com.qubaopen.core.entity.AbstractBaseEntity;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Table(name = "exercise_info")
@Audited
public class ExerciseInfo extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -8853353244170272247L;

	/**
	 * 练习主题
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Exercise exercise;

	/**
	 * 练习名称
	 */
	private String name;

	/**
	 * 练习内容
	 */
	@Column(columnDefinition = "TEXT")
	private String content;

	/**
	 * 顺序
	 */
	private String number;

	/**
	 * 图片url
	 */
	private String picUrl;

	/**
	 * 开头语
	 */
	private String openingSentence;

	/**
	 * 结束语
	 */
	private String endingSentence;

	public Exercise getExercise() {
		return exercise;
	}

	public void setExercise(Exercise exercise) {
		this.exercise = exercise;
	}

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

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getOpeningSentence() {
		return openingSentence;
	}

	public void setOpeningSentence(String openingSentence) {
		this.openingSentence = openingSentence;
	}

	public String getEndingSentence() {
		return endingSentence;
	}

	public void setEndingSentence(String endingSentence) {
		this.endingSentence = endingSentence;
	}
}
