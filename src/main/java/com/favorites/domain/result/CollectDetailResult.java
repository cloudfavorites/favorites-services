package com.favorites.domain.result;

import java.util.List;

public class CollectDetailResult {

	private String title;
	private String description;
	private String logoUrl;
	private String remark;
	private String type;
	private Long favoritesId;
	private String favoritesName;
	private List<String> atFriends;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getFavoritesId() {
		return favoritesId;
	}

	public void setFavoritesId(Long favoritesId) {
		this.favoritesId = favoritesId;
	}

	public String getFavoritesName() {
		return favoritesName;
	}

	public void setFavoritesName(String favoritesName) {
		this.favoritesName = favoritesName;
	}

	public List<String> getAtFriends() {
		return atFriends;
	}

	public void setAtFriends(List<String> atFriends) {
		this.atFriends = atFriends;
	}

}
