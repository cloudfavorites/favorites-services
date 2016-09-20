package com.favorites.param;

import org.apache.commons.lang3.StringUtils;

public class CommentParam extends BaseParam{

	private Long collectId;
	private String content;
	private Long userId;
	public Long getCollectId() {
		return collectId;
	}
	public void setCollectId(Long collectId) {
		this.collectId = collectId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public static boolean checkParam(CommentParam commentParam){
		if(null == commentParam || null == commentParam.getCollectId() || null == commentParam.getUserId() ||
				StringUtils.isBlank(commentParam.getContent())){
			return false;
		}
		return true;
	}
	
}
