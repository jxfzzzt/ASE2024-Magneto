package cn.qs.bean.wechat;

import java.util.Date;

import javax.persistence.Entity;

import cn.qs.bean.AbstractSequenceEntity;

/**
 * 缴费信息
 * 
 * @author QiaoLiQiang
 * @time 2019年12月11日下午10:25:30
 */
// Remark1用作备注，用于标记使用优惠金额等信息
@Entity
public class Pay extends AbstractSequenceEntity {

	/**
	 * 幼儿园ID
	 */
	private Integer kindergartenId;

	/**
	 * 幼儿园姓名
	 */
	private String kindergartenName;

	/**
	 * 用户ID
	 */
	private Integer userId;

	/**
	 * username(缴费者)
	 */
	private String username;

	/**
	 * 学期
	 */
	private String semester;

	/**
	 * 缴费日期
	 */
	private Date payDate;

	private String parentName;

	private String parentPhone;

	private String childrenName;

	/**
	 * 年级
	 */
	private String grade;

	/**
	 * 班级
	 */
	private String classNum;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 收费金额
	 */
	private Float payAmount;

	/**
	 * 本次缴费生成的二维码
	 */
	private String qrcodePath;

	/**
	 * 发送到微信平台的orderId，对应微信平台out_trade_no参数
	 */
	private String orderId;

	/**
	 * 订单状态：未支付、已支付
	 */
	private String orderStatus;

	public Integer getKindergartenId() {
		return kindergartenId;
	}

	public void setKindergartenId(Integer kindergartenId) {
		this.kindergartenId = kindergartenId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getParentPhone() {
		return parentPhone;
	}

	public void setParentPhone(String parentPhone) {
		this.parentPhone = parentPhone;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getClassNum() {
		return classNum;
	}

	public void setClassNum(String classNum) {
		this.classNum = classNum;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Float getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Float payAmount) {
		this.payAmount = payAmount;
	}

	public String getQrcodePath() {
		return qrcodePath;
	}

	public void setQrcodePath(String qrcodePath) {
		this.qrcodePath = qrcodePath;
	}

	public String getKindergartenName() {
		return kindergartenName;
	}

	public void setKindergartenName(String kindergartenName) {
		this.kindergartenName = kindergartenName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getChildrenName() {
		return childrenName;
	}

	public void setChildrenName(String childrenName) {
		this.childrenName = childrenName;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	@Override
	public String toString() {
		return "Pay [kindergartenId=" + kindergartenId + ", kindergartenName=" + kindergartenName + ", userId=" + userId
				+ ", username=" + username + ", semester=" + semester + ", payDate=" + payDate + ", parentName="
				+ parentName + ", parentPhone=" + parentPhone + ", childrenName=" + childrenName + ", grade=" + grade
				+ ", classNum=" + classNum + ", address=" + address + ", payAmount=" + payAmount + ", qrcodePath="
				+ qrcodePath + ", orderId=" + orderId + ", orderStatus=" + orderStatus + "]";
	}

}
