package com.lnu.db;

import java.io.Serializable;

public abstract class BaseEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private Long				_id;
	
	public Long getId() {
		return _id;
	}
	
	void setId(Long id) {
		this._id = id;
	}
	
}
