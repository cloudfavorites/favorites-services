package com.favorites.param;

import org.apache.commons.lang3.StringUtils;

public class CollectParam extends BaseParam{
	
	private Integer page = 0;
	private Integer size = 15;
	private String type;
	private Long userId;
	private Long favoritesId;
	
	
	public Long getFavoritesId() {
		return favoritesId;
	}
	public void setFavoritesId(Long favoritesId) {
		this.favoritesId = favoritesId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
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
	
	public static boolean checkParam(CollectParam collectParam){
		if(null == collectParam || null == collectParam.getPage() || null == collectParam.getUserId()
				|| StringUtils.isBlank(collectParam.getMyself()) || StringUtils.isBlank(collectParam.getType())){
			return false;
		}
		if("myselffavorites".equalsIgnoreCase(collectParam.getMyself()+collectParam.getType())
				|| "otherfavorites".equalsIgnoreCase(collectParam.getMyself()+collectParam.getType())){
			if(null == collectParam.getFavoritesId()){
				return false;
			}
		}
		return true;
	}

}
