package com.favorites.web;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.favorites.comm.utils.SimpleHttpUtils;

public class UserControllerTest {


	//private String baseUrl="http://app.zhongxinvc.com:8000/zc-app";
	private String baseUrl="http://192.168.0.59:9090";
	//private String baseUrl="http://localhost:9090";
	

	@Test
	public void login() {
			String res = "";
			String url = baseUrl+"/user/login";
			Map<String, String> contents = new HashMap<String, String>();
			contents.put("userName", "q");
			contents.put("passWord", "q");
			res = SimpleHttpUtils.httpPost(url,contents); 
			System.out.println("***********************8");
			System.out.println(res);
	}

	
	@Test
	public void register() {
			String res = "";
			String url = baseUrl+"/user/register";
			Map<String, String> contents = new HashMap<String, String>();
			contents.put("email", "q@126.com");
			contents.put("userName", "q");
			contents.put("passWord", "q");
			res = SimpleHttpUtils.httpPost(url,contents); 
			System.out.println(res);
	}
}