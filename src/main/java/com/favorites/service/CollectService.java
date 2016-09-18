package com.favorites.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.favorites.domain.CollectSummary;
import com.favorites.param.CollectParam;

public interface CollectService {
	
	public List<CollectSummary> getCollects(Pageable pageable,CollectParam collectParam);

}
