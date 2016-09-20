package com.favorites.service;

import java.util.List;

import com.favorites.domain.Comment;
import com.favorites.domain.result.CommentResult;
import com.favorites.param.CommentParam;

public interface CommentService {
	
	public void addComment(CommentParam commentParam);
	
	public List<CommentResult> getCommentList(Long collectId);

}
