package com.favorites.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.favorites.domain.CollectSummary;
import com.favorites.domain.result.CollectDetailResult;
import com.favorites.param.CollectParam;

public interface CollectService {
	
	public List<CollectSummary> getCollects(Pageable pageable,CollectParam collectParam);
	
	public void editCollect(CollectParam collectParam);
	
	public boolean checkCollect(CollectParam collectParam);
	
	public CollectDetailResult getCollectDetail(Long id);
	
	public void delCollectById(Long id,Long userId);
	
	public void likeAndUnlike(Long collectId,Long userId);
	
	public void changePrivacy(Long collectId,Long userId);

}
