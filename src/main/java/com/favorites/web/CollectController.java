package com.favorites.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.favorites.comm.aop.LoggerManage;
import com.favorites.domain.CollectSummary;
import com.favorites.domain.result.ExceptionMsg;
import com.favorites.domain.result.ResponseData;
import com.favorites.param.CollectParam;
import com.favorites.service.CollectService;

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

}
