package com.qubaopen.survey.entity.topic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;

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

}
