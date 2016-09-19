package com.favorites.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.favorites.domain.CollectRepository;
import com.favorites.domain.FollowRepository;
import com.favorites.domain.User;
import com.favorites.domain.UserRepository;
import com.favorites.domain.enums.CollectType;
import com.favorites.domain.enums.FollowStatus;
import com.favorites.domain.enums.IsDelete;
import com.favorites.domain.result.ExceptionMsg;
import com.favorites.domain.result.ResponseData;
import com.favorites.domain.result.UserInformationResult;
import com.favorites.param.UserParam;
import com.favorites.service.UserService;
@Service("userService")
public class UserServiceImpl implements UserService{
	
	protected Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CollectRepository collectRespository;
	@Autowired
	private FollowRepository followRespository;
	
	/**
	 * 个人信息修改（用户名&简介）
	 */
	public ResponseData updateUserInfo(UserParam userParam){
		if(userParam.getNewUserName().length() > 12){
			return new ResponseData(ExceptionMsg.UserNameLengthLimit);
		}
		User user = userRepository.findOne(userParam.getUserId());
		if(null == user){
			return new ResponseData(ExceptionMsg.UserNotExist);
		}
		if(!user.getUserName().equals(userParam.getNewUserName())){
			User newUser = userRepository.findByUserName(userParam.getNewUserName());
			if(null != newUser){
				return new ResponseData(ExceptionMsg.UserNameUsed);
			}
			userRepository.setUserName(userParam.getNewUserName(), user.getEmail());
		}
		if(!userParam.getIntroduction().equals(user.getIntroduction())){
			userRepository.setIntroduction(userParam.getIntroduction(), user.getEmail());
		}
		Map<String, String> resultMap = new HashMap<>();
		resultMap.put("introduction", userParam.getIntroduction());
		resultMap.put("newUserName", userParam.getNewUserName());
		return new ResponseData(ExceptionMsg.SUCCESS,resultMap);
	}
	
	/**
	 * 用户个人信息获取
	 * @param ret
	 * @param userParam
	 * @return
	 */
	public ResponseData getUserInformation(UserInformationResult ret,UserParam userParam){
		if("myself".equalsIgnoreCase(userParam.getMyself())){
			// 查看自己的个人主页
			Long collectCount = collectRespository.countByUserIdAndIsDelete(userParam.getUserId(), IsDelete.NO);
			ret.setCollectCount(collectCount);
		}else{
			// 查看别人的个人主页
			ret.setEmail("");
			Long collectCount = collectRespository.countByUserIdAndIsDeleteAndType(userParam.getUserId(), IsDelete.NO, CollectType.PUBLIC);
			ret.setCollectCount(collectCount);
		}
		Long followCount = followRespository.countByUserIdAndStatus(userParam.getUserId(), FollowStatus.FOLLOW);
		ret.setFollowCount(followCount);
		Long followedCount = followRespository.countByFollowIdAndStatus(userParam.getUserId(), FollowStatus.FOLLOW);
		ret.setFollowedCount(followedCount);
		return new ResponseData(ExceptionMsg.SUCCESS,ret);
	}

}
