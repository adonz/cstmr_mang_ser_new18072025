package com.incede.nbfc.customer_management.BaseEntity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

@MappedSuperclass
@Data
public abstract class BaseEntity {
	
	
	@Column(name="is_del")
	private Boolean isDelete = false;
	
	@Column(name="created_by" , nullable= false)
	private Integer createdBy ;
	
	@Column(name="created_at")
	private LocalDateTime createdAt;
	
	@Column(name="updated_by", nullable= true)
	private Integer updatedBy;
	
	@Column(name="updated_at")
	private LocalDateTime updatedAt;
	
	
	
	@PrePersist
	protected void onCreate() {
		this.createdAt= LocalDateTime.now();
	}
	@PreUpdate
	protected void onUpdate() {
		this.updatedAt=LocalDateTime.now();
	}

}
