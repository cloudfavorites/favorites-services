package com.favorites.domain.result;

public class UserInformationResult {
	
	private String userName;// 用户名
	private String introduction;// 简介
	private String email;// 邮箱
	private String profilePicture;// 头像
	private Long collectCount;// 收藏数
	private Long followedCount;// 粉丝数
	private Long followCount;// 关注数
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getProfilePicture() {
		return profilePicture;
	}
	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}
	public Long getCollectCount() {
		return collectCount;
	}
	public void setCollectCount(Long collectCount) {
		this.collectCount = collectCount;
	}
	public Long getFollowedCount() {
		return followedCount;
	}
	public void setFollowedCount(Long followedCount) {
		this.followedCount = followedCount;
	}
	public Long getFollowCount() {
		return followCount;
	}
	public void setFollowCount(Long followCount) {
		this.followCount = followCount;
	}
	

}
