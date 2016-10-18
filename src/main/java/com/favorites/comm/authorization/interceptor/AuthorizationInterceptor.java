package com.favorites.comm.authorization.interceptor;


import com.favorites.comm.Const;
import com.favorites.comm.authorization.manager.TokenManager;
import com.favorites.comm.authorization.model.TokenModel;
import com.favorites.domain.result.ExceptionMsg;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 自定义拦截器，判断此次请求是否有权限
 */

public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    protected Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private TokenManager manager;

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        logger.info("进入Interceptor");
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //从header中得到token
        String authorization = request.getHeader(Const.AUTHORIZATION);
        if(StringUtils.isBlank(authorization)){
            authorization = request.getParameter(Const.AUTHORIZATION);
        }
        logger.info("request token：" + authorization);
        //验证token
        if (manager == null) {
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            manager = (TokenManager) factory.getBean("tokenManager");
        }
        TokenModel model = manager.getToken(authorization);
        if (manager.checkToken(model)) {
            //如果token验证成功，将token对应的用户id存在request中，便于之后注入
            request.setAttribute(Const.CURRENT_USER_ID, model.getUserId());
            return true;
        }
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        String r = "{\"rspCode\":\"" + ExceptionMsg.TokenTimeOut.getCode() +
                "\", \"rspMsg\":\"" + ExceptionMsg.TokenTimeOut.getMsg() + "\"}";
        response.getWriter().write(r);
        return false;
    }
}
