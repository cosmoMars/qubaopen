package com.qubaopen.survey.entity.user;

import com.qubaopen.core.entity.AbstractBaseEntity;
import org.hibernate.envers.Audited;

import javax.persistence.*;

/**
 * @author mars 用户分享表
 */
@Entity
@Table(name = "user_share")
@Audited
public class UserShare extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -5848763450183067878L;

	/**
	 * 用户
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Enumerated
	private ShareTarget shareTarget;

	/**
	 * 分享目标 0新浪微博 1腾讯微博 2微信朋友圈 3 QQ空间 4微信
	 */
	public enum ShareTarget {
		SINA_WEIBO, TENCENT_WEIBO, WECHAT_FRIEND, QQSPACE, WECHAT
	}

	@Enumerated
	private ShareOrigin shareOrigin;

	/**
	 * 分享来源 0分享软件 1奖品 2自测 3兴趣 4调研 5心理地图 6自测解析度称号,7专栏,8练习
	 */
	public enum ShareOrigin {
		SOFTWARE, REWARD, SELF, INTEREST, SURVEY, MINDMAP, USERSELFTITLE, Topic, Exercise
	}

	/**
	 * 备注 用来记录 分享来源的id
	 */
	private String remark;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ShareTarget getShareTarget() {
		return shareTarget;
	}

	public void setShareTarget(ShareTarget shareTarget) {
		this.shareTarget = shareTarget;
	}

	public ShareOrigin getShareOrigin() {
		return shareOrigin;
	}

	public void setShareOrigin(ShareOrigin shareOrigin) {
		this.shareOrigin = shareOrigin;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
