package com.lnu.db;

import java.io.Serializable;

public abstract class BaseEntity implements Serializable {
	
	private static final long	serialVersionUID	= 1L;
	private Long				_id;
	
	public Long getId() {
		return _id;
	}
	
	void setId(Long id) {
		this._id = id;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseEntity other = (BaseEntity) obj;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		return true;
	}
	
}
