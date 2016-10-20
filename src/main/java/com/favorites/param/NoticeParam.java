package com.favorites.param;
/**
*@ClassName: NoticeParam
*@Description: 
*@author YY 
*@date 2016年10月19日  上午10:41:08
*@version 1.0
*/
public class NoticeParam extends BaseParam{
	
	private Integer page = 0;
	private Integer size = 15;
	private String type;
	private Long userId;
	
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
