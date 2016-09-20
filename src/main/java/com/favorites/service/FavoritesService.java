package com.favorites.service;

import java.util.List;

import com.favorites.domain.Favorites;

public interface FavoritesService {
	
	public Favorites saveFavorites(Long userId,Long count,String name);
	
	public List<Favorites> getUserFavorites(Long userId, String myself);
	
	public Long getFavoritesId(Long userId,String name);

}
