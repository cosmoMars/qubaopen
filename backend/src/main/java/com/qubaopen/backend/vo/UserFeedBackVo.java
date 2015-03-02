package com.qubaopen.backend.vo;

import java.io.Serializable;

public class UserFeedBackVo implements Serializable {

	private static final long serialVersionUID = -5162437719768870231L;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 反馈时间
	 */
	private String feedBackTime;

	/**
	 * 联系方式 phone/email
	 */
	private String contactMethod;

	/**
	 * 用户id
	 */
	private long userId;

	/**
	 * 用户类型
	 */
	private String feedBackType;
	
	private String createDate;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFeedBackTime() {
		return feedBackTime;
	}

	public void setFeedBackTime(String feedBackTime) {
		this.feedBackTime = feedBackTime;
	}

	public String getContactMethod() {
		return contactMethod;
	}

	public void setContactMethod(String contactMethod) {
		this.contactMethod = contactMethod;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getFeedBackType() {
		return feedBackType;
	}

	public void setFeedBackType(String feedBackType) {
		this.feedBackType = feedBackType;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

}
