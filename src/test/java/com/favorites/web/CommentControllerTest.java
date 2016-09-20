package com.favorites.web;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.favorites.comm.utils.SimpleHttpUtils;

public class CommentControllerTest {
	
//	private String baseUrl="http://localhost:9090";
//	private String baseUrl="http://192.168.0.59:9090";
	private String baseUrl="http://localhost:9090/comment";
	
	@Test
	public void addComment(){
		String res = "";
		String url = baseUrl + "/addComment";
		Map<String, String> contents = new HashMap<String, String>();
		contents.put("collectId", "93");
		contents.put("userId", "4");
		contents.put("content", "@admin 回复一下93号文章的 评论");
		res = SimpleHttpUtils.httpPost(url,contents); 
		System.out.println(res);
	}
	
	@Test
	public void getCommentList(){
		String res = "";
		String url = baseUrl + "/getCommentList";
		Map<String, String> contents = new HashMap<String, String>();
		contents.put("collectId", "93");
		res = SimpleHttpUtils.httpPost(url,contents); 
		System.out.println(res);
	}
	
	@Test
	public void deleteComment(){
		String res = "";
		String url = baseUrl + "/deleteComment";
		Map<String, String> contents = new HashMap<String, String>();
		contents.put("id", "25");
		contents.put("userId", "1");
		res = SimpleHttpUtils.httpPost(url,contents); 
		System.out.println(res);
	}

}
