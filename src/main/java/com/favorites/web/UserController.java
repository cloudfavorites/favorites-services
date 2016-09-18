package com.favorites.web;

import java.sql.Timestamp;
import java.util.UUID;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.favorites.comm.Const;
import com.favorites.comm.aop.LoggerManage;
import com.favorites.comm.utils.DateUtils;
import com.favorites.comm.utils.MD5Util;
import com.favorites.comm.utils.MessageUtil;
import com.favorites.domain.Favorites;
import com.favorites.domain.User;
import com.favorites.domain.UserRepository;
import com.favorites.domain.result.ExceptionMsg;
import com.favorites.domain.result.LoginResult;
import com.favorites.domain.result.Response;
import com.favorites.domain.result.ResponseData;
import com.favorites.domain.result.UserInformationResult;
import com.favorites.param.UserParam;
import com.favorites.service.ConfigService;
import com.favorites.service.FavoritesService;
import com.favorites.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
	@Autowired
	private UserRepository userRepository;
	@Resource
	private UserService userService;
	@Resource
	private ConfigService configService;
	@Resource
	private FavoritesService favoritesService;
	@Resource
    private JavaMailSender mailSender;
	@Value("${spring.mail.username}")
	private String mailFrom;
	@Value("${mail.subject.forgotpassword}")
	private String mailSubject;
	@Value("${mail.content.forgotpassword}")
	private String mailContent;
	@Value("${favorites.web.path}")
	private String webPath;
	
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@LoggerManage(description="用户登录")
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
			ret.setUserName(loginUser.getUserName());
			ret.setToken(getSession().getId());
			return new ResponseData(ExceptionMsg.SUCCESS,ret);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("login failed, ", e);
			return new ResponseData(ExceptionMsg.FAILED);
		}
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@LoggerManage(description="用户注册")
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
	
	@RequestMapping(value="/updateUserInfo",method=RequestMethod.POST)
	@LoggerManage(description="个人信息修改（简介&用户名）")
	public Response updateUserInfo(UserParam userParam){
		if(null == userParam || null == userParam.getUserId() || StringUtils.isBlank(userParam.getIntroduction()) 
				|| StringUtils.isBlank(userParam.getNewUserName())){
			return result(ExceptionMsg.ParamError);
		}
		try {
			return userService.updateUserInfo(userParam);
		} catch (Exception e) {
			logger.error("个人信息修改异常：",e);
			return result(ExceptionMsg.FAILED);
		}
	}
	
	
	/**
	 * 忘记密码-发送重置邮件
	 * @param email
	 * @return
	 */
	@RequestMapping(value = "/sendForgotPasswordEmail", method = RequestMethod.POST)
	@LoggerManage(description="发送忘记密码邮件")
	public Response sendForgotPasswordEmail(String email) {
		if(StringUtils.isBlank(email)){
			return result(ExceptionMsg.ParamError);
		}
		try {
			User registUser = userRepository.findByEmail(email);
			if (null == registUser) {
				return result(ExceptionMsg.EmailNotRegister);
			}	
			String secretKey = UUID.randomUUID().toString(); // 密钥
            Timestamp outDate = new Timestamp(System.currentTimeMillis() + 30 * 60 * 1000);// 30分钟后过期
            long date = outDate.getTime() / 1000 * 1000;
            userRepository.setOutDateAndValidataCode(outDate+"", secretKey, email);
            String key =email + "$" + date + "$" + secretKey;
            System.out.println(" key>>>"+key);
            String digitalSignature = MD5Util.encrypt(key);// 数字签名
            String path = this.getRequest().getContextPath();
            String basePath = this.getRequest().getScheme() + "://"
                    + this.getRequest().getServerName() + ":"
                    + this.getRequest().getServerPort() + path + "/";
            String resetPassHref = basePath + "newPassword?sid="
                    + digitalSignature +"&email="+email;
            String emailContent = MessageUtil.getMessage(mailContent, resetPassHref);					
	        MimeMessage mimeMessage = mailSender.createMimeMessage();	        
	        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
	        helper.setFrom(mailFrom);
	        helper.setTo(email);
	        helper.setSubject(mailSubject);
	        helper.setText(emailContent, true);
	        mailSender.send(mimeMessage);
		} catch (Exception e) {
			logger.error("发送忘记密码邮件异常：",e);
			return result(ExceptionMsg.FAILED);
		}
		return result();
	}
	
	/**
	 * 忘记密码-设置新密码
	 * @param newpwd
	 * @param email
	 * @param sid
	 * @return
	 */
	@RequestMapping(value = "/setNewPassword", method = RequestMethod.POST)
	@LoggerManage(description="设置新密码")
	public Response setNewPassword(String newpwd, String email, String sid) {
		if(StringUtils.isBlank(newpwd) || StringUtils.isBlank(email) || StringUtils.isBlank(sid)){
			return result(ExceptionMsg.ParamError);
		}
		try {
			User user = userRepository.findByEmail(email);
			Timestamp outDate = Timestamp.valueOf(user.getOutDate());
			if(outDate.getTime() <= System.currentTimeMillis()){ //表示已经过期
				return result(ExceptionMsg.LinkOutdated);
            }
            String key = user.getEmail()+"$"+outDate.getTime()/1000*1000+"$"+user.getValidataCode();//数字签名
            String digitalSignature = MD5Util.encrypt(key);
            if(!digitalSignature.equals(sid)) {
            	 return result(ExceptionMsg.LinkOutdated);
            }
            userRepository.setNewPassword(getPwd(newpwd), email);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("设置新密码异常： ", e);
			return result(ExceptionMsg.FAILED);
		}
		return result();
	}
	
	/**
	 * 注销
	 * @return
	 */
	@RequestMapping(value="/logout", method=RequestMethod.POST)
	@LoggerManage(description="注销")
	public Response logout() {
		try {
			getSession().removeAttribute(Const.LOGIN_SESSION_KEY);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("注销异常： ", e);
			return result(ExceptionMsg.FAILED);
		}
		return result();
	}
	
	/**
	 * 获取用户个人信息
	 * @return
	 */
	@RequestMapping(value="/getUserInformation",method=RequestMethod.POST)
	@LoggerManage(description="获取用户个人信息")
	public ResponseData getUserInformation(UserParam userParam){
		UserInformationResult ret = new UserInformationResult();
		if(null == userParam || null == userParam.getUserId() || StringUtils.isBlank(userParam.getMyself())){
			return new ResponseData(ExceptionMsg.ParamError);
		}
		User user = userRepository.findOne(userParam.getUserId());
		if(null == user){
			return new ResponseData(ExceptionMsg.UserNotExist);
		}
		try {
			BeanUtils.copyProperties(user, ret);
			ret.setProfilePicture(webPath + user.getProfilePicture());
			return userService.getUserInformation(ret, userParam);
		} catch (Exception e) {
			logger.error("获取用户个人信息异常：",e);
			return new ResponseData(ExceptionMsg.FAILED);
		}
	}
	
}