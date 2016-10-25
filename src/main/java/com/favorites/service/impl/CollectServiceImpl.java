package com.favorites.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.favorites.comm.utils.DateUtils;
import com.favorites.comm.utils.StringUtil;
import com.favorites.domain.Collect;
import com.favorites.domain.CollectRepository;
import com.favorites.domain.CollectSummary;
import com.favorites.domain.CollectView;
import com.favorites.domain.CommentRepository;
import com.favorites.domain.Favorites;
import com.favorites.domain.FavoritesRepository;
import com.favorites.domain.FollowRepository;
import com.favorites.domain.Praise;
import com.favorites.domain.PraiseRepository;
import com.favorites.domain.User;
import com.favorites.domain.UserRepository;
import com.favorites.domain.enums.CollectType;
import com.favorites.domain.enums.IsDelete;
import com.favorites.domain.result.CollectDetailResult;
import com.favorites.param.CollectParam;
import com.favorites.service.CollectService;
import com.favorites.service.FavoritesService;
import com.favorites.service.NoticeService;

@Service("collectService")
public class CollectServiceImpl implements CollectService{
	protected Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private PraiseRepository praiseRepository;
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private FollowRepository followRepository;
	@Autowired
	private CollectRepository collectRepository;
	@Resource
	private FavoritesService favoritesService;
	@Autowired
	private FavoritesRepository favoritesRepository;
	@Autowired
	private UserRepository userRepository;
	@Resource
	private NoticeService noticeService;
	
	
	/**
	 * 文章列表获取
	 */
	public List<CollectSummary> getCollects(Pageable pageable,CollectParam collectParam) {
		Page<CollectView> views = null;
		if ("myselfhome".equalsIgnoreCase(collectParam.getMyself() + collectParam.getType())) {
			List<Long> userIds=followRepository.findMyFollowIdByUserId(collectParam.getUserId());
			if(userIds==null || userIds.size()==0){
				views = collectRepository.findViewByUserId(collectParam.getUserId(), pageable);
			}else{
				views = collectRepository.findViewByUserIdAndFollows(collectParam.getUserId(), userIds, pageable);
			}
		}else if("myselfcenterpage".equalsIgnoreCase(collectParam.getMyself() + collectParam.getType())){
			views = collectRepository.findViewByUserId(collectParam.getUserId(), pageable);
		} else if ("myselfexplore".equalsIgnoreCase(collectParam.getMyself()+collectParam.getType())) {
			views = collectRepository.findExploreView(collectParam.getUserId(),pageable);
		} else if("otherscenterpage".equalsIgnoreCase(collectParam.getType())){
			views = collectRepository.findViewByUserIdAndType(collectParam.getUserId(), pageable, CollectType.PUBLIC);
		} else if("othersfavorites".equalsIgnoreCase(collectParam.getType())){
			views = collectRepository.findViewByUserIdAndTypeAndFavoritesId(collectParam.getUserId(), pageable, CollectType.PUBLIC, collectParam.getFavoritesId());
		} else if("myselfgarbage".equalsIgnoreCase(collectParam.getMyself() + collectParam.getType())){
			views = collectRepository.findViewByUserIdAndIsDelete(collectParam.getUserId(), pageable);
		}else if("myselffavorites".equalsIgnoreCase(collectParam.getMyself() + collectParam.getType())){
			views = collectRepository.findViewByFavoritesId(collectParam.getFavoritesId(), pageable);
		}
		return convertCollect(views,collectParam.getUserId());
	}
	
	/**
	 * @author neo
	 * @date 2016年8月11日
	 * @param collects
	 * @return
	 */
	private List<CollectSummary> convertCollect(Page<CollectView> views,Long userId) {
		List<CollectSummary> summarys=new ArrayList<CollectSummary>();
		for (CollectView view : views) {
			CollectSummary summary=new CollectSummary(view);
			summary.setPraiseCount(praiseRepository.countByCollectId(view.getId()));
			summary.setCommentCount(commentRepository.countByCollectId(view.getId()));
			Praise praise=praiseRepository.findByUserIdAndCollectId(userId, view.getId());
			if(praise!=null){
				summary.setPraise(true);
			}else{
				summary.setPraise(false);
			}
			summary.setCollectTime(DateUtils.getTimeFormatText(view.getLastModifyTime()));
			summarys.add(summary);
		}
		return summarys;
	}
	
