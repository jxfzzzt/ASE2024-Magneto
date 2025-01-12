package com.xiaoleilu.ucloud.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiaoleilu.hutool.StrUtil;

/**
 * API响应内容标准实现
 * @author Looly
 *
 */
public class StandardResponse implements Response{
	
	// --------------------------------------------------------------- Static method start
	/**
	 * 将返回的JSON字符串转为响应对象
	 * @param jsonStr 响应JSON字符串
	 * @return 响应对象
	 */
	public static StandardResponse parse(String jsonStr) {
		return new StandardResponse(jsonStr);
	}
	// --------------------------------------------------------------- Static method end
	
	/** 服务器返回的JSON对象 */
	private JSONObject json;
	
	// --------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 * @param jsonStr Response JSON字符串
	 */
	public StandardResponse(String jsonStr) {
		//对于文件操作，不返回内容，此时表示成功
		if(StrUtil.isBlank(jsonStr)) {
			json = new JSONObject();
			json.put(RET_CODE, RetCode.OK);
		}else {
			json = JSON.parseObject(jsonStr);
		}
		
	}
	// --------------------------------------------------------------- Constructor end
	
	@Override
	public Object get(String key) {
		return this.json.get(key);
	}
	
	@Override
	public int getRetCode() {
		return this.json.getIntValue(RET_CODE);
	}
	
	@Override
	public String getAction() {
		return this.json.getString(ACTION);
	}
	
	@Override
	public String getMessage() {
		return this.json.getString(MESSAGE);
	}
	
	@Override
	public int getTotalCount() {
		return this.json.getIntValue(TOTAL_COUNT);
	}
	
	@Override
	public JSONObject getJson(){
		return this.json;
	}
	
	@Override
	public boolean isOk() {
		return RetCode.OK == this.getRetCode();
	}
	
	@Override
	public String toPretty(){
		return JSON.toJSONString(this.json, true);
	}
	
	/**
	 * 返回原JSON字符串
	 */
	@Override
	public String toString() {
		return this.json.toJSONString();
	}
}
