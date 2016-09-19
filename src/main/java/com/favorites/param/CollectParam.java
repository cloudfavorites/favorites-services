package com.favorites.param;

import org.apache.commons.lang3.StringUtils;

public class CollectParam extends BaseParam {

	private Long id;
	private Integer page = 0;
	private Integer size = 15;
	private String type;
	private Long userId;
	private Long favoritesId;
	private String title;
	private String url;
	private String description;
	private String logoUrl;
	private String charset;
	private String collectType;
	private String remark;
	private String newFavorites;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getCollectType() {
		return collectType;
	}

	public void setCollectType(String collectType) {
		this.collectType = collectType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getNewFavorites() {
		return newFavorites;
	}

	public void setNewFavorites(String newFavorites) {
		this.newFavorites = newFavorites;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public static boolean checkParam(CollectParam collectParam) {
		if (null == collectParam || null == collectParam.getPage() || null == collectParam.getUserId()
				|| StringUtils.isBlank(collectParam.getMyself()) || StringUtils.isBlank(collectParam.getType())) {
			return false;
		}
		if ("myselffavorites".equalsIgnoreCase(collectParam.getMyself() + collectParam.getType())
				|| "otherfavorites".equalsIgnoreCase(collectParam.getMyself() + collectParam.getType())) {
			if (null == collectParam.getFavoritesId()) {
				return false;
			}
		}
		return true;
	}

	public static boolean checkEditCollectParam(CollectParam collectParam) {
		if (null == collectParam || null == collectParam.getUserId() || StringUtils.isBlank(collectParam.getTitle())||
				(StringUtils.isBlank(collectParam.getNewFavorites()) && null == collectParam.getFavoritesId())||
				StringUtils.isBlank(collectParam.getCollectType()) || StringUtils.isBlank(collectParam.getUrl())) {
			return false;
		}
		if ("save".equalsIgnoreCase(collectParam.getType())) {
			if (null != collectParam.getId()) {
				return false;
			}
		}
		if ("update".equalsIgnoreCase(collectParam.getType())) {
			if (null == collectParam.getId()) {
				return false;
			}
		}
		return true;
	}

}
