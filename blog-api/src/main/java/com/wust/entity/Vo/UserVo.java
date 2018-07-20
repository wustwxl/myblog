package com.wust.entity.Vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserVo implements Serializable {
    /**
     * user表主键
     */
    private Integer uid;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户的邮箱
     */
    private String email;

    /**
     * 用户的主页
     */
    private String homeUrl;

    /**
     * 用户显示的名称
     */
    private String screenName;

    /**
     * 用户注册时间
     */
    private Integer created;

    /**
     * 最后活动时间
     */
    private Integer activated;

    /**
     * 上次登录最后活跃时间
     */
    private Integer logged;

    /**
     * 用户组
     */
    private String groupName;

    private static final long serialVersionUID = 1L;

}