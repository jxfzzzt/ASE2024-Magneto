package com.car.common.res;

import java.io.Serializable;

import com.car.common.enums.ResEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultRes<T> implements Serializable {
	@ApiModelProperty(value = "输出编码，如：0000", name = "code", example = "0000")
	private String code;
	@ApiModelProperty(value = "输出消息(String)", name = "msg", example = "操作成功")
	private String msg;
	@ApiModelProperty(value = "输出对象(Object)", name = "data")
	private T data;

	public ResultRes(ResEnum resEnum) {
		this.code = resEnum.getValue();
		this.msg = resEnum.getDesc();
	}

	public ResultRes(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	/**
	 * 系统默认返回内置错误编码
	 * code : 9999
	 * msg : 系统繁忙
	 * @return BaseRes
	 */
	public static ResultRes error() {
		ResultRes baseRes = new ResultRes();
		baseRes.setCode(ResEnum.SYSTEM_ERROR.getValue());
		baseRes.setMsg(ResEnum.SYSTEM_ERROR.getDesc());
		return baseRes;
	}

	/**
	 * 系统默认返回内置错误编码
	 * code : 9999
	 * msg : 系统繁忙
	 * @param data  返回消息数据
	 * @return BaseRes
	 */
	public static ResultRes<String> error(String data) {
		ResultRes<String> baseRes = new ResultRes<>();
		baseRes.setCode(ResEnum.SYSTEM_ERROR.getValue());
		baseRes.setMsg(ResEnum.SYSTEM_ERROR.getDesc());
		baseRes.setData(data);
		return baseRes;
	}

	/**
	 * 系统默认返回内置错误编码
	 * code : 9999
	 * msg : 系统繁忙
	 * @param resEnum  返回消息数据
	 * @return BaseRes
	 */
	public static ResultRes error(ResEnum resEnum) {
		ResultRes baseRes = new ResultRes();
		baseRes.setCode(resEnum.getValue());
		baseRes.setMsg(resEnum.getDesc());
		return baseRes;
	}

	/**
	 * 返回错误消息编码，不包含消息体
	 * @param code
	 * @param msg
	 * @return BaseRes
	 */
	public static ResultRes error(String code, String msg) {
		ResultRes baseRes = new ResultRes();
		baseRes.setCode(code);
		baseRes.setMsg(msg);
		return baseRes;
	}

	/**
	 * 返回错误消息编码，包含消息体
	 * @param code  编码
	 * @param msg   消息
	 * @param data  字符串
	 * @return BaseRes
	 */
	public static ResultRes error(String code, String msg, String data) {
		ResultRes baseRes = new ResultRes();
		baseRes.setCode(code);
		baseRes.setMsg(msg);
		baseRes.setData(data);
		return baseRes;
	}

	/**
	 * 返回错误消息体
	 * @param data
	 * @param <T>
	 * @return BaseRes
	 */
	public static <T> ResultRes<T> error(T data, String msg) {
		return new ResultRes(ResEnum.SYSTEM_ERROR.getValue(), msg, data);
	}

	/**
	 * 返回错误消息体
	 * @param data
	 * @param <T>
	 * @return BaseRes
	 */
	public static <T> ResultRes<T> error(String code, String msg, T data) {
		ResultRes<T> baseRes = new ResultRes<T>();
		baseRes.setCode(code);
		baseRes.setMsg(msg);
		baseRes.setData(data);
		return baseRes;
	}

	/**
	 * 返回正确消息体
	 * @param data
	 * @param <T>
	 * @return BaseRes
	 */
	public static <T> ResultRes<T> success(T data) {
		return new ResultRes(ResEnum.SUCCESS.getValue(), ResEnum.SUCCESS.getDesc(), data);
	}

	/**
	 * 返回正确消息
	 * @param <T>
	 * @return
	 */
	public static <T> ResultRes<T> success() {
		return new ResultRes(ResEnum.SUCCESS.getValue(), ResEnum.SUCCESS.getDesc());
	}

	/**
	 * 判断是否执行正确
	 * @return
	 */
	public boolean isSuccess() {
		return this.code.equals(ResEnum.SUCCESS.getValue());
	}

	@Override
	public String toString() {
		return "BaseRes(code=" + this.getCode() + ", msg=" + this.getMsg() + ", data=" + this.getData() + ")";
	}

	public ResultRes(final String code, final String msg, final T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public ResultRes() {
	}
}
