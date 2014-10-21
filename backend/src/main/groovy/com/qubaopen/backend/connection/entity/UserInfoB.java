package com.qubaopen.backend.connection.entity;

public class UserInfoB {

	private long id;
	
	private String name;
	
	private String sex;
	
	private String bloodType;
	
	private boolean sinaShare;
	
	private boolean tencentShare;
	
	private boolean weChatFriendShare;
	
	private boolean qqSpaceShare;
	
	private boolean weChatShare;
	
	private boolean publicToCheif;
	
	private boolean saveFlow;
	
	private boolean publicMovement;
	
	private boolean publicAnswers;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBloodType() {
		return bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	public boolean isSinaShare() {
		return sinaShare;
	}

	public void setSinaShare(boolean sinaShare) {
		this.sinaShare = sinaShare;
	}

	public boolean isTencentShare() {
		return tencentShare;
	}

	public void setTencentShare(boolean tencentShare) {
		this.tencentShare = tencentShare;
	}

	public boolean isWeChatFriendShare() {
		return weChatFriendShare;
	}

	public void setWeChatFriendShare(boolean weChatFriendShare) {
		this.weChatFriendShare = weChatFriendShare;
	}

	public boolean isQqSpaceShare() {
		return qqSpaceShare;
	}

	public void setQqSpaceShare(boolean qqSpaceShare) {
		this.qqSpaceShare = qqSpaceShare;
	}

	public boolean isWeChatShare() {
		return weChatShare;
	}

	public void setWeChatShare(boolean weChatShare) {
		this.weChatShare = weChatShare;
	}

	public boolean isPublicToCheif() {
		return publicToCheif;
	}

	public void setPublicToCheif(boolean publicToCheif) {
		this.publicToCheif = publicToCheif;
	}

	public boolean isSaveFlow() {
		return saveFlow;
	}

	public void setSaveFlow(boolean saveFlow) {
		this.saveFlow = saveFlow;
	}

	public boolean isPublicMovement() {
		return publicMovement;
	}

	public void setPublicMovement(boolean publicMovement) {
		this.publicMovement = publicMovement;
	}

	public boolean isPublicAnswers() {
		return publicAnswers;
	}

	public void setPublicAnswers(boolean publicAnswers) {
		this.publicAnswers = publicAnswers;
	}
	
}
