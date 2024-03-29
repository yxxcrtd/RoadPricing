package com.igoosd.rps.util;


import com.igoosd.common.util.DESUtils;
import com.igoosd.common.util.HashKit;
import com.igoosd.model.TUser;
import com.igoosd.rps.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Web相关工具类,同时用spring session，因此用户信息也放到session和cookie里
 * 
 */
@Service
public  class WebUtils {

	@Autowired
	UserService userService;

	private static final String SESSION_KEY_LOGIN_USER = "sessionUser";

	/**
	 * 密码:md5hex
	 * @param password
	 * @return
	 */
	public static String pwdEncode(String password) {
		return HashKit.md5(password);
	}

	
	/**
	 * 返回当前用户
	 * @param request
	 * @param response
	 * @return GitUserModel
	 */
	public SessionUser currentUser(HttpServletRequest request, HttpServletResponse response) {
		SessionUser user = (SessionUser) request.getSession().getAttribute(SESSION_KEY_LOGIN_USER);
		if(user!=null){
			return user;
		}
		String cookieKey = Const.USER_COOKIE_KEY;
		// 获取cookie信息
		String userCookie = getCookie(request, cookieKey);
		// 1.cookie为空，直接清除
		if (StringUtils.isEmpty(userCookie)) {
			removeCookie(response, cookieKey);
			return null;
		}
		// 2.解密cookie
		String cookieInfo = null;
		// cookie 私钥
		String secret = Const.USER_COOKIE_SECRET;
		try {
			cookieInfo = new DESUtils(secret).decryptString(userCookie);
		} catch (RuntimeException e) {
			// ignore
		}
		// 3.异常或解密问题，直接清除cookie信息
		if (StringUtils.isEmpty(cookieInfo)) {
			removeCookie(response, cookieKey);
			return null;
		}
		String[] userInfo = cookieInfo.split("~");
		// 4.规则不匹配
		if (userInfo.length < 3) {
			removeCookie(response, cookieKey);
			return null;
		}
		String userIdStr   = userInfo[0];
		String oldTime  = userInfo[1];
		String maxAge   = userInfo[2];
		// 5.判定时间区间，超时的cookie清理掉
		if (!"-1".equals(maxAge)) {
			long now  = System.currentTimeMillis();
			long time = Long.parseLong(oldTime) + (Long.parseLong(maxAge) * 1000);
			if (time <= now) {
				removeCookie(response, cookieKey);
				return null;
			}
		}
		if(userIdStr == null || "null".equals(userIdStr)){
			removeCookie(response, cookieKey);
			return null;
		}
		//用户是否可用
		Long userId = Long.valueOf(userIdStr);
		TUser dbUser =  userService.getEntityByKey(userId);

		user = new SessionUser();
		user.setId(dbUser.getId());
		user.setLoginName(dbUser.getLoginName());
		user.setPhone(dbUser.getPhone());
		user.setSex(dbUser.getSex());
		user.setUserName(dbUser.getUserName());
		user.setIp(getIP(request));
		//存储到session中
		request.getSession().setAttribute(SESSION_KEY_LOGIN_USER, user);
		return user;
	}

	/**
	 * 用户登陆状态维持
	 * 
	 * cookie设计为: des(私钥).encode(userId~time~maxAge~ip)
	 * 
	 * @param user  用户model
	 * @param remember   是否记住密码、此参数控制cookie的 maxAge，默认为-1（只在当前会话）<br>
	 *                   记住密码默认为30天
	 * @return void
	 */
	public static void loginUser(HttpServletRequest request, HttpServletResponse response, SessionUser user, boolean... remember) {
		
		request.setAttribute(SESSION_KEY_LOGIN_USER, user);
		// 获取用户的id、nickName
		String uid     = user.getId()+"";
		// 当前毫秒数
		long   now      = System.currentTimeMillis();
		// 超时时间
		int    maxAge   = -1;
		if (remember.length > 0 && remember[0]) {
			maxAge      = 60 * 60 * 24 * 30; // 30天
		}
		// 用户id地址
		String ip		= getIP(request);
		// 构造cookie
		StringBuilder cookieBuilder = new StringBuilder()
			.append(uid).append("~")
			.append(now).append("~")
			.append(maxAge).append("~")
			.append(ip);

		// cookie 私钥
		String secret = Const.USER_COOKIE_SECRET;
		// 加密cookie
		String userCookie = new DESUtils(secret).encryptString(cookieBuilder.toString());
		String cookieKey = Const.USER_COOKIE_KEY;
		// 设置用户的cookie、 -1 维持成session的状态
		setCookie(response, cookieKey, userCookie, maxAge);
	}

	/**
	 * 退出即删除用户信息
	 * @return void
	 */
	public static void logoutUser(HttpServletRequest request,HttpServletResponse response) {
		request.getSession().removeAttribute(SESSION_KEY_LOGIN_USER);
		removeCookie(response, Const.USER_COOKIE_KEY);
		
	}

	/**
	 * 读取cookie
	 * @param request
	 * @param key
	 * @return
	 */
	public static String getCookie(HttpServletRequest request, String key) {
		Cookie[] cookies = request.getCookies();
		if(null != cookies){
			for (Cookie cookie : cookies) {
				if (key.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * 清除 某个指定的cookie 
	 * @param response
	 * @param key
	 */
	public static void removeCookie(HttpServletResponse response, String key) {
		setCookie(response, key, null, 0);
	}

	/**
	 * 设置cookie
	 * @param response
	 * @param name
	 * @param value
	 * @param maxAgeInSeconds
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, int maxAgeInSeconds) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setMaxAge(maxAgeInSeconds);
		// 指定为httpOnly保证安全性
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
	}



	/**
	 * 获取浏览器信息
	 * @param request
	 * @return String
	 */
	public static String getUserAgent(HttpServletRequest request) {
		return request.getHeader("User-Agent");
	}

	/**
	 * 获取ip
	 * @param request
	 * @return
	 */
	public static String getIP(HttpServletRequest request) {
		String ip = request.getHeader("X-Requested-For");
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Forwarded-For");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	public  boolean isAdmin(HttpServletRequest request,HttpServletResponse response) {
		SessionUser user = this.currentUser(request,  response);
		if(user==null){
			throw new RuntimeException("未登陆用户");
		}
		return user.getUserName().equals("admin");
			
	}

}
