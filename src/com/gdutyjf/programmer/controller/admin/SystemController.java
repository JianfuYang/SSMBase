package com.gdutyjf.programmer.controller.admin;


import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.gdutyjf.programmer.entity.admin.User;
import com.gdutyjf.programmer.service.admin.AuthorityService;
import com.gdutyjf.programmer.service.admin.LogService;
import com.gdutyjf.programmer.service.admin.MenuService;
import com.gdutyjf.programmer.service.admin.RoleService;
import com.gdutyjf.programmer.service.admin.UserService;
import com.gdutyjf.programmer.util.CpachaUtil;
import com.gdutyjf.programmer.entity.admin.Menu;
import com.gdutyjf.programmer.util.MenuUtil;
import com.gdutyjf.programmer.entity.admin.Authority;
import com.gdutyjf.programmer.entity.admin.Role;

/**
 *	 系统操作类控制器
 * @author Jianf
 *
 */
@Controller
@RequestMapping("/system")
public class SystemController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	AuthorityService authorityService;

	@Autowired
	MenuService menuService;
	
	@Autowired
	LogService logService;
	
	/**
	 * 	系统登录后的主页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public ModelAndView index(ModelAndView model,HttpServletRequest request){
		List<Menu> userMenus = (List<Menu>)request.getSession().getAttribute("userMenus");
		model.addObject("topMenuList", MenuUtil.getAllTopMenu(userMenus));
		model.addObject("secondMenuList", MenuUtil.getAllSecondMenu(userMenus));
		System.out.println("所有的二级菜单为："+ MenuUtil.getAllSecondMenu(userMenus).toString());
		model.setViewName("system/index");
		return model;//WEB-INF/views/+system/index+.jsp = WEB-INF/views/system/index.jsp
	}
	
	/**
	 *	 系统登录后的欢迎页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/welcome",method = RequestMethod.GET)
	public ModelAndView welcome(ModelAndView model) {
		model.setViewName("system/welcome");
		return model;
	}
	
	
	//登录页面
	@RequestMapping(value = "/login",method = RequestMethod.GET)
	public ModelAndView login(ModelAndView model) {
		model.setViewName("system/login");
		
		return model;
	}
	
	/**
	 * 登录表单处理提交控制器
	 * @param user
	 * @param cpacha
	 * @return
	 */
	@RequestMapping(value = "/login",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> loginAct(User user,String cpacha,HttpServletRequest request) {
		Map<String, String> ret = new HashMap<String, String>();
		if(user == null) {
			ret.put("type", "error");
			ret.put("msg","请填写用户信息");
			return ret;
		}
		if(StringUtils.isEmpty(cpacha)) {
			ret.put("type", "error");
			ret.put("msg","请填写验证码");
			return ret;
		}
		if(StringUtils.isEmpty(user.getUsername())) {
			ret.put("type", "error");
			ret.put("msg","请填写用户名");
			return ret;
		}
		if(StringUtils.isEmpty(user.getPassword())) {
			ret.put("type", "error");
			ret.put("msg","请填写密码");
			return ret;
		}
		Object loginCpacha = request.getSession().getAttribute("loginCpacha");
		//自定义日志
		
		if(loginCpacha == null) {
			ret.put("type","error");
			ret.put("msg", "会话超时，请刷新页面");
			return ret;
		}
		//对比验证码
		if(!cpacha.toUpperCase().equals(loginCpacha.toString().toUpperCase())) {
			ret.put("type","error");
			ret.put("msg", "验证码错误");
			logService.add("用户名"+user.getUsername()+"的用户登录时输入验证码错误！");
			return ret;
		}
		//查询用户是否存在
		User userFindByUsername = userService.findByUsername(user.getUsername());
		if(userFindByUsername == null) {
			ret.put("type","error");
			ret.put("msg","该用户名不存在！");
			logService.add("登录时，用户名"+user.getUsername()+"的用户不存在！");
			return ret;
		}
		if(!user.getPassword().contentEquals(userFindByUsername.getPassword())) {
			ret.put("type","error");
			ret.put("msg","密码错误！");
			logService.add("用户名"+user.getUsername()+"的用户登录时输入密码错误！");
			return ret;
		}
		//说明用户名密码及验证码都正确
		//此时需要查询用户的权限
		
		//根据Id获取用户
		Role role = roleService.find(userFindByUsername.getRoleId());
		//根据角色id获取权限信息
//		System.out.println("roleId:"+role.getId());
		List<Authority> authorityList = authorityService.findListByRoleId(role.getId());
		String menuIds = "";
		for(Authority authority:authorityList){
			menuIds += authority.getMenuId() + ",";
		}
		if(!StringUtils.isEmpty(menuIds)){
			menuIds = menuIds.substring(0,menuIds.length()-1);
		}
		//
		List<Menu> userMenus = menuService.findListByIds(menuIds);
		//
		request.getSession().setAttribute("admin",userFindByUsername);
		request.getSession().setAttribute("role", role);
		request.getSession().setAttribute("userMenus", userMenus);
		
		//登录成功
		ret.put("type","success");
		ret.put("msg", "登录成功");
		logService.add(role.getName()+"用户"+user.getUsername()+"登录成功！");
		return ret;
		
	}
	
	/**
	 *  后台退出注销功能
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/logout",method = RequestMethod.GET)
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute("admin", null);
		session.setAttribute("role", null);
		session.setAttribute("userMenus", null);
		return "redirect:login";
	}
	
	/**
	 *  修改密码页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/edit_password",method = RequestMethod.GET)
	public ModelAndView editPassword(ModelAndView model) {
		model.setViewName("system/edit_password");
		return model;
	}
	
	@RequestMapping(value = "/edit_password",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> editPasswordAct(String newpassword, String oldpassword ,HttpServletRequest request) {
		
		Map<String, String> ret = new HashMap<String, String>();
		if(StringUtils.isEmpty(newpassword)) {
			ret.put("type", "error");
			ret.put("msg","请填写新密码");
			return ret;
		}
		User user = (User)request.getSession().getAttribute("admin");
		user.setPassword(newpassword);
		if(!user.getPassword().equals(oldpassword)) {
			ret.put("type", "error");
			ret.put("msg","原密码错误！");
			return ret;
		}
		
		if(userService.editPassword(user) <= 0) {
			ret.put("type", "error");
			ret.put("msg","修改密码失败，请联系管理员！");
			return ret;
		}
	
		ret.put("type", "success");
		ret.put("msg","修改密码成功！");
		logService.add("用户名为【"+user.getUsername()+"】的用户成功修改密码！");
		return ret;
		
	}
	
	
	/**
	 * 	本系统所有的验证码均采用此法
	 * @param vcodeLen
	 * @param width
	 * @param height
	 * @param cpachaType 用来区别验证码的类型，传入字符串
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/get_cpacha",method = RequestMethod.GET)
	public void generateCpacha(
			@RequestParam(name = "vl",required = false,defaultValue = "4")Integer vcodeLen,
			@RequestParam(name = "w",required = false,defaultValue = "100")Integer width,
			@RequestParam(name = "h",required = false,defaultValue = "30")Integer height,
			@RequestParam(name = "type",required = false,defaultValue = "loginCpacha")String cpachaType,
			HttpServletRequest request,
			HttpServletResponse response) {
		CpachaUtil cpachaUtil = new CpachaUtil(vcodeLen,width,height);
		String generatorVCode = cpachaUtil.generatorVCode();
		request.getSession().setAttribute(cpachaType, generatorVCode);
		BufferedImage generatorRoteVcodeImage = cpachaUtil.generatorRotateVCodeImage(generatorVCode, true);
		try {
			ImageIO.write(generatorRoteVcodeImage,"gif",response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
}
