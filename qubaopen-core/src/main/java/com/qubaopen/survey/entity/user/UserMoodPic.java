package com.qubaopen.survey.entity.user;

import com.qubaopen.core.entity.AbstractBaseEntity;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * Created by mars on 15/4/8.
 */
@Entity
@Table(name = "user_mood_pic")
@Audited
public class UserMoodPic extends AbstractBaseEntity<Long> {

    /**
     * 图片类型
     */
    @Enumerated
    private Type type;

    public enum Type {
        Up, Top, Down, Bottom
    }

    /**
     * 路径
     */
    private String path;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
