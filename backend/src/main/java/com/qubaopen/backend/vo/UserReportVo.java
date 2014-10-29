package com.qubaopen.backend.vo;

import java.io.Serializable;

public class UserReportVo implements Serializable {

	private static final long serialVersionUID = -7551780391634146572L;

	/**
	 * 日期
	 */
	private String date;

	/**
	 * 总注册人数
	 */
	private int registerCount;

	/**
	 * 成功注册人数
	 */
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
