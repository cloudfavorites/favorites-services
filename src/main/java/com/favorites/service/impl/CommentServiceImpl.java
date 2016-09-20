package com.favorites.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.favorites.comm.utils.DateUtils;
import com.favorites.comm.utils.StringUtil;
import com.favorites.domain.Collect;
import com.favorites.domain.CollectRepository;
import com.favorites.domain.Comment;
import com.favorites.domain.CommentRepository;
import com.favorites.domain.User;
import com.favorites.domain.UserRepository;
import com.favorites.domain.result.CommentResult;
import com.favorites.param.CommentParam;
import com.favorites.service.CommentService;
import com.favorites.service.NoticeService;

@Service("commentService")
public class CommentServiceImpl implements CommentService{
	protected Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CommentRepository commentRepository;
	@Resource
	private NoticeService noticeService;
	@Autowired
	private CollectRepository collectRepository;
	@Value("${favorites.web.path}")
	private String webPath;

	/**
	 * 添加评论&回复评论
	 * @param commentParam
	 */
	public void addComment(CommentParam commentParam){
		User user = null;
		Comment comment = new Comment(); 
		comment.setContent(commentParam.getContent());
		if (commentParam.getContent().indexOf("@") > -1) {
			List<String> atUsers = StringUtil.getAtUser(commentParam.getContent());
			if(atUsers!=null && atUsers.size()>0){
				 user = userRepository.findByUserName(atUsers.get(0));
				if (null != user) {
					comment.setReplyUserId(user.getId());
				} else {
					logger.info("为找到匹配：" + atUsers.get(0) + "的用户.");
				}
				String content=comment.getContent().substring(0,comment.getContent().indexOf("@"));
				if(StringUtils.isBlank(content)){
					content=comment.getContent().substring(comment.getContent().indexOf("@")+user.getUserName().length()+1,comment.getContent().length());
				}
				comment.setContent(content);
			}
		}
		comment.setUserId(commentParam.getUserId());
		comment.setCreateTime(DateUtils.getCurrentTime());
		comment.setCollectId(commentParam.getCollectId());
		commentRepository.save(comment);
		if(null != user){
			// 保存消息通知(回复)
			noticeService.saveNotice(String.valueOf(comment.getCollectId()), "comment", user.getId(), String.valueOf(comment.getId()));
		}else{
			// 保存消息通知（直接评论）
			Collect collect = collectRepository.findOne(comment.getCollectId());
			if(null != collect){
				noticeService.saveNotice(String.valueOf(comment.getCollectId()), "comment", collect.getUserId(), String.valueOf(comment.getId()));
			}
		}
	}
	
	/**
	 * 获取评论列表
	 * @param collectId
	 * @return
	 */
	public List<CommentResult> getCommentList(Long collectId){
		List<Comment> comments= commentRepository.findByCollectIdOrderByIdDesc(collectId);
		return convertComment(comments);
	}
	
	private List<CommentResult> convertComment(List<Comment> comments) {
		List<CommentResult> results = new ArrayList<>();
		for (Comment comment : comments) {
			CommentResult result = new CommentResult();
			result.setId(comment.getId());
			User user = userRepository.findOne(comment.getUserId());
			result.setCommentTime(DateUtils.getTimeFormatText(comment.getCreateTime()));
			result.setUserName(user.getUserName());
			result.setProfilePicture(webPath+user.getProfilePicture());
			result.setContent(comment.getContent());
			if(comment.getReplyUserId()!=null){
		     User replyUser = userRepository.findOne(comment.getReplyUserId());
		     result.setReplyUserName(replyUser.getUserName());
			}
			results.add(result);
		}
		return results;
	}
}
