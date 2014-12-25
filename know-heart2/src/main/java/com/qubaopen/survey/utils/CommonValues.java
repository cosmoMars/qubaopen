package com.qubaopen.survey.utils;

/**
 * 表示业务公用参数
 * 
 * @param CHECKNUM
 *            ：手机用户每天可以申请验证码的次数
 * @param ADDRESSNUM
 *            :·用户收货地址最大数量
 * @param MAXCOIN
 *            :·每天最多获得金币数
 * 
 */

public enum CommonValues {

	VERIFYNUM(10), ADDRESSNUM(10), MAXCOIN(100);

	int value;

	CommonValues(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
