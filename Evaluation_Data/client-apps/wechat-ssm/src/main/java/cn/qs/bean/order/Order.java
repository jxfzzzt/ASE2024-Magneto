package cn.qs.bean.order;

import javax.persistence.Entity;

import cn.qs.bean.AbstractUUIDEntity;

/**
 * 养殖场
 */

@Entity(name = "order_table")
public class Order extends AbstractUUIDEntity {

	/**
	 * 出货单位
	 */
	private String shippingUnit;

	/**
	 * 购货单位
	 */
	private String purchaseUnit;

	/**
	 * 交易时间
	 */
	private String tradeTime;

	/**
	 * 交易地址
	 */
	private String tradeAddress;

	/**
	 * 订单数量
	 */
	private Integer orderNumbers;

	private String remark1;

	private String remark2;

	private String remark3;

	public String getShippingUnit() {
		return shippingUnit;
	}

	public void setShippingUnit(String shippingUnit) {
		this.shippingUnit = shippingUnit;
	}

	public String getPurchaseUnit() {
		return purchaseUnit;
	}

	public void setPurchaseUnit(String purchaseUnit) {
		this.purchaseUnit = purchaseUnit;
	}

	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}

	public String getTradeAddress() {
		return tradeAddress;
	}

	public void setTradeAddress(String tradeAddress) {
		this.tradeAddress = tradeAddress;
	}

	public Integer getOrderNumbers() {
		return orderNumbers;
	}

	public void setOrderNumbers(Integer orderNumbers) {
		this.orderNumbers = orderNumbers;
	}

	public String getRemark1() {
		return remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getRemark2() {
		return remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	public String getRemark3() {
		return remark3;
	}

	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}

}
