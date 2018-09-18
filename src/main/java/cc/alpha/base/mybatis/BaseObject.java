package cc.alpha.base.mybatis;

import java.io.Serializable;
import java.util.Date;



/**
 * 持久化基础对象
 * @author hy suncheng
 */
public class BaseObject implements Serializable{


	private static final long serialVersionUID = -8687717417709037969L;

	private String id;
	/** 创建时间 */
	private Date createTime;
	/** 修改时间 */
	private Date updateTime;
	/** 创建人 */
	private String createUserId;
	/** 修改人 */
	private String updateUserId;
	/** 是否删除标志 0未删除 1 已删除 */
	private Integer isDeleted = 0;


	public void updateBefore(String userId){
		this.setUpdateTime(new Date());
		this.setUpdateUserId(userId);
	}

	public void saveBefore(String userId){
		this.setUpdateTime(new Date());
		this.setCreateTime(new Date());
		this.setCreateUserId(userId);
		this.setUpdateUserId(userId);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
}
