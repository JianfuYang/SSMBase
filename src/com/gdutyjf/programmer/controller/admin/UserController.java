package com.gdutyjf.programmer.controller.admin;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.gdutyjf.programmer.entity.admin.User;
import com.gdutyjf.programmer.page.admin.Page;
import com.gdutyjf.programmer.service.admin.RoleService;
import com.gdutyjf.programmer.service.admin.UserService;

/**
 * 用户管理控制器
 * @author Jianf
 *
 */
@RequestMapping("/admin/user")
@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	
	/**
	 * 	用户列表页面
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public ModelAndView list(ModelAndView modelAndView) {
		
		Map<String, Object> queryMap = new HashMap<String, Object>();
		modelAndView.addObject("roleList",roleService.findList(queryMap));
		modelAndView.setViewName("user/list");
		
		return modelAndView;
	}
	
	
	/**
	 *  获取用户列表
	 * @param page
	 * @param username
	 * @param roleId
	 * @param sex
	 * @return
	 */
	@RequestMapping(value = "/list",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getList(Page page,
			@RequestParam(name = "username",required = false,defaultValue = "") String username,
			@RequestParam(name = "roleId",required = false) Long roleId,
			@RequestParam(name = "sex",required = false) Integer sex
			
			){
		Map<String, Object> reMap = new HashMap<String, Object>();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("username", username);
		queryMap.put("roleId", roleId);
		queryMap.put("sex", sex);
		queryMap.put("offset", page.getOffset());
		queryMap.put("pageSize", page.getRows());
		reMap.put("rows", userService.findList(queryMap));
		reMap.put("total", userService.getTotal(queryMap));
		return reMap;
	}
	
	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/add",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> add(User user){
		Map<String, String> retMap = new HashMap<String, String>();
		if(user == null) {
			retMap.put("type", "error");
			retMap.put("msg", "请填写正确的用户信息！");
			return retMap;
		}
		
		if(StringUtils.isEmpty(user.getUsername())) {
			retMap.put("type", "error");
			retMap.put("msg", "请填写用户名！");
			return retMap;
		}
		if(StringUtils.isEmpty(user.getPassword())) {
			retMap.put("type", "error");
			retMap.put("msg", "请填写密码！");
			return retMap;
		}
		if(user.getRoleId() == null) {
			retMap.put("type", "error");
			retMap.put("msg", "请选择所属角色！");
			return retMap;
		}
		
		if(isExist(user.getUsername(), 0l)) {
			retMap.put("type", "error");
			retMap.put("msg", "该用户名已经存在！请重新输入！");
			return retMap;
		}
		if(userService.add(user) <= 0) {
			retMap.put("type", "error");
			retMap.put("msg", "用户添加失败，请联系管理员！");
			return retMap;
		}
		
		retMap.put("type", "success");
		retMap.put("msg", "添加成功");
		return retMap;
	}
	
	/**
	 *  判断用户名是否存在
	 * @param username
	 * @param id
	 * @return
	 */
	private boolean isExist(String username,Long id) {
		User user = userService.findByUsername(username);
		if(user == null)
			return false;
		if(user.getId().longValue() == id.longValue())
			return false;
		
		return true;
	}
	
	/**
	 *  编辑用户
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/edit",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> edit(User user){
		Map<String, String> retMap = new HashMap<String, String>();
		if(user == null) {
			retMap.put("type", "error");
			retMap.put("msg", "请填写正确的用户信息！");
			return retMap;
		}
		
		if(StringUtils.isEmpty(user.getUsername())) {
			retMap.put("type", "error");
			retMap.put("msg", "请填写用户名！");
			return retMap;
		}
		if(user.getId() == null) {
			retMap.put("type", "error");
			retMap.put("msg", "请选择所属角色！");
			return retMap;
		}
		
		if(isExist(user.getUsername(), user.getId())) {
			retMap.put("type", "error");
			retMap.put("msg", "该用户名已经存在！请重新输入！");
			return retMap;
		}
		if(userService.edit(user) <= 0) {
			retMap.put("type", "error");
			retMap.put("msg", "用户添加失败，请联系管理员！");
			return retMap;
		}
		
		retMap.put("type", "success");
		retMap.put("msg", "添加成功");
		return retMap;
	}
	
	/**
	 *  批量删除用户
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> delete(String ids){
		Map<String, String> retMap = new HashMap<String, String>();
		if(StringUtils.isEmpty(ids)) {
			retMap.put("type", "error");
			retMap.put("msg", "请选择要删除的数据！");
			return retMap;
		}
		if(ids.contains(",")) {
			ids = ids.substring(0,ids.length()-1);
		}
		
		if(userService.delete(ids) <= 0) {
			retMap.put("type", "error");
			retMap.put("msg", "用户删除失败，请联系管理员！");
			return retMap;
		}
		
		retMap.put("type", "success");
		retMap.put("msg", "用户删除成功！");
		return retMap;
	}
	
	
	/**
	 *  上传图片
	 * @param photo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/upload_photo",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> uploadPhoto(MultipartFile photo,HttpServletRequest request){
		Map<String, String> retMap = new HashMap<String, String>();
		if(photo == null) {
			retMap.put("type", "error");
			retMap.put("msg", "请选择要上传的文件！");
			return retMap;
		}
		if(photo.getSize() > 1024*1024*1024)
		{
			retMap.put("type", "error");
			retMap.put("msg", "文件大小不能超过10M！");
			return retMap;
		}
		//获取文件后缀
		String suffix = photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf(".")+1,photo.getOriginalFilename().length());
		if(!"jpg,jpeg,gif,png".toUpperCase().contains(suffix.toUpperCase())) {
			retMap.put("type", "error");
			retMap.put("msg", "请选择jpg,jpeg,gif,png格式的图片！");
			return retMap;
		}
		
		String savePath = request.getServletContext().getRealPath("/") + "/resources/upload/" ;
		File savePathFile = new File(savePath);
		if(!savePathFile.exists()) {
			//若不存在该目录，则创建目录
			savePathFile.mkdir();
		}
		String fileName = new Date().getTime()+"."+suffix;
		try {
			//将文件保存至指定目录
			photo.transferTo(new File(savePath + fileName));
		} catch (Exception e) {
			retMap.put("type", "error");
			retMap.put("msg", "保存文件异常！");
			e.printStackTrace();
			return retMap;
		}
		
		retMap.put("type", "success");
		retMap.put("msg", "用户删除成功！");
		retMap.put("filepath", request.getServletContext().getContextPath() + "/resources/upload/"+fileName);
		return retMap;
	}
	
	
	
	
}
