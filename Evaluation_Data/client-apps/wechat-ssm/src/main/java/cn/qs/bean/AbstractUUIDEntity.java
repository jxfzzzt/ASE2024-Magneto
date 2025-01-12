package cn.qs.bean;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

/**
 * 所有实体类中相同的部分(UUID类型的)
 * 
 * @author Administrator
 *
 */
@MappedSuperclass
public abstract class AbstractUUIDEntity extends AbstractEntity {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	protected String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
