package com.qubaopen.survey.entity.payment;

public enum PayStatus {
	/** 失败 0. */
	FAILURE,
	/** 等待支付 1. */
	WAITING_PAYMENT,
	/** 成功 2. */
	COMPLETE,
	/** 超时 3. */
	TIMEOUT,
	/** 取消 4. */
	CANCEL,
	/** 过期 5. */
	OVERDUE,
	/** 等待退款 6. */
	WAITING_REFUND,
	/** 已退款 7. */
	REFUNDED;
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 * @author <a href="mailto:yangmujiang@xiaomashijia.com">Reamy(杨木江)</a>
	 * @date 2014-09-13 16:06:55
	 */
	@Override
	public String toString() {
		return this.ordinal()+"";
	}
}
