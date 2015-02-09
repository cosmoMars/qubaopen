package com.qubaopen.survey.entity.user;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qubaopen.core.entity.AbstractBaseEntity;

/**
 * @author mars 用户反馈表
 */
@Entity
@Table(name = "user_feed_back")
@Audited
public class UserFeedBack extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -4149843099074806989L;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 页面标签
	 */
	private String title;

	/**
	 * 反馈时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date feedBackTime;

	/**
	 * 联系方式 phone/email
	 */
	private String contactMethod;

	/**
	 * 用户id
	 */
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id")
	private User user;

	/**
	 * 意见反馈的用户类型 0 是默认普通用户 1是企业用户
	 */
	@Enumerated
	private FeedBackType feedBackType;

	/**
	 * ORDINARY 0 普通用户, ENTERPRISE 1 企业用户
	 */
	public enum FeedBackType {
		ORDINARY, ENTERPRISE
	}

	@Enumerated
	private Type type;

	private enum Type {
		Good, Evaluate
	}

	/**
	 * 反馈类型
	 */
	@ManyToMany
	@JoinTable(name = "back_type_relation", joinColumns = @JoinColumn(name = "back_id"), inverseJoinColumns = @JoinColumn(name = "type_id"))
	private Set<UserFeedBackType> backTypes;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getFeedBackTime() {
		return feedBackTime;
	}

	public void setFeedBackTime(Date feedBackTime) {
		this.feedBackTime = feedBackTime;
	}

	public String getContactMethod() {
		return contactMethod;
	}

	public void setContactMethod(String contactMethod) {
		this.contactMethod = contactMethod;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public FeedBackType getFeedBackType() {
		return feedBackType;
	}

	public void setFeedBackType(FeedBackType feedBackType) {
		this.feedBackType = feedBackType;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Set<UserFeedBackType> getBackTypes() {
		return backTypes;
	}

	public void setBackTypes(Set<UserFeedBackType> backTypes) {
		this.backTypes = backTypes;
	}

}