	/**
	 * 文章编辑（保存或修改）
	 */
	public void editCollect(CollectParam collectParam){
		Long favoritesId = null;
		Collect collect = null;
		if(null == collectParam.getId()){
			collect = new Collect();
			// 保存
			if(StringUtils.isNotBlank(collectParam.getNewFavorites())){
				favoritesId = favoritesService.getFavoritesId(collectParam.getUserId(), collectParam.getNewFavorites());
			}else{
				favoritesRepository.increaseCountById(collectParam.getFavoritesId(), DateUtils.getCurrentTime());
				favoritesId = collectParam.getFavoritesId();
			}
			collect.setIsDelete(IsDelete.NO);
			collect.setCreateTime(DateUtils.getCurrentTime());
		}else{
			collect = collectRepository.findOne(collectParam.getId());
			if(collectParam.getUserId().longValue() != collect.getUserId().longValue()){
				logger.info("当前用户ID:" + collectParam.getUserId() + "与文章所属用户Id：" + collect.getUserId() + "不等，不进行修改操作");
				return;
			}
			// 修改
			if(StringUtils.isNotBlank(collectParam.getNewFavorites())){
				favoritesId = favoritesService.getFavoritesId(collectParam.getUserId(), collectParam.getNewFavorites());
			}else if(collectParam.getFavoritesId().longValue() != collect.getId().longValue() && !IsDelete.YES.equals(collect.getIsDelete())){
				favoritesRepository.increaseCountById(collectParam.getFavoritesId(), DateUtils.getCurrentTime());
				favoritesRepository.reduceCountById(collect.getFavoritesId(), DateUtils.getCurrentTime());
				favoritesId = collectParam.getFavoritesId();
			}
			if(IsDelete.YES.equals(collect.getIsDelete())){
				// 由回收站移除
				collect.setIsDelete(IsDelete.NO);
				if(null == favoritesId){
					favoritesId = collectParam.getFavoritesId();
					favoritesRepository.increaseCountById(collectParam.getFavoritesId(), DateUtils.getCurrentTime());
				}
			}
		}
		collect.setFavoritesId(favoritesId);
		if(CollectType.PUBLIC.toString().equalsIgnoreCase(collectParam.getCollectType())){
			collect.setType(CollectType.PUBLIC);
		}else{
			collect.setType(CollectType.PRIVATE);
		}
		collect.setCharset(collectParam.getCharset());
		collect.setTitle(collectParam.getTitle());
		collect.setUserId(collectParam.getUserId());
		if(StringUtils.isNotBlank(collectParam.getDescription())){
			collect.setDescription(collectParam.getDescription());
		}else{
			collect.setDescription(collectParam.getTitle());
		}
		collect.setLogoUrl(collectParam.getLogoUrl());
		collect.setRemark(collectParam.getRemark());
		collect.setUrl(collectParam.getUrl());
		collect.setLastModifyTime(DateUtils.getCurrentTime());
		collectRepository.save(collect);
		noticeFriends(collect);
	}
	
	/**
	 * 添加消息通知
	 * @param collect
	 */
	private void noticeFriends(Collect collect){
		if (StringUtils.isNotBlank(collect.getRemark())&& collect.getRemark().indexOf("@") > -1) {
			List<String> atUsers = StringUtil.getAtUser(collect.getRemark());
			for (String str : atUsers) {
				logger.info("用户名：" + str);
				User user = userRepository.findByUserName(str);
				if (null != user) {
					// 保存消息通知
					noticeService.saveNotice(String.valueOf(collect.getId()),"at", user.getId(), null);
				} else {
					logger.info("为找到匹配：" + str + "的用户.");
				}
			}
		}
	}

