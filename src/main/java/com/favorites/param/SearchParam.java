package com.favorites.param;
/**
*@ClassName: SearchParam
*@Description: 
*@author YY 
*@date 2016年10月25日  下午1:53:07
*@version 1.0
*/
public class SearchParam extends BaseParam{
	
	private Integer page = 0;
	private Integer size = 15;
	private Long userId;
	private String key;
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
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

}
