package cn.qs.bean.user;

import java.util.Date;

import javax.persistence.Entity;

import cn.qs.bean.AbstractSequenceEntity;

// remark1 用作所属幼儿园(存的是幼儿园名称,修改幼儿园名称的时候要修改这里的remark1)
@Entity
public class User extends AbstractSequenceEntity {

	private String username;

	private String fullname;

	private String password;

	private String sex;

	private String phone;

	private String email;

	private Date updatetime;

	private String roles;

	private String userblank;

	// 微信昵称
	private String wechatnickname;

	// 微信头像
	private String wechatphoto;

	// 微信地址
	private String wechataddress;

	// 孩子姓名(用于交费快速带入)
	private String childrenname;

	// 优惠金额(可以在付钱的时候使用)
	private Float coupon;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username == null ? null : username.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname == null ? null : fullname.trim();
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex == null ? null : sex.trim();
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone == null ? null : phone.trim();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email == null ? null : email.trim();
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles == null ? null : roles.trim();
	}

	public String getUserblank() {
		return userblank;
	}

	public void setUserblank(String userblank) {
		this.userblank = userblank == null ? null : userblank.trim();
	}

	public String getWechatnickname() {
		return wechatnickname;
	}

	public void setWechatnickname(String wechatnickname) {
		this.wechatnickname = wechatnickname;
	}

	public String getWechatphoto() {
		return wechatphoto;
	}

	public void setWechatphoto(String wechatphoto) {
		this.wechatphoto = wechatphoto;
	}

	public String getWechataddress() {
		return wechataddress;
	}

	public void setWechataddress(String wechataddress) {
		this.wechataddress = wechataddress;
	}

	public String getChildrenname() {
		return childrenname;
	}

	public void setChildrenname(String childrenname) {
		this.childrenname = childrenname;
	}

	public Float getCoupon() {
		return coupon;
	}

	public void setCoupon(Float coupon) {
		this.coupon = coupon;
	}

}