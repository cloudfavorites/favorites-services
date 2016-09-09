package com.favorites.web;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.favorites.comm.utils.SimpleHttpUtils;

public class UserControllerTest {


	//private String baseUrl="http://app.zhongxinvc.com:8000/zc-app";
//	private String baseUrl="http://192.168.0.60:9090";
	private String baseUrl="http://localhost:9090";
	

	@Test
	public void login() {
			String res = "";
			String url = baseUrl+"/user/login";
			Map<String, String> contents = new HashMap<String, String>();
			contents.put("userName", "admin");
			contents.put("passWord", "222222");
			res = SimpleHttpUtils.httpPost(url,contents); 
			System.out.println("***********************8");
			System.out.println(res);
	}

	
	@Test
	public void register() {
			String res = "";
			String url = baseUrl+"/user/register";
			Map<String, String> contents = new HashMap<String, String>();
			contents.put("email", "admin2@qq.com");
			contents.put("userName", "admin2");
			contents.put("passWord", "111111");
			res = SimpleHttpUtils.httpPost(url,contents); 
			System.out.println(res);
	}
	
	/**
	 * 密码修改
	 */
	@Test
	public void updatePassWord(){
		String res = "";
		String url = baseUrl + "/user/updatePassWord";
		Map<String, String> contents = new HashMap<String, String>();
		contents.put("userId", "1");
		contents.put("newPwd", "222222");
		contents.put("oldPwd", "111111");
		res = SimpleHttpUtils.httpPost(url,contents); 
		System.out.println(res);
	}
	
	/**
	 * 简介修改
	 */
	@Test
	public void updateIntroduction(){
		String res = "";
		String url = baseUrl + "/user/updateIntroduction";
		Map<String, String> contents = new HashMap<String, String>();
		contents.put("userId", "1");
		contents.put("introduction", "身怀宝藏，总会遇见饿狼");
		res = SimpleHttpUtils.httpPost(url,contents); 
		System.out.println(res);
	}
	
	/**
	 * 用户名修改
	 */
	@Test
	public void updateUserName(){
		String res = "";
		String url = baseUrl + "/user/updateUserName";
		Map<String, String> contents = new HashMap<String, String>();
		contents.put("userId", "1");
		contents.put("newUserName", "Smilence");
		res = SimpleHttpUtils.httpPost(url,contents); 
		System.out.println(res);
	}
}