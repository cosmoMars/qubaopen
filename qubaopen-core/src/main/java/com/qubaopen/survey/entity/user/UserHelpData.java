package com.qubaopen.survey.entity.user;

import com.qubaopen.survey.entity.comment.Help;
import com.qubaopen.survey.entity.comment.HelpComment;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by mars on 15/5/28.
 */
@Entity
@Table
public class UserHelpData extends AbstractPersistable<Long> {

    /**
     * 用户
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    /**
     * 求助
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Help help;

    /**
     * 求助评论
     */
    private HelpComment helpComment;

    /**
     * 求助数量
     */
    private int helpCommentCount;

    /**
     * 最后一条纪录
     */
    private long currentHelpId;

    /**
     * 刷新
     */
    private boolean refresh;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Help getHelp() {
        return help;
    }

    public void setHelp(Help help) {
        this.help = help;
    }

    public int getHelpCommentCount() {
        return helpCommentCount;
    }

    public void setHelpCommentCount(int helpCommentCount) {
        this.helpCommentCount = helpCommentCount;
    }

    public long getCurrentHelpId() {
        return currentHelpId;
    }

    public void setCurrentHelpId(long currentHelpId) {
        this.currentHelpId = currentHelpId;
    }

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    public HelpComment getHelpComment() {
        return helpComment;
    }

    public void setHelpComment(HelpComment helpComment) {
        this.helpComment = helpComment;
    }
}
