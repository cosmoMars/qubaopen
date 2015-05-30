package com.qubaopen.survey.entity.self;

import com.qubaopen.survey.entity.user.User;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.Date;

/**
 * @author mars 兴趣问卷用户答卷
 */
@Entity
@Table(name = "self_user_questionnaire")
@Audited
public class SelfUserQuestionnaire extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 6653201418514687605L;

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
	@JoinColumn(name = "self_id")
	private Self self;

	/**
	 * 问卷结果选项
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "self_result_option_id")
	private SelfResultOption selfResultOption;

	/**
	 * 问卷结果选项
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "self_question_type_id")
	private SelfQuestionType selfQuestionType;
	
	/**
	 * 分数
	 */
	private int score;

	/**
	 * 时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date time;
	
	/**
	 * 使用
	 */
	private boolean used;

	/**
	 * 新浪分享
	 */
	private boolean sharedSina;

	/**
	 * 腾讯分享
	 */
	private boolean sharedTencent;

	/**
	 * 微信朋友圈分享
	 */
	private boolean sharedWeChatFriend;

	/**
	 * QQ空间分享
	 */
	@Column(name = "shared_qq_space", nullable = false)
	private boolean sharedQQSpace;

	/**
	 * 微信分享
	 */
	private boolean sharedWeChat;

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
	private Boolean publicToAll;

	/**
	 * 准确
	 */
	private Boolean accuracy;

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

	public Self getSelf() {
		return self;
	}

	public void setSelf(Self self) {
		this.self = self;
	}

	public SelfResultOption getSelfResultOption() {
		return selfResultOption;
	}

	public void setSelfResultOption(SelfResultOption selfResultOption) {
		this.selfResultOption = selfResultOption;
	}

	public SelfQuestionType getSelfQuestionType() {
		return selfQuestionType;
	}

	public void setSelfQuestionType(SelfQuestionType selfQuestionType) {
		this.selfQuestionType = selfQuestionType;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	public boolean isSharedSina() {
		return sharedSina;
	}

	public void setSharedSina(boolean sharedSina) {
		this.sharedSina = sharedSina;
	}

	public boolean isSharedTencent() {
		return sharedTencent;
	}

	public void setSharedTencent(boolean sharedTencent) {
		this.sharedTencent = sharedTencent;
	}

	public boolean isSharedWeChatFriend() {
		return sharedWeChatFriend;
	}

	public void setSharedWeChatFriend(boolean sharedWeChatFriend) {
		this.sharedWeChatFriend = sharedWeChatFriend;
	}

	public boolean isSharedQQSpace() {
		return sharedQQSpace;
	}

	public void setSharedQQSpace(boolean sharedQQSpace) {
		this.sharedQQSpace = sharedQQSpace;
	}

	public boolean isSharedWeChat() {
		return sharedWeChat;
	}

	public void setSharedWeChat(boolean sharedWeChat) {
		this.sharedWeChat = sharedWeChat;
	}

	public Transmit getTransmit() {
		return transmit;
	}

	public void setTransmit(Transmit transmit) {
		this.transmit = transmit;
	}

	public Boolean getPublicToAll() {
		return publicToAll;
	}

	public void setPublicToAll(Boolean publicToAll) {
		this.publicToAll = publicToAll;
	}

	public Boolean getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(Boolean accuracy) {
		this.accuracy = accuracy;
	}
}
