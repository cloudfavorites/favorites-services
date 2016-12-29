package com.favorites.web;

import com.favorites.comm.aop.LoggerManage;
import com.favorites.domain.CollectSummary;
import com.favorites.domain.result.CollectDetailResult;
import com.favorites.domain.result.ExceptionMsg;
import com.favorites.domain.result.Response;
import com.favorites.domain.result.ResponseData;
import com.favorites.param.CollectParam;
import com.favorites.param.SearchParam;
import com.favorites.service.CollectService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/collect")
public class CollectController extends BaseController{
	
	@Resource
	private CollectService collectService;
	
	@RequestMapping(value="/getCollectList",method=RequestMethod.POST)
	@LoggerManage(description="获取文章列表")
	public ResponseData getCollectList(CollectParam collectParam){
		Boolean check = CollectParam.checkParam(collectParam);
		if(!check){
			return new ResponseData(ExceptionMsg.ParamError);
		}
		Sort sort = new Sort(Direction.DESC, "id");
	    try {
	    	Pageable pageable = new PageRequest(collectParam.getPage(), collectParam.getSize(), sort);
		    List<CollectSummary> collectList = collectService.getCollects(pageable, collectParam);
			return new ResponseData(ExceptionMsg.SUCCESS,collectList);
		} catch (Exception e) {
			logger.error("获取文章了列表异常：",e);
			return new ResponseData(ExceptionMsg.FAILED);
		}
	}

	@RequestMapping(value="/getExploreCollectList",method=RequestMethod.POST)
	@LoggerManage(description = "发现页面文章获取")
	public ResponseData getExploreCollectList(CollectParam collectParam){
		if(null == collectParam || null == collectParam.getPage()){
			return new ResponseData(ExceptionMsg.ParamError);
		}
		Sort sort = new Sort(Direction.DESC, "id");
		collectParam.setType("explore");
		collectParam.setMyself("myself");
		try {
			Pageable pageable = new PageRequest(collectParam.getPage(), collectParam.getSize(), sort);
			List<CollectSummary> collectList = collectService.getCollects(pageable, collectParam);
			return new ResponseData(ExceptionMsg.SUCCESS,collectList);
		} catch (Exception e) {
			logger.error("获取文章了列表异常：",e);
			return new ResponseData(ExceptionMsg.FAILED);
		}
	}
	
	@RequestMapping(value="/editCollect",method=RequestMethod.POST)
	@LoggerManage(description="文章编辑(修改或新增)")
	public Response editCollect(CollectParam collectParam){
		Boolean check = CollectParam.checkEditCollectParam(collectParam);
		if(!check){
			return result(ExceptionMsg.ParamError);
		}
		if(!collectService.checkCollect(collectParam)){
			return result(ExceptionMsg.CollectExist);
		}
		try {
			collectService.editCollect(collectParam);
		} catch (Exception e) {
			logger.error("文章编辑(修改或新增)异常：",e);
			return result(ExceptionMsg.FAILED);
		}
		return result(ExceptionMsg.SUCCESS);
	}
	
	@RequestMapping(value="/getCollectById",method=RequestMethod.POST)
	@LoggerManage(description="文章获取(根据ID)")
	public ResponseData getCollectById(Long id){
		if(null == id){
			return new ResponseData(ExceptionMsg.ParamError);
		}
		try {
			CollectDetailResult result = collectService.getCollectDetail(id);
			return new ResponseData(ExceptionMsg.SUCCESS,result);
		} catch (Exception e) {
			logger.error("文章获取（根据ID）异常：",e);
			return new ResponseData(ExceptionMsg.FAILED);
		}
	}
	
	@RequestMapping(value="/delCollect",method=RequestMethod.POST)
	@LoggerManage(description="文章删除")
	public Response delCollect(Long userId,Long id) {
		if(null == id || null == userId){
			return result(ExceptionMsg.ParamError);
		}
		try {
			collectService.delCollectById(id, userId);
			return result(ExceptionMsg.SUCCESS);
		} catch (Exception e) {
			logger.error("删除文章异常：",e);
			return result(ExceptionMsg.FAILED);
		}
	}
	
	@RequestMapping(value="/likeAndUnlike",method=RequestMethod.POST)
	@LoggerManage(description="点赞&取消点赞")
	public Response likeAndUnlike(Long collectId,Long userId){
		if(null == collectId || null == userId){
			return result(ExceptionMsg.ParamError);
		}
		try {
			collectService.likeAndUnlike(collectId, userId);
		} catch (Exception e) {
			logger.error("点赞&取消点赞异常：",e);
			return result(ExceptionMsg.FAILED);
		}
		return result();
	}
	
	@RequestMapping(value="/changePrivacy",method=RequestMethod.POST)
	@LoggerManage(description="文章公开私密修改")
	public Response changePrivacy(Long collectId,Long userId){
		if(null == collectId || null == userId){
			return result(ExceptionMsg.ParamError);
		}
		try {
			collectService.changePrivacy(collectId, userId);
		} catch (Exception e) {
			logger.error("文章公开私密修改异常：",e);
			return result(ExceptionMsg.FAILED);
		}
		return result();
	}

	@RequestMapping(value="/search",method=RequestMethod.POST)
	@LoggerManage(description="搜索")
	public ResponseData search(SearchParam searchParam) {
		Sort sort = new Sort(Direction.DESC, "id");
		try {
			Pageable pageable = new PageRequest(searchParam.getPage(), searchParam.getSize(), sort);
			List<CollectSummary> collectList = null;
			if("myself".equals(searchParam.getMyself())){
				collectList = collectService.searchMy(searchParam.getUserId(),searchParam.getKey() ,pageable);
			}else{
				collectList = collectService.searchOther(searchParam.getUserId(), searchParam.getKey(), pageable);
			}
		    return new ResponseData(ExceptionMsg.SUCCESS,collectList);
		} catch (Exception e) {
			logger.error("搜索异常：",e);
			return new ResponseData(ExceptionMsg.FAILED);
		}  
	}
}
