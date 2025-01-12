package com.car.account.web.model.contact;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author xielinjiang
 * @date 2020/12/22
 */
@Data
@Table(name = "contact_msg")
public class ContactMsg extends BaseModelInfo {

    /**
     * 姓名
     */
    @Column(name = "name")
    private String name;

    /**
     * 手机
     */
    @Column(name = "phone")
    private String phone;

    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 地址
     */
    @Column(name = "address")
    private String address;

    /**
     * 公司
     */
    @Column(name = "company")
    private String company;

    /**
     * 留言
     */
    @Column(name = "content")
    private String content;
}
