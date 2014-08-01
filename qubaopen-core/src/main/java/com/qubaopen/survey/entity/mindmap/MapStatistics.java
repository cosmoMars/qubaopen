package com.qubaopen.survey.entity.mindmap;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.user.User;

/**
 * 心理地图统计
 *
 * @author mars
 *
 */
@Entity
@Table(name = "map_statistics")
public class MapStatistics extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -3138111352912710215L;

	/**
	 * 用户
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	/**
	 * 问卷 调研，自测，兴趣
	 */
	private long questionnaireId;

	/**
	 * 图形结果
	 */
	private String result;

	/**
	 * 单个结果
	 */
	private long resultId;

	/**
	 * INTEREST, SURVEY, SELF
	 */
	private Type type;

	private enum Type {
		INTEREST, SURVEY, SELF
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public long getQuestionnaireId() {
		return questionnaireId;
	}

	public void setQuestionnaireId(long questionnaireId) {
		this.questionnaireId = questionnaireId;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public long getResultId() {
		return resultId;
	}

	public void setResultId(long resultId) {
		this.resultId = resultId;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

}
