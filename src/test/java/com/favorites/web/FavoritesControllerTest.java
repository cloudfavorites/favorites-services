package com.favorites.web;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.favorites.comm.utils.SimpleHttpUtils;

/**
*@ClassName: FavoritesControllerTest
*@Description: 
*@author YY 
*@date 2016年10月18日  上午10:17:14
*@version 1.0
*/
public class FavoritesControllerTest {
	
	private String baseUrl="http://localhost:9090";
	
	@Test
	public void add() {
			String res = "";
			String url = baseUrl+"/favorites/add";
			Map<String, Object> contents = new HashMap<String, Object>();
			contents.put("userId", 1L);
			contents.put("favoritesName", "小强强");
			res = SimpleHttpUtils.httpPost(url,contents); 
			System.out.println("***********************");
			System.out.println(res);
			System.out.println("***********************");
	}
	
	@Test
	public void update() {
			String res = "";
			String url = baseUrl+"/favorites/update";
			Map<String, Object> contents = new HashMap<String, Object>();
			contents.put("userId", 1L);
			contents.put("favoritesName", "地方地方");
			contents.put("favoritesId", 278L);
			res = SimpleHttpUtils.httpPost(url,contents); 
			System.out.println("***********************");
			System.out.println(res);
			System.out.println("***********************");
	}
	
	@Test
	public void delete() {
			String res = "";
			String url = baseUrl+"/favorites/delete";
			Map<String, Object> contents = new HashMap<String, Object>();
			contents.put("userId", 1L);
			contents.put("favoritesId", 278L);
			res = SimpleHttpUtils.httpPost(url,contents); 
			System.out.println("***********************");
			System.out.println(res);
			System.out.println("***********************");
	}
	
	@Test
	public void get() {
			String res = "";
			String url = baseUrl+"/favorites/getFavorites";
			Map<String, Object> contents = new HashMap<String, Object>();
			contents.put("userId", 1L);
			contents.put("myself", "myself");
			res = SimpleHttpUtils.httpPost(url,contents); 
			System.out.println("***********************");
			System.out.println(res);
			System.out.println("***********************");
	}

}
