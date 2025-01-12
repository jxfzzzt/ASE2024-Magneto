package com.xiaoleilu.ucloud.uhost.image;

/**
 * 镜像过滤器<br>
 * 用于筛选返回的镜像
 * @author Looly
 *
 */
public interface ImageFilter {
	/**
	 * 过滤
	 * @param image 镜像对象
	 * @return 是否是满足条件的对象
	 */
	public boolean filter(Image image);
}
