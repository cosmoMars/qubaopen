package com.qubaopen.survey.entity.vo;

import java.io.Serializable;

public class QuestionVo implements Serializable {

	private static final long serialVersionUID = 4746105088379922093L;

	/**
	 * 问题
	 */
	private long questionId;

//	/**
//	 * 选择答案
//	 */
//	private long[] choiceIds;

	/**
	 * 内容
	 */
	private String[] content;

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public String[] getContent() {
		return content;
	}

	public void setContent(String[] content) {
		this.content = content;
	}


//	/**
//	 * 排序题答案
//	 */
//	private long[] orderIds;

}
