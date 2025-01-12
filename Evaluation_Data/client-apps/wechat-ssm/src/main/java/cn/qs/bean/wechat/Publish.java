package cn.qs.bean.wechat;

import java.sql.Date;

import javax.persistence.Entity;

import cn.qs.bean.AbstractSequenceEntity;

/**
 * 朋友圈转发信息
 */
@Entity
public class Publish extends AbstractSequenceEntity {

	private Date publishTime;
	
	private Integer userId;
	
	private String  username;

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
