package com.tzword.contentcenter.domain.entity.content;

import java.util.Date;
import javax.persistence.*;

@Table(name = "share")
public class Share {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "JDBC")
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
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 是否启用,1启用，0停用
     */
    private Boolean enable;

    /**
     * 所属经纪机构
     */
    @Column(name = "agency_id")
    private Integer agencyId;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取姓名
     *
     * @return name - 姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置姓名
     *
     * @param name 姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取手机号
     *
     * @return phone - 手机号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置手机号
     *
     * @param phone 手机号
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取电子邮件
     *
     * @return email - 电子邮件
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置电子邮件
     *
     * @param email 电子邮件
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取自我介绍
     *
     * @return aboutme - 自我介绍
     */
    public String getAboutme() {
        return aboutme;
    }

    /**
     * 设置自我介绍
     *
     * @param aboutme 自我介绍
     */
    public void setAboutme(String aboutme) {
        this.aboutme = aboutme;
    }

    /**
     * 获取经过MD5加密的密码
     *
     * @return passwd - 经过MD5加密的密码
     */
    public String getPasswd() {
        return passwd;
    }

    /**
     * 设置经过MD5加密的密码
     *
     * @param passwd 经过MD5加密的密码
     */
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    /**
     * 获取头像图片
     *
     * @return avatar - 头像图片
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * 设置头像图片
     *
     * @param avatar 头像图片
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * 获取1:普通用户，2:房产经纪人
     *
     * @return type - 1:普通用户，2:房产经纪人
     */
    public Boolean getType() {
        return type;
    }

    /**
     * 设置1:普通用户，2:房产经纪人
     *
     * @param type 1:普通用户，2:房产经纪人
     */
    public void setType(Boolean type) {
        this.type = type;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取是否启用,1启用，0停用
     *
     * @return enable - 是否启用,1启用，0停用
     */
    public Boolean getEnable() {
        return enable;
    }

    /**
     * 设置是否启用,1启用，0停用
     *
     * @param enable 是否启用,1启用，0停用
     */
    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    /**
     * 获取所属经纪机构
     *
     * @return agency_id - 所属经纪机构
     */
    public Integer getAgencyId() {
        return agencyId;
    }

    /**
     * 设置所属经纪机构
     *
     * @param agencyId 所属经纪机构
     */
    public void setAgencyId(Integer agencyId) {
        this.agencyId = agencyId;
    }
}