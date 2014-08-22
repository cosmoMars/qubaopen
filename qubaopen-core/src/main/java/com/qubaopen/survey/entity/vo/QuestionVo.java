package com.qubaopen.survey.entity.vo;

import java.io.Serializable;
import java.util.Set;

public class QuestionVo implements Serializable {

	private static final long serialVersionUID = 4746105088379922093L;

	/**
	 * 问题
	 */
	private long questionId;

	/**
	 * 内容
	 */
	private Set<Content> contents;

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public Set<Content> getContents() {
		return contents;
	}

	public void setContents(Set<Content> contents) {
		this.contents = contents;
	}

}
