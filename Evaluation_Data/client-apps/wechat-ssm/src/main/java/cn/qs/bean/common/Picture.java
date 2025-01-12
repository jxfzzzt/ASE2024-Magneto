package cn.qs.bean.common;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

//系统图片表
@Entity
public class Picture {
	@Id
	private String id;

	private String name;

	private String path;

	private Date createtime;

	private String pictureblank;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path == null ? null : path.trim();
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getPictureblank() {
		return pictureblank;
	}

	public void setPictureblank(String pictureblank) {
		this.pictureblank = pictureblank == null ? null : pictureblank.trim();
	}
}