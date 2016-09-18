package com.favorites.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.favorites.comm.utils.DateUtils;
import com.favorites.domain.CollectRepository;
import com.favorites.domain.CollectSummary;
import com.favorites.domain.CollectView;
import com.favorites.domain.CommentRepository;
import com.favorites.domain.FollowRepository;
import com.favorites.domain.Praise;
import com.favorites.domain.PraiseRepository;
import com.favorites.domain.enums.CollectType;
import com.favorites.param.CollectParam;
import com.favorites.service.CollectService;

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
	
	public List<CollectSummary> getCollects(Pageable pageable,CollectParam collectParam) {
		// TODO Auto-generated method stub
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

}
