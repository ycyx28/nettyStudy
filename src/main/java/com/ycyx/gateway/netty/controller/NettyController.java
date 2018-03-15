package com.ycyx.gateway.netty.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ycyx.gateway.netty.constant.SessionConstant;
import com.ycyx.gateway.netty.model.Session;
import com.ycyx.gateway.netty.model.User;

@Controller
@RequestMapping("/netty")
public class NettyController {
	
	
	@RequestMapping("/server")
	@ResponseBody
	public HashMap<String, Object> nettyServer(){
		HashMap<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("msg", "success");
		return responseMap;
	}
	
	@RequestMapping("/login")
	@ResponseBody
	public HashMap<String, Object> login(User user,HttpServletRequest request){
		HashMap<String, Object> responseMap = new HashMap<String, Object>();
		
		if(null == user){
			responseMap.put("code", "302");
			responseMap.put("msg", "登录信息不能为空");
			return responseMap;
		}
		
		if(StringUtils.isEmpty(user.getUserName()) || StringUtils.isEmpty(user.getPassWord()) ){
			responseMap.put("code", "301");
			responseMap.put("msg", "用户名或密码不能为空");
			return responseMap;
		}
		String sessionId = request.getSession().getId();
		if(null != SessionConstant.sessionMap.get(sessionId)){
			responseMap.put("code", "300");
			responseMap.put("msg", "您已经登录，请勿重复登录");
			return responseMap;
		}
		Session session = new Session();
		session.setSessionId(sessionId);
		session.setUserName(user.getUserName());
		SessionConstant.sessionMap.put(sessionId, session);
		responseMap.put("code", "200");
		responseMap.put("sessionId", sessionId);
		return responseMap;
	}

}
