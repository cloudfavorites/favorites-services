package com.favorites.param;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class BaseParam {
	
	protected String myself;// 是否是当前用户

	
	public String getMyself() {
		return myself;
	}



	public void setMyself(String myself) {
		this.myself = myself;
	}



	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
