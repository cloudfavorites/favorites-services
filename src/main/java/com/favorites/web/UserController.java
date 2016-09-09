package com.favorites.web;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.favorites.comm.Const;
import com.favorites.comm.aop.LoggerManage;
import com.favorites.comm.utils.DateUtils;
import com.favorites.domain.Favorites;
import com.favorites.domain.User;
import com.favorites.domain.UserRepository;
import com.favorites.domain.result.ExceptionMsg;
import com.favorites.domain.result.LoginResult;
import com.favorites.domain.result.Response;
import com.favorites.domain.result.ResponseData;
import com.favorites.param.UserParam;
import com.favorites.service.ConfigService;
import com.favorites.service.FavoritesService;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
	@Autowired
	private UserRepository userRepository;
	@Resource
	private ConfigService configService;
	@Resource
	private FavoritesService favoritesService;
	
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseData login(User user) {
		logger.info("login begin, param is " + user);
		try {
			User loginUser = userRepository.findByUserNameOrEmail(user.getUserName(), user.getUserName());
			if (loginUser == null || !loginUser.getPassWord().equals(getPwd(user.getPassWord()))) {
				return new ResponseData(ExceptionMsg.LoginNameOrPassWordError);
			}
			getSession().setAttribute(Const.LOGIN_SESSION_KEY, loginUser);
			LoginResult ret=new LoginResult();
			ret.setUserId(loginUser.getId());
			ret.setToken(getSession().getId());
			return new ResponseData(ExceptionMsg.SUCCESS,ret);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("login failed, ", e);
			return new ResponseData(ExceptionMsg.FAILED);
		}
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public Response register(User user) {
		logger.info("create user begin, param is " + user);
		try {
			User registUser = userRepository.findByEmail(user.getEmail());
			if (null != registUser) {
				return result(ExceptionMsg.EmailUsed);
			}
			User userNameUser = userRepository.findByUserName(user.getUserName());
			if (null != userNameUser) {
				return result(ExceptionMsg.UserNameUsed);
			}
			user.setPassWord(getPwd(user.getPassWord()));
			user.setCreateTime(DateUtils.getCurrentTime());
			user.setLastModifyTime(DateUtils.getCurrentTime());
			user.setProfilePicture("img/favicon.png");
			userRepository.save(user);
			// 添加默认收藏夹
			Favorites favorites = favoritesService.saveFavorites(user.getId(),0l, "未读列表");
			// 添加默认属性设置
			configService.saveConfig(user.getId(),String.valueOf(favorites.getId()));	
			getSession().setAttribute(Const.LOGIN_SESSION_KEY, user);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("create user failed, ", e);
			return result(ExceptionMsg.FAILED);
		}
		return result();
	}

	@RequestMapping(value="/updatePassWord",method=RequestMethod.POST)
	@LoggerManage(description="密码修改")
	public Response updatePassWord(UserParam userParam){
		if(null == userParam || StringUtils.isBlank(userParam.getNewPwd())|| StringUtils.isBlank(userParam.getOldPwd())|| null == userParam.getUserId()){
			return result(ExceptionMsg.ParamError);
		}
		try {
			User user = userRepository.findOne(userParam.getUserId());
			if(null == user){
				return result(ExceptionMsg.UserNotExist);
			}
			if(!user.getPassWord().equals(getPwd(userParam.getOldPwd()))){
				return result(ExceptionMsg.OldPassWordWrong);
			}
			userRepository.setNewPassword(getPwd(userParam.getNewPwd()), user.getEmail());
		} catch (Exception e) {
			logger.error("密码修改异常：",e);
			return result(ExceptionMsg.FAILED);
		}
		return result();
	}
	
	@RequestMapping(value="/updateIntroduction",method=RequestMethod.POST)
	@LoggerManage(description="简介修改")
	public Response updateIntroduction(UserParam userParam){
		if(null == userParam || null == userParam.getUserId() || StringUtils.isBlank(userParam.getIntroduction())){
			return result(ExceptionMsg.ParamError);
		}
		try {
			User user = userRepository.findOne(userParam.getUserId());
			if(null == user){
				return result(ExceptionMsg.UserNotExist);
			}
			userRepository.setIntroduction(userParam.getIntroduction(), user.getEmail());
		} catch (Exception e) {
			logger.error("简介修改异常：",e);
			return result(ExceptionMsg.FAILED);
		}
		return result();
	}
	
	@RequestMapping(value="/updateUserName",method=RequestMethod.POST)
	@LoggerManage(description="用户名修改")
	public Response updateUserName(UserParam userParam){
		if(null == userParam || null == userParam.getUserId() || StringUtils.isBlank(userParam.getNewUserName())){
			return result(ExceptionMsg.ParamError);
		}
		if(userParam.getNewUserName().length() > 12){
			return result(ExceptionMsg.UserNameLengthLimit);
		}
		try {
			User user = userRepository.findOne(userParam.getUserId());
			if(null == user){
				return result(ExceptionMsg.UserNotExist);
			}
			if(user.getUserName().equals(userParam.getNewUserName())){
				return result(ExceptionMsg.UserNameSame);
			}
			User newUser = userRepository.findByUserName(userParam.getNewUserName());
			if(null != newUser){
				return result(ExceptionMsg.UserNameUsed);
			}
			userRepository.setUserName(userParam.getNewUserName(), user.getEmail());
		} catch (Exception e) {
			logger.error("用户名修改异常：",e);
			return result(ExceptionMsg.FAILED);
		}
		return result();
	}
	
}