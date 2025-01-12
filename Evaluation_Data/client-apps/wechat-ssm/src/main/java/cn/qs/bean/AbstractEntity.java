package cn.qs.bean;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Index;

import com.alibaba.fastjson.JSONObject;

import cn.qs.utils.system.UserContext;

/**
 * 所有实体类中相同的部分
 * 
 * @author Administrator
 *
 */
@MappedSuperclass
public class AbstractEntity {

	/**
	 * 创建者
	 */
	@Index(name = "creator")
	protected String creator;

	/**
	 * 创建时间
	 */
	protected Date createtime;

	// 三个保留字段
	protected String remark1;
	protected String remark2;
	protected String remark3;

	/**
	 * 属性字段(用于后期扩展)
	 */
	protected String properties;

	@Transient
	protected Map<String, Object> propertiesMap;

	public AbstractEntity() {
		this.createtime = new Date();
		creator = UserContext.get() == null ? "" : UserContext.get().getUsername();
		propertiesMap = new LinkedHashMap<>();

		if (StringUtils.isNotBlank(properties)) {
			propertiesMap = JSONObject.parseObject(properties, LinkedHashMap.class);
		}
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
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

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;

		if (StringUtils.isNotBlank(properties)) {
			propertiesMap = JSONObject.parseObject(properties, LinkedHashMap.class);
		}
	}

	public Map<String, Object> getPropertiesMap() {
		return propertiesMap;
	}

	public void setPropertiesMap(Map<String, Object> propertiesMap) {
		this.propertiesMap = propertiesMap;
	}

	public void setProperty(String key, Object value) {
		propertiesMap.put(key, value);
		properties = JSONObject.toJSONString(propertiesMap);
	}

	public Object getProperty(String key) {
		return propertiesMap.get(key);
	}
}
