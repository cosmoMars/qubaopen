package com.qubaopen.survey.entity.user;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qubaopen.core.entity.AbstractBaseEntity2;

/**
 * @author mars 用户信息表
 */
@Entity
@Table(name = "user_info")
@Audited
public class UserInfo extends AbstractBaseEntity2<Long> {

	private static final long serialVersionUID = -6507205012887757887L;

	/**
	 * 用户信息
	 */
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@PrimaryKeyJoinColumn
	private User user;

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 昵称
	 */
	private String nickName;

	/**
	 * 性别
	 */
	@Enumerated
	private Sex sex;

	private enum Sex {
		MALE, FEMALE, OTHER
	}

	/**
	 * 出生时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date birthday;

	/**
	 * 血型 A, B, AB, O, OTHER
	 */
	@Enumerated
	private BloodType bloodType;

	/**
	 * A, B, AB, O, OTHER
	 */
	private enum BloodType {
		A, B, O, AB, OTHER
	}

	/**
	 * 头像
	 */
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private byte[] avatar;

	/**
	 * 个性签名
	 */
	private String signature;

	/**
	 * 新浪微博软件分享
	 */
	private boolean sharedSina;

	/**
	 * 腾讯微博软件分享
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
	 * 好友数量
	 */
	private int friendNum;

	/**
	 * 向萌主公开测试答案
	 */
	private boolean publicAnswersToChief;

	/**
	 * 开启省流量模式
	 */
	private boolean saveFlow;

	/**
	 * 向好友公开最新动态
	 */
	private boolean publicMovementToFriend;

	/**
	 * 向好友公开测试答案
	 */
	private boolean publicAnswersToFriend;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public BloodType getBloodType() {
		return bloodType;
	}

	public void setBloodType(BloodType bloodType) {
		this.bloodType = bloodType;
	}

	public byte[] getAvatar() {
		return avatar;
	}

	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
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

	public int getFriendNum() {
		return friendNum;
	}

	public void setFriendNum(int friendNum) {
		this.friendNum = friendNum;
	}

	public boolean isPublicAnswersToChief() {
		return publicAnswersToChief;
	}

	public void setPublicAnswersToChief(boolean publicAnswersToChief) {
		this.publicAnswersToChief = publicAnswersToChief;
	}

	public boolean isSaveFlow() {
		return saveFlow;
	}

	public void setSaveFlow(boolean saveFlow) {
		this.saveFlow = saveFlow;
	}

	public boolean isPublicMovementToFriend() {
		return publicMovementToFriend;
	}

	public void setPublicMovementToFriend(boolean publicMovementToFriend) {
		this.publicMovementToFriend = publicMovementToFriend;
	}

	public boolean isPublicAnswersToFriend() {
		return publicAnswersToFriend;
	}

	public void setPublicAnswersToFriend(boolean publicAnswersToFriend) {
		this.publicAnswersToFriend = publicAnswersToFriend;
	}

}
