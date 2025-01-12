package cn.qs.bean.wechat;

import javax.persistence.Column;
import javax.persistence.Entity;

import cn.qs.bean.AbstractSequenceEntity;

/**
 * 幼儿园
 * 
 * @author QiaoLiQiang
 * @time 2019年12月11日下午10:25:30
 */
@Entity
public class Kindergarten extends AbstractSequenceEntity {

	/**
	 * 幼儿园名称
	 */
	@Column(unique = true)
	private String name;

	/**
	 * 版本号
	 */
	private String version;

	/**
	 * 服务商
	 */
	private String server;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 缴费金额(定值)
	 */
	private Float amount;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "Kindergarten [name=" + name + ", version=" + version + ", server=" + server + ", address=" + address
				+ ", amount=" + amount + ", id=" + id + ", creator=" + creator + ", createtime=" + createtime
				+ ", remark1=" + remark1 + ", remark2=" + remark2 + ", remark3=" + remark3 + ", properties="
				+ properties + ", propertiesMap=" + propertiesMap + "]";
	}

}
