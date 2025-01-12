package com.car.common.enums;

import lombok.Getter;

/**
 * 团购状态
 * @author jinhaifei
 *
 */
@Getter
public enum GroupbuyEnum {
	/**
	 * 数据状态枚举
	 */
	WAIT(0, "等待开始"), PROCESSING(1, "进行中"), OVER(2, "已结束");

	private Integer value;
	private String desc;

	GroupbuyEnum(Integer value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public static String enumOfDesc(Integer value) {
		for (GroupbuyEnum enums : values()) {
			if (enums.value.equals(value)) {
				return enums.desc;
			}
		}
		return null;
	}

}
