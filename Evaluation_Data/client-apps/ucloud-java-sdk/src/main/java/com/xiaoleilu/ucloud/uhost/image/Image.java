package com.xiaoleilu.ucloud.uhost.image;

import org.slf4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiaoleilu.hutool.Log;
import com.xiaoleilu.hutool.StrUtil;

/**
 * 镜像
 * @author Looly
 *
 */
public class Image {
	private static final Logger log = Log.get();
	
	/** 镜像ID */
	private String imageId;
	/** 镜像名称 */
	private String imageName;
	/** 镜像类型：标准镜像：Base，行业镜像：Business， 自定义镜像：Custom */
	private ImageType imageType;
	/** 镜像状态， 可用：Available，制作中：Making， 不可用：Unavailable */
	private String state;
	/** 镜像描述 */
	private String imageDescription;
	
	/** 操作系统名称 */
	private String osName;
	/** 操作系统类型：Liunx，Windows */
	private OsType osType;
	
	/** 创建时间 */
	private long createTime;
	
	// --------------------------------------------------------------- Getters And Setters start
	/**
	 * @return 镜像ID
	 */
	public String getImageId() {
		return imageId;
	}
	/**
	 * 设置镜像ID
	 * @param imageId 镜像ID
	 */
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	/**
	 * @return 镜像名称
	 */
	public String getImageName() {
		return imageName;
	}
	/**
	 * 设置镜像名称
	 * @param imageName 镜像名称
	 */
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	/**
	 * @return 镜像类型
	 */
	public ImageType getImageType() {
		return imageType;
	}
	/**
	 * 设置镜像类型
	 * @param imageType 镜像类型
	 */
	public void setImageType(ImageType imageType) {
		this.imageType = imageType;
	}

	/**
	 * @return 镜像状态
	 */
	public String getState() {
		return state;
	}
	/**
	 * 设置镜像状态
	 * @param state 镜像状态
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return 镜像描述
	 */
	public String getImageDescription() {
		return imageDescription;
	}
	/**
	 * 设置镜像描述
	 * @param imageDescription 镜像描述
	 */
	public void setImageDescription(String imageDescription) {
		this.imageDescription = imageDescription;
	}

	/**
	 * @return 操作系统名称
	 */
	public String getOsName() {
		return osName;
	}
	/**
	 * 设置操作系统名称
	 * @param osName 操作系统名称
	 */
	public void setOsName(String osName) {
		this.osName = osName;
	}

	/**
	 * @return 操作系统类型
	 */
	public OsType getOsType() {
		return osType;
	}
	/**
	 * 设置操作系统类型
	 * @param osType 操作系统类型
	 */
	public void setOsType(OsType osType) {
		this.osType = osType;
	}

	/**
	 * @return 创建时间
	 */
	public long getCreateTime() {
		return createTime;
	}
	/**
	 * 设置创建时间
	 * @param createTime 创建时间
	 */
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	// --------------------------------------------------------------- Getters And Setters end
	
	/**
	 * 转为JSON字符串
	 */
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	/**
	 * Image JSON对象转为Java 对象
	 * @param jsonObj JSON对象
	 * @return Image
	 */
	public static Image parse(JSONObject jsonObj) {
		if(null == jsonObj) {
			return null;
		}
		
		final Image image = new Image();
		
		image.imageId = jsonObj.getString("ImageId");
		image.imageName = jsonObj.getString("ImageName");
		image.state = jsonObj.getString("State");
		image.osName = jsonObj.getString("OsName");
		image.createTime = jsonObj.getLong("CreateTime");
		image.imageDescription = jsonObj.getString("ImageDescription");
		
		String osType = null;
		try {
			osType = jsonObj.getString("OsType");
			if(StrUtil.isNotBlank(osType)) {
				image.osType = OsType.valueOf(osType);
			}
		} catch (Exception e) {
			log.warn("Unknown OS Type {}, error: {}", osType, e.getMessage());
		}
		
		String imageType = null;
		try {
			imageType = jsonObj.getString("ImageType");
			if(StrUtil.isNotBlank(imageType)) {
				image.imageType = ImageType.valueOf(imageType);
			}
		} catch (Exception e) {
			log.warn("Unknown Image Type {}, error: {}", imageType, e.getMessage());
		}
		
		return image;
	}
}
