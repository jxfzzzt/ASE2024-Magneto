package com.xiaoleilu.ucloud.core;

/**
 * API指令接口<br>
 * API指令一般为一个枚举类，实现此接口是为了更好的限定指令<br>
 * 每个模块的API指令类都要实现这一接口，例如UHost的实现枚举是UHostAction
 * @author Looly
 *
 */
public interface Action {
	public String toString();
}
