package com.favorites.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.favorites.comm.aop.LoggerManage;
import com.favorites.domain.CollectSummary;
import com.favorites.domain.NoticeRepository;
import com.favorites.domain.result.ExceptionMsg;
import com.favorites.domain.result.ResponseData;
import com.favorites.param.NoticeParam;
import com.favorites.service.NoticeService;

/**
*@ClassName: NoticeController
*@Description: 
*@author YY 
*@date 2016年10月19日  上午10:35:04
*@version 1.0
*/

@RestController
@RequestMapping("/notice")
public class NoticeController extends BaseController{
	
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private NoticeRepository noticeRepository;
	
	/**
	 * 获取消息通知文章列表
	 * @param noticeParam
	 * @return
	 */
	@RequestMapping(value="/getNoticeCollectList")
	@LoggerManage(description="获取消息通知文章列表")
	public ResponseData getNoticeCollectList(NoticeParam noticeParam) {
		if (null == noticeParam || null == noticeParam.getPage() || null == noticeParam.getUserId() || StringUtils.isBlank(noticeParam.getType())) {
			return new ResponseData(ExceptionMsg.ParamError);
		}
		try {
			Sort sort = new Sort(Direction.DESC, "id");
		    Pageable pageable = new PageRequest(noticeParam.getPage(), noticeParam.getSize(), sort);
		    List<CollectSummary> collectList = null;
		    if("at".equals(noticeParam.getType())){
		    	collectList=noticeService.getNoticeCollects("at".toString(), noticeParam.getUserId(), pageable);
		    }else if("comment".equals(noticeParam.getType())){
		    	collectList=noticeService.getNoticeCollects("comment", noticeParam.getUserId(), pageable);
		    }else if("praise".equals(noticeParam.getType())){
		    	collectList=noticeService.getNoticeCollects("praise", noticeParam.getUserId(), pageable);
		    }else{
		    	return new ResponseData(ExceptionMsg.ParamError);
		    }
		    return new ResponseData(ExceptionMsg.SUCCESS,collectList);
		} catch (Exception e) {
			logger.error("获取消息通知文章列表异常：",e);
			return new ResponseData(ExceptionMsg.FAILED);
		}
		
	}
	
	/**
	 * 获取未读消息数量
	 * @param userId
	 * @param type
	 * @return
	 */
	@RequestMapping(value="/getUnreadNoticeCounts")
	@LoggerManage(description="获取未读消息数量")
	public ResponseData getUnreadNoticeCounts(Long userId,String type) {
		if(null == userId || StringUtils.isBlank(type)){
			return new ResponseData(ExceptionMsg.ParamError);
		}
		try {
			Long noticeCounts = noticeService.unreadNoticeCounts(userId, type);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("noticeCounts", noticeCounts);
			return new ResponseData(ExceptionMsg.SUCCESS,map);
		} catch (Exception e) {
			logger.error("获取消息通知文章列表异常：",e);
			return new ResponseData(ExceptionMsg.FAILED);
		}
	}
	
	/**
	 * 更新消息为已读
	 * @param userId
	 * @param type
	 * @return
	 */
	@RequestMapping(value="/updateNoticeReaded")
	@LoggerManage(description="更新消息为已读")
	public ResponseData updateNoticeReaded(Long userId,String type) {
		if(null == userId || StringUtils.isBlank(type)){
			return new ResponseData(ExceptionMsg.ParamError);
		}
		try {
			int counts = noticeRepository.updateReadedByUserId("read", userId, type);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("counts", counts);
			return new ResponseData(ExceptionMsg.SUCCESS, map);
		} catch (Exception e) {
			logger.error("更新消息为已读异常：",e);
			return new ResponseData(ExceptionMsg.FAILED);
		}
	}

}
