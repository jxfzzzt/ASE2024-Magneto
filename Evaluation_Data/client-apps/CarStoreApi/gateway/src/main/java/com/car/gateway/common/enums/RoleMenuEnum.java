package com.car.gateway.common.enums;

import lombok.Getter;

/**
 * @author Buerger
 * @date 2020/10/21 16:10
 */
@Getter
public enum  RoleMenuEnum {

    /**
     * 角色菜单枚举
     */
    FAIL_TO_GET_ROLE_MENU("00001","获取角色菜单失败");

    private String value;
    private String desc;

    RoleMenuEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
