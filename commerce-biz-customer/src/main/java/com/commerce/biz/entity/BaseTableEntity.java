package com.commerce.biz.entity;

import java.time.Instant;

import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * The persistent class for the base_table database table.
 * 
 */
@Table("cloud.base_table")
public abstract class BaseTableEntity implements Persistable<String>{

	@Column("from_ip")
	private String fromIp;
	
	@Column("create_by")
	private String createBy;

	@Column("create_time")
	private Instant createTime;

	@Column("modify_by")
	private String modifyBy;

	@Column("modify_time")
	private Instant modifyTime;
	
	@Transient
    private boolean isNew;

	protected BaseTableEntity() {
		// Do nothing 
	}

	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Instant getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Instant createTime) {
		this.createTime = createTime;
	}

	public String getFromIp() {
		return this.fromIp;
	}

	public void setFromIp(String fromIp) {
		this.fromIp = fromIp;
	}

	public String getModifyBy() {
		return this.modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	public Instant getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(Instant modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Override
    @Transient
    public boolean isNew() {
        return this.isNew || getId() == null;
    }

	/**
	 * R2dbc不支持插入id有值，id有值會更新。所以必須實現Persistable， <br>
	 * 重寫isNew保存時如果id有值，調用setAsNew 即可保存 略顯繁瑣 <br>
	 * @param <T>
	 * 
	 * */
    @SuppressWarnings("unchecked")
	public <T> T setAsNew(){
        this.isNew = true;
        return (T) this;
    }

}