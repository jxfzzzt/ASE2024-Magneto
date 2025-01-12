package com.car.account.web.model.store;

import com.car.common.datasource.model.BaseModelInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author zhouz
 * @date 2020/12/19
 */
@Data
@Table(name = "store_user")
public class StoreUser extends BaseModelInfo {

    /**
     * 店铺uuid
     */
    @Column(name = "store_uuid")
    private String storeUuid;

    /**
     * 姓名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 手机号
     */
    @Column(name = "mobile")
    private String mobile;

    /**
     * 人员类型,对应字典表 uuid
     */
    @Column(name = "person_type")
    private String personType;

    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 证件类型1身份证2护照
     */
    @Column(name = "id_type")
    private Integer idType;

    @Column(name = "id_num")
    private String idNum;

    /**
     * 店铺用户头像链接地址
     */
    @Column(name = "photo_img_url")
    private String photoImgUrl;

}
