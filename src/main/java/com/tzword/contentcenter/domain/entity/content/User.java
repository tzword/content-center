package com.tzword.contentcenter.domain.entity.content;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@FieldNameConstants
public class User {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 自我介绍
     */
    private String aboutme;

    /**
     * 经过MD5加密的密码
     */
    private String passwd;

    /**
     * 头像图片
     */
    private String avatar;

    /**
     * 1:普通用户，2:房产经纪人
     */
    private Boolean type;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否启用,1启用，0停用
     */
    private Boolean enable;

    /**
     * 所属经纪机构
     */
    private Integer agencyId;
}