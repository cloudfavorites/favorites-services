package com.favorites.web;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.favorites.comm.utils.SimpleHttpUtils;

/**
*@ClassName: NoticeControllerTest
*@Description: 
*@author YY 
*@date 2016年10月19日  下午6:08:12
*@version 1.0
*/
public class NoticeControllerTest {
	
	private String baseUrl="http://localhost:9090";
	
	@Test
	public void getNoticeCollectList() {
			String res = "";
			String url = baseUrl+"/notice/getNoticeCollectList";
			Map<String, Object> contents = new HashMap<String, Object>();
			contents.put("authorization", "1_afbb9fb6e0148a7cddced88c4f7e18eb");
			contents.put("userId", 1L);
			contents.put("type", "praise");
			res = SimpleHttpUtils.httpPost(url,contents); 
			System.out.println("***********************");
			System.out.println(res);
			System.out.println("***********************");
	}
	
	@Test
	public void getUnreadNoticeCounts() {
			String res = "";
			String url = baseUrl+"/notice/getUnreadNoticeCounts";
			Map<String, Object> contents = new HashMap<String, Object>();
			contents.put("authorization", "1_afbb9fb6e0148a7cddced88c4f7e18eb");
			contents.put("userId", 1L);
			contents.put("type", "at");
			res = SimpleHttpUtils.httpPost(url,contents); 
			System.out.println("***********************");
			System.out.println(res);
			System.out.println("***********************");
	}
	
	@Test
	public void updateNoticeReaded() {
			String res = "";
			String url = baseUrl+"/notice/updateNoticeReaded";
			Map<String, Object> contents = new HashMap<String, Object>();
			contents.put("authorization", "1_afbb9fb6e0148a7cddced88c4f7e18eb");
			contents.put("userId", 1L);
			contents.put("type", "praise");
			res = SimpleHttpUtils.httpPost(url,contents); 
			System.out.println("***********************");
			System.out.println(res);
			System.out.println("***********************");
	}

}
