package com.favorites.web;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.favorites.comm.utils.SimpleHttpUtils;

public class CollectControllerTest {
	
//	private String baseUrl="http://localhost:9090";
//	private String baseUrl="http://192.168.0.59:9090";
	private String baseUrl="http://localhost:9090/collect";

	/**
	 * 获取文章列表
	 * myself+home=首页
	 * myself+favorites=收藏夹
	 * myself+garbage=回收站
	 * myself+export=发现
	 * myself+centerpage=个人中心页面
	 * others+centerpage=别人个人中心页面
	 * others+favorites=别人公开收藏夹
	 */
	@Test
	public void getCollectList(){
		String res = "";
		String url = baseUrl + "/getCollectList";
		Map<String, String> contents = new HashMap<String, String>();
		contents.put("userId", "10");
		contents.put("page", "0");
		contents.put("myself", "myself");
		contents.put("type", "favorites");
		contents.put("favoritesId", "62");
		res = SimpleHttpUtils.httpPost(url,contents); 
		System.out.println(res);
	}
}
