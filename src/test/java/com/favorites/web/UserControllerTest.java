package com.favorites.web;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.favorites.comm.utils.SimpleHttpUtils;

public class UserControllerTest {


	//private String baseUrl="http://localhost:9090";
//	private String baseUrl="http://192.168.0.59:9090";
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
	 * 个人信息修改（简介&用户名）
	 */
	@Test
	public void updateUserInfo(){
		String res = "";
		String url = baseUrl + "/user/updateUserInfo";
		Map<String, String> contents = new HashMap<String, String>();
		contents.put("userId", "10");
		contents.put("introduction", "超级管理员10");
		contents.put("newUserName", "admin");
		res = SimpleHttpUtils.httpPost(url,contents); 
		System.out.println(res);
	}
	
	
	/**
	 * 获取用户个人信息
	 */
	@Test
	public void getUserInformation(){
		String res = "";
		String url = baseUrl + "/user/getUserInformation";
		Map<String, String> contents = new HashMap<String, String>();
		contents.put("userId", "10");
		contents.put("myself", "others");
		res = SimpleHttpUtils.httpPost(url,contents); 
		System.out.println(res);
	}
	
	/**
	 * 发送重置密码邮件
	 */
	@Test
	public void sendForgotPasswordEmail(){
		String url = baseUrl + "/user/sendForgotPasswordEmail";
		Map<String, String> contents = new HashMap<String, String>();
//		contents.put("email","yuyang_053@163.com");
		String res = SimpleHttpUtils.httpPost(url,contents); 
		System.out.println(res);
	}
	
	/**
	 * 属性修改
	 */
	@Test
	public void updateConfig(){
		String url = baseUrl + "/user/updateConfig";
		Map<String, Object> contents = new HashMap<String, Object>();
		contents.put("userId",1);
//		contents.put("type","defaultFavorites");
//		contents.put("type","defaultCollectType");
		contents.put("type","defaultModel");
//		contents.put("defaultFavorites","73");
		String res = SimpleHttpUtils.httpPost(url,contents); 
		System.out.println(res);
	}
	
	/**
	 * 获取用户收藏夹
	 */
	@Test
	public void getUserFavorites(){
		String url = baseUrl + "/user/getUserFavorites";
		Map<String, Object> contents = new HashMap<String, Object>();
		contents.put("userId",1);
		contents.put("myself","myself");
		String res = SimpleHttpUtils.httpPost(url,contents); 
		System.out.println(res);
	}
	
}