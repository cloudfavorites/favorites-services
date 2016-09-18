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
	
}