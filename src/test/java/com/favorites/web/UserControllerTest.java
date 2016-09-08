package com.favorites.web;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.favorites.comm.utils.SimpleHttpUtils;

public class UserControllerTest {


	//private String baseUrl="http://app.zhongxinvc.com:8000/zc-app";
	//private String baseUrl="http://192.168.0.59:8081/zc-app";
	private String baseUrl="http://localhost:8080";
	

	@Test
	public void login() {
			String res = "";
			String url = baseUrl+"/user/login";
			Map<String, String> contents = new HashMap<String, String>();
			contents.put("userName", "gg");
			contents.put("passWord", "aa");
			res = SimpleHttpUtils.httpPost(url,contents); 
			System.out.println(res);
	}

}