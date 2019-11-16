package com.gdutyjf.programmer.interceptor.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.gdutyjf.programmer.entity.admin.Menu;
import com.gdutyjf.programmer.util.MenuUtil;

import net.sf.json.JSONObject;

public class LoginInterceptor implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception exception)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView modelAndView)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		// TODO Auto-generated method stub
		String requestURI = request.getRequestURI();
		
		Object admin = request.getSession().getAttribute("admin");
		if(admin == null) {
			//未登录或登录失效
			System.out.println("链接"+requestURI+"进入拦截器！");
			String header = request.getHeader("X-Requested-With");
			//判断是否是ajax请求
			if("XMLHttpRequest".equals(header)) {
				//表示是ajax请求
				Map<String, String> retMap = new HashMap<String, String>();
				retMap.put("type","error");
				retMap.put("msg", "登录会话超时或还未登录，请重新登录");
				response.getWriter().write(JSONObject.fromObject(retMap).toString());
				return false;
			}
			response.sendRedirect(request.getServletContext().getContextPath()+"/system/login");
			return false;
		}
		String mid = request.getParameter("_mid");
		if(!StringUtils.isEmpty(mid)) {
			List<Menu> allThirdMenus = MenuUtil.getAllThirdMenu((List<Menu>) request.getSession().getAttribute("userMenus"),Long.valueOf(mid));
			request.setAttribute("thirdMenuList", allThirdMenus);
		}
		
		return true;
	}

}
