package cn.qs.bean;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 所有实体类中相同的部分(自增ID类型的)
 * 
 * @author Administrator
 *
 */
@MappedSuperclass
public abstract class AbstractSequenceEntity extends AbstractEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