	/**
	 * 验证是否重复收藏
	 * @param collect
	 * @param userId
	 * @return
	 */
	public boolean checkCollect(CollectParam collectParam){
		if(StringUtils.isNotBlank(collectParam.getNewFavorites())){
			// url+favoritesId+userId
			Favorites favorites = favoritesRepository.findByUserIdAndName(collectParam.getUserId(), collectParam.getNewFavorites());
			if(null == favorites){
				return true;
			}else{
				List<Collect> list = collectRepository.findByFavoritesIdAndUrlAndUserIdAndIsDelete(favorites.getId(), collectParam.getUrl(), collectParam.getUserId(), IsDelete.NO);
				if(null != list && list.size() > 0){
					return false;
				}else{
					return true;
				}
			}
		}else{
			if(null != collectParam.getId()){
				Collect collect = collectRepository.findOne(collectParam.getId());
				if(collectParam.getFavoritesId().longValue() != collect.getFavoritesId().longValue()){
					List<Collect> list = collectRepository.findByFavoritesIdAndUrlAndUserIdAndIsDelete(collectParam.getFavoritesId(), collectParam.getUrl(), collectParam.getUserId(),IsDelete.NO);
					if(null != list && list.size() > 0){
						return false;
					}else{
						return true;
					}
				}else{
					return true;
				}
			}else{
				List<Collect> list = collectRepository.findByFavoritesIdAndUrlAndUserIdAndIsDelete(collectParam.getFavoritesId(), collectParam.getUrl(), collectParam.getUserId(),IsDelete.NO);
				if(null != list && list.size() > 0){
					return false;
				}else{
					return true;
				}
			}
		}
	}
	
	/**
	 * 获取文章详情
	 * @param id
	 * @return
	 */
	public CollectDetailResult getCollectDetail(Long id){
		CollectDetailResult result = new CollectDetailResult();
		Collect collect = collectRepository.findOne(id);
		if(null != collect){
			BeanUtils.copyProperties(collect, result);
			List<String> atFriends = followRepository.findByUserId(collect.getUserId());
			result.setAtFriends(atFriends);
			result.setType(collect.getType().toString());
			Favorites favorites = favoritesRepository.findOne(collect.getFavoritesId());
			if(null != favorites){
				result.setFavoritesName(favorites.getName());
			}
		}
		return result;
	}
	
	/**
	 * 删除文章
	 * @param id
	 * @param userId
	 */
	public void delCollectById(Long id,Long userId){
		Collect collect = collectRepository.findOne(id);
		if(null != collect && userId.longValue() == collect.getUserId().longValue()){
		  collectRepository.deleteById(id);
		  if(null != collect.getFavoritesId() && !IsDelete.YES.equals(collect.getIsDelete())){
			 favoritesRepository.reduceCountById(collect.getFavoritesId(), DateUtils.getCurrentTime());
		  }
		}
	}
	
	/**
	 * 点赞与取消点赞
	 * @param collectId
	 * @param userId
	 */
	public void likeAndUnlike(Long collectId,Long userId){
		Praise praise=praiseRepository.findByUserIdAndCollectId(userId, collectId);
		if(null == praise){
			Praise newPraise=new Praise();
			newPraise.setUserId(userId);
			newPraise.setCollectId(collectId);
			newPraise.setCreateTime(DateUtils.getCurrentTime());
			praiseRepository.save(newPraise);
			// 保存消息通知
			Collect collect = collectRepository.findOne(collectId);
			if(null != collect){
				noticeService.saveNotice(String.valueOf(collectId), "praise", collect.getUserId(), String.valueOf(newPraise.getId()));
			}
		}else if(praise.getUserId().longValue() == userId.longValue()){
			praiseRepository.delete(praise.getId());
		}
	}
	
	/**
	 * 文章 公开私密 修改
	 * @param collectId
	 * @param userId
	 */
	public void changePrivacy(Long collectId,Long userId){
		Collect collect = collectRepository.findOne(collectId);
		if(null != collect && userId.longValue() == collect.getUserId().longValue()){
			if(CollectType.PRIVATE.equals(collect.getType())){
				collectRepository.modifyByIdAndUserId(CollectType.PUBLIC, collectId, userId);
			}else if(CollectType.PUBLIC.equals(collect.getType())){
				collectRepository.modifyByIdAndUserId(CollectType.PRIVATE, collectId, userId);
			}
		}
	}
	
	@Override
	public List<CollectSummary> searchMy(Long userId,String key,Pageable pageable) {
		// TODO Auto-generated method stub
		Page<CollectView> views = collectRepository.searchMyByKey(userId,"%"+key+"%",pageable);
		return convertCollect(views,userId);
	}
	
	@Override
	public List<CollectSummary> searchOther(Long userId,String key,Pageable pageable) {
		// TODO Auto-generated method stub
		Page<CollectView> views = collectRepository.searchOtherByKey(userId, "%"+key+"%", pageable);
		return convertCollect(views,userId);
	}
}
