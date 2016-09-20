package com.favorites.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.favorites.comm.aop.LoggerManage;
import com.favorites.domain.Comment;
import com.favorites.domain.CommentRepository;
import com.favorites.domain.result.CommentResult;
import com.favorites.domain.result.ExceptionMsg;
import com.favorites.domain.result.Response;
import com.favorites.domain.result.ResponseData;
import com.favorites.param.CommentParam;
import com.favorites.service.CommentService;


@RestController
@RequestMapping("/comment")
public class CommentController extends BaseController{
	@Resource
	private CommentService commentService;
	@Autowired
	private CommentRepository commentRepository;
	
	@RequestMapping(value="/addComment",method=RequestMethod.POST)
	@LoggerManage(description="评论&回复")
	public Response addComment(CommentParam commentParam){
		boolean check = CommentParam.checkParam(commentParam);
		if(!check){
			return result(ExceptionMsg.ParamError);
		}
		try {
			commentService.addComment(commentParam);
		} catch (Exception e) {
			logger.error("评论&回复异常：",e);
			return result(ExceptionMsg.FAILED);
		}
		return result();
	}
	
	@RequestMapping(value="/getCommentList",method=RequestMethod.POST)
	@LoggerManage(description="评论列表获取")
	public ResponseData getCommentList(Long collectId){
		if(null == collectId){
			return new ResponseData(ExceptionMsg.ParamError);
		}
		try {
			List<CommentResult> commentList = commentService.getCommentList(collectId);
			return new ResponseData(ExceptionMsg.SUCCESS,commentList);
		} catch (Exception e) {
			logger.error("获取评论列表异常：",e);
			return new ResponseData(ExceptionMsg.FAILED);
		}
	}

	@RequestMapping(value="/deleteComment",method=RequestMethod.POST)
	@LoggerManage(description="删除评论")
	public Response deleteComment(Long id,Long userId) {
		if(null == id || null == userId){
			return result(ExceptionMsg.ParamError);
		}
		try {
			Comment comment = commentRepository.findOne(id);
			if(null != comment && comment.getUserId().longValue() == userId.longValue()){
				commentRepository.deleteById(id);
			}
		} catch (Exception e) {
			logger.error("删除评论异常：",e);
			return result(ExceptionMsg.FAILED);
		}
		
		return result();
	}
}
