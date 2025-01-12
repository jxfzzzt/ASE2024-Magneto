package cn.qs.bean.user;

import javax.persistence.MappedSuperclass;

/**
 * 微信授权获取到的用户信息
 * 
 * @author Administrator
 *
 */
@MappedSuperclass
public class WechatUser {

	// openid
	private String openid;

	/**
	 * 昵称
	 */
	private String nickname;
	// 1 男; 0女
	private String sex;

	private String language;

	// 西城
	private String city;

	// 北京
	private String province;

	// 中国
	private String country;

	/**
	 * 头像地址
	 */
	private String headimgurl;

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	@Override
	public String toString() {
		return "WechatUser [openid=" + openid + ", nickname=" + nickname + ", sex=" + sex + ", language=" + language
				+ ", city=" + city + ", province=" + province + ", country=" + country + ", headimgurl=" + headimgurl
				+ "]";
	}

}