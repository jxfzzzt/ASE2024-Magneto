package com.car.account.client.enums.technician;

import com.car.account.client.enums.withdrawal.WithdrawalUserRoleEnum;
import lombok.Getter;

/**
 * @author zhouz
 * @date 2021/2/7
 */
@Getter
public enum TechnicianCertificateImgTypeEnum {

    /**
     * 证书图片类型:0 国家等级鉴定 1 主机厂认证
     */
    STATE_VERIFICATION(0, "国家等级鉴定"),
    HOST_AUTHENTICATION(1, "主机厂认证");

    private Integer value;
    private String desc;

    TechnicianCertificateImgTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (TechnicianCertificateImgTypeEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
