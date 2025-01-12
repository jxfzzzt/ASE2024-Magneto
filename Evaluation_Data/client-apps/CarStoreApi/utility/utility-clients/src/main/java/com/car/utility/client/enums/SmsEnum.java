package com.car.utility.client.enums;

import lombok.Getter;

/**
 * 数据状态枚举
 * @author xlj
 */
@Getter
public enum SmsEnum {
    REGISTER(0,"SMS_207875141","用户注册验证码"),
    StoreCheckSuccess(1,"SMS_207952593","店铺审核成功短信"),
    StoreCheckReject(2,"SMS_207947610","店铺审核驳回短信"),
    TechnicianCheckSuccess(3,"SMS_207952587","技师审核成功短信"),
    TechnicianCheckReject(4,"SMS_207947610","技师审核驳回短信"),


    ;
    private Integer value;
    private String smsCode;
    private String desc;

    SmsEnum(Integer value, String smsCode, String desc) {
        this.value = value;
        this.desc = desc;
        this.smsCode = smsCode;
    }

    public static String enumOfDesc(Integer value) {
        for (SmsEnum enums :
                values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }

    public static String enumOfSmsCode(Integer value) {
        for (SmsEnum enums :
                values()) {
            if (enums.value.equals(value)) {
                return enums.smsCode;
            }
        }
        return null;
    }

}
