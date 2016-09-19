package com.favorites.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.favorites.comm.utils.DateUtils;
import com.favorites.domain.Favorites;
import com.favorites.domain.FavoritesRepository;
import com.favorites.service.FavoritesService;

@Service("favoritesService")
public class FavoritesServiceImpl implements FavoritesService{
	
	@Autowired
	private FavoritesRepository favoritesRepository;
	
	/**
	 * 保存
	 * @param userId
	 * @param count
	 * @param name
	 * @return
	 */
	public Favorites saveFavorites(Long userId,Long count,String name){
		Favorites favorites = new Favorites();
		favorites.setName(name);
		favorites.setUserId(userId);
		favorites.setCount(count);
		favorites.setCreateTime(DateUtils.getCurrentTime());
		favorites.setLastModifyTime(DateUtils.getCurrentTime());
		favoritesRepository.save(favorites);
		return favorites;
	}
	
	public Long getFavoritesId(Long userId,String name){
		Favorites favorites = favoritesRepository.findByUserIdAndName(userId, name);
		if(null != favorites && null != favorites.getId()){
			favoritesRepository.increaseCountById(favorites.getId(),DateUtils.getCurrentTime());
		}else{
			favorites = saveFavorites(userId, 1l,name);
		}
		return favorites.getId();
	}

}
