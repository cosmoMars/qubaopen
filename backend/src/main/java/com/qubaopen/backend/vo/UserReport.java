package com.qubaopen.backend.vo;

import java.io.Serializable;

public class UserReport implements Serializable {

	private static final long serialVersionUID = -7551780391634146572L;

	private String date;

	private int registerCount;

	private int userCount;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getRegisterCount() {
		return registerCount;
	}

	public void setRegisterCount(int registerCount) {
		this.registerCount = registerCount;
	}

	public int getUserCount() {
		return userCount;
	}

	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}

}
