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

import org.hibernate.envers.Audited;

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
	private Date birthday;

	/**
	 * 血型
	 */
	@Enumerated
	private BloodType bloodType;

	/**
	 * A, B, AB, O
	 *
	 */
	private enum BloodType {
		A, B, AB, O
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
	private boolean isSharedSina;

	/**
	 * 腾讯微博软件分享
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
	 * 好友数量
	 */
	private int friendNum;

	/**
	 * 向萌主公开测试答案
	 */
	private boolean isPublicAnswersToChief;

	/**
	 * 开启省流量模式
	 */
	private boolean isSaveFlow;

	/**
	 * 向好友公开最新动态
	 */
	private boolean isPublicMovementToFriend;

	/**
	 * 向好友公开测试答案
	 */
	private boolean isPublicAnswersToFriend;

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

	public int getFriendNum() {
		return friendNum;
	}

	public void setFriendNum(int friendNum) {
		this.friendNum = friendNum;
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

	public boolean isPublicAnswersToChief() {
		return isPublicAnswersToChief;
	}

	public void setPublicAnswersToChief(boolean isPublicAnswersToChief) {
		this.isPublicAnswersToChief = isPublicAnswersToChief;
	}

	public boolean isPublicMovementToFriend() {
		return isPublicMovementToFriend;
	}

	public void setPublicMovementToFriend(boolean isPublicMovementToFriend) {
		this.isPublicMovementToFriend = isPublicMovementToFriend;
	}

	public boolean isPublicAnswersToFriend() {
		return isPublicAnswersToFriend;
	}

	public void setPublicAnswersToFriend(boolean isPublicAnswersToFriend) {
		this.isPublicAnswersToFriend = isPublicAnswersToFriend;
	}

	public boolean isSaveFlow() {
		return isSaveFlow;
	}

	public void setSaveFlow(boolean isSaveFlow) {
		this.isSaveFlow = isSaveFlow;
	}

}
