package com.favorites.web;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.favorites.comm.aop.LoggerManage;
import com.favorites.domain.CollectRepository;
import com.favorites.domain.Config;
import com.favorites.domain.ConfigRepository;
import com.favorites.domain.Favorites;
import com.favorites.domain.FavoritesRepository;
import com.favorites.domain.result.ExceptionMsg;
import com.favorites.domain.result.Response;
import com.favorites.domain.result.ResponseData;
import com.favorites.service.FavoritesService;
import com.favorites.comm.utils.DateUtils;

@RestController
@RequestMapping("/favorites")
public class FavoritesController extends BaseController{
	
	@Autowired
	private FavoritesRepository favoritesRepository;
	@Resource
	private FavoritesService favoritesService;
	@Autowired
	private CollectRepository collectRepository;
	@Autowired
	private ConfigRepository configRespository;
	
	/**
	 * 新建收藏夹
	 * @param userId
	 * @param favoritesName
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@LoggerManage(description="新建收藏夹")
	public Response addFavorites(Long userId,String favoritesName){
		if(null == userId || StringUtils.isBlank(favoritesName)){
			return new ResponseData(ExceptionMsg.ParamError);
		}
		Favorites favorites = favoritesRepository.findByUserIdAndName(userId, favoritesName);
		if(null != favorites){
			logger.info("收藏夹名称已被创建");
			return result(ExceptionMsg.FavoritesNameUsed);
		}else{
			try {
				favoritesService.saveFavorites(userId, 0l, favoritesName);
			} catch (Exception e) {
				logger.error("新建收藏夹异常：",e);
				return result(ExceptionMsg.FAILED);
			}
		}
		return result();
	}
	
	/**
	 * 修改收藏夹
	 * @param userId
	 * @param favoritesName
	 * @param favoritesId
	 * @return
	 */
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@LoggerManage(description="修改收藏夹")
	public Response updateFavorites(Long userId,String favoritesName,Long favoritesId){
		if(null == userId || StringUtils.isBlank(favoritesName) || null == favoritesId){
			return new ResponseData(ExceptionMsg.ParamError);
		}
		Favorites fav = favoritesRepository.findOne(favoritesId);
		if(null != fav){
			if(userId == fav.getUserId().longValue()){
				Favorites favorites = favoritesRepository.findByUserIdAndName(userId, favoritesName);
				if(null != favorites){
					logger.info("收藏夹名称已被创建");
					return result(ExceptionMsg.FavoritesNameUsed);
				}else{
					try {
						favoritesRepository.updateNameById(favoritesId, DateUtils.getCurrentTime(), favoritesName);
					} catch (Exception e) {
						logger.error("修改收藏夹异常：",e);
					}
				}
			}else{
				logger.info("收藏夹不为该用户所有");
				return result(ExceptionMsg.FavoritesNotUsers);
			}			
		}else{
			logger.info("收藏夹不存在");
			return result(ExceptionMsg.FavoritesNotExist);
		}
		return result();
	}
	
	/**
	 * 删除收藏夹
	 * @param userId
	 * @param favoritesId
	 * @return
	 */
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	@LoggerManage(description="删除收藏夹")
	public Response delFavorites(Long userId,Long favoritesId){
		if(null == userId || null == favoritesId){
			return new ResponseData(ExceptionMsg.ParamError);
		}
		try {
			Favorites fav = favoritesRepository.findOne(favoritesId);
			if(null != fav){
				if(userId == fav.getUserId().longValue()){
					favoritesRepository.delete(favoritesId);
					// 删除该收藏夹下文章
					collectRepository.deleteByFavoritesId(favoritesId);
					Config config = configRespository.findByUserIdAndDefaultFavorties(userId,String.valueOf(favoritesId));
					if(null != config){
						// 默认收藏夹被删除，设置“未读列表”为默认收藏夹
						Favorites favorites = favoritesRepository.findByUserIdAndName(userId, "未读列表");
						if(null != favorites){
							configRespository.updateFavoritesById(config.getId(), String.valueOf(favorites.getId()), DateUtils.getCurrentTime());
						}
					}
				}else{
					logger.info("收藏夹不为该用户所有");
					return result(ExceptionMsg.FavoritesNotUsers);
				}			
			}else{
				logger.info("收藏夹不存在");
				return result(ExceptionMsg.FavoritesNotExist);
			}		
		} catch (Exception e) {
			logger.error("删除收藏夹异常：",e);
		}
		return result();
	}
	
	/**
	 * 获取收藏夹
	 * @param userId
	 * @param myself
	 * @return
	 */
	@RequestMapping(value="/getFavorites",method=RequestMethod.POST)
	@LoggerManage(description="获取收藏夹")
	public ResponseData getFavorites(Long userId, String myself){
		if(null == userId || StringUtils.isBlank(myself)){
			return new ResponseData(ExceptionMsg.ParamError);
		}
		try {
			List<Favorites> favoritesList = favoritesService.getUserFavorites(userId, myself);
			return new ResponseData(ExceptionMsg.SUCCESS,favoritesList);
		} catch (Exception e) {
			logger.error("获取收藏夹异常：",e);
			return new ResponseData(ExceptionMsg.FAILED);
		}
	}

}
