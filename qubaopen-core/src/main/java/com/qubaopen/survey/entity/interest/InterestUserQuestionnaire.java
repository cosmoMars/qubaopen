package com.qubaopen.survey.entity.interest;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

import com.qubaopen.survey.entity.user.User;

/**
 * @author mars 兴趣问卷用户答卷
 */
@Entity
@Table(name = "interest_user_questionnaire")
@Audited
public class InterestUserQuestionnaire extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -5171099225306703597L;

	/**
	 * 用户
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	/**
	 * 用户答卷状态 UNCOMPLETED 兴趣问卷未完成, COMPLETED 兴趣问卷已完成
	 */
	@Enumerated
	private Status status;

	private enum Status {
		UNCOMPLETED, COMPLETED
	}

	/**
	 * 问卷
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "interest_id")
	private Interest interest;

	/**
	 * 问卷结果选项
	 */
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "interest_result_option_id")
	private InterestResultOption interestResultOption;

	/**
	 * 时间
	 */
	private Date time;

	/**
	 * 新浪分享
	 */
	private boolean isSharedSina;

	/**
	 * 腾讯分享
	 */
	private boolean isSharedTencent;

	/**
	 * 微信朋友圈分享
	 */
	private boolean isSharedWeChatFriend;

	/**
	 * QQ空间分享
	 */
	@Column(name = "is_shared_qq_space")
	private boolean isSharedQQSpace;

	/**
	 * 微信分享
	 */
	private boolean isSharedWeChat;

	/**
	 * 用户历史问卷，同步发送标志位 0 未发送 1发送中 2 已发送
	 */
	@Enumerated
	private Transmit transmit;

	/**
	 * 用户历史问卷，同步发送标志位 NOTRANSMIT 0 未发送,TRANSMITTING 1 发送中,TRANSMITTED 2 已发送
	 */
	private enum Transmit {
		NOTRANSMIT, TRANSMITTING, TRANSMITTED
	}

	/**
	 * 是否公开 0 不公开 1 公开 默认为空
	 */
	private Boolean isPublic;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Interest getInterest() {
		return interest;
	}

	public void setInterest(Interest interest) {
		this.interest = interest;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Transmit getTransmit() {
		return transmit;
	}

	public void setTransmit(Transmit transmit) {
		this.transmit = transmit;
	}

	public InterestResultOption getInterestResultOption() {
		return interestResultOption;
	}

	public void setInterestResultOption(InterestResultOption interestResultOption) {
		this.interestResultOption = interestResultOption;
	}

	public boolean isSharedSina() {
		return isSharedSina;
	}

	public void setSharedSina(boolean isSharedSina) {
		this.isSharedSina = isSharedSina;
	}

	public boolean isSharedTencent() {
		return isSharedTencent;
	}

	public void setSharedTencent(boolean isSharedTencent) {
		this.isSharedTencent = isSharedTencent;
	}

	public boolean isSharedWeChatFriend() {
		return isSharedWeChatFriend;
	}

	public void setSharedWeChatFriend(boolean isSharedWeChatFriend) {
		this.isSharedWeChatFriend = isSharedWeChatFriend;
	}

	public boolean isSharedQQSpace() {
		return isSharedQQSpace;
	}

	public void setSharedQQSpace(boolean isSharedQQSpace) {
		this.isSharedQQSpace = isSharedQQSpace;
	}

	public boolean isSharedWeChat() {
		return isSharedWeChat;
	}

	public void setSharedWeChat(boolean isSharedWeChat) {
		this.isSharedWeChat = isSharedWeChat;
	}

	public Boolean isPublic() {
		return isPublic;
	}

	public void setPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}

}
