package com.favorites.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.favorites.comm.utils.DateUtils;
import com.favorites.domain.CollectRepository;
import com.favorites.domain.Favorites;
import com.favorites.domain.FavoritesRepository;
import com.favorites.domain.enums.CollectType;
import com.favorites.domain.enums.IsDelete;
import com.favorites.service.FavoritesService;

@Service("favoritesService")
public class FavoritesServiceImpl implements FavoritesService{
	
	@Autowired
	private FavoritesRepository favoritesRepository;
	@Autowired
	private CollectRepository collectRepository;
	
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

	/**
	 * 获取收藏夹
	 * @param userId
	 * @param myself
	 * @return
	 */
	@Override
	public List<Favorites> getUserFavorites(Long userId, String myself) {
		List<Favorites> favoritesList = favoritesRepository.findByUserId(userId);
		if(!"myself".equals(myself)){
			for(Favorites favorites:favoritesList){
				favorites.setCount(collectRepository.countByFavoritesIdAndTypeAndIsDelete(favorites.getId(), CollectType.PUBLIC, IsDelete.NO));
			}
		}
		return favoritesList;
	}

}
