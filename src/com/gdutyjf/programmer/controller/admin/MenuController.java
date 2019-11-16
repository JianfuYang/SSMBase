package com.gdutyjf.programmer.controller.admin;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.gdutyjf.programmer.entity.admin.Menu;
import com.gdutyjf.programmer.page.admin.Page;
import com.gdutyjf.programmer.service.admin.MenuService;

import org.apache.commons.lang.StringUtils;

/**
 * 	菜单管理控制器
 * @author Jianf
 *
 */
@RequestMapping("admin/menu")
@Controller
public class MenuController {

	@Autowired
	MenuService menuService;
	/**
	 *	菜单管理列表页
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public ModelAndView list(ModelAndView model) {
		model.addObject("topList", menuService.findTopList());
		model.setViewName("/menu/list");
		return model;
	}
	
	/**
	 * 	获取菜单列表
	 * @param page
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/list",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getMenuList(Page page,
			@RequestParam(name = "name",required = false,defaultValue = "")String name
			){
		Map<String, Object> retMap = new HashMap<String, Object>();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("offset", page.getOffset());
		queryMap.put("pageSize", page.getRows());
		queryMap.put("name",name);
		List<Menu> findList = menuService.findList(queryMap);
		
		retMap.put("rows",findList);
		retMap.put("total", menuService.getTotal(queryMap));
		
		return retMap;
	}
	
	/**
	 * 	获取指定目录下的系统图标集合
	 */
	@RequestMapping(value = "/get_icons",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getIconList(HttpServletRequest request){
		Map<String, Object> retMap = new HashMap<String, Object>();
		String realPath = request.getServletContext().getRealPath("/");
		File file = new File(realPath + "\\resources\\admin\\easyui\\css\\icons");
		List<String> icons = new ArrayList<String>();
		if(!file.exists()) {
			retMap.put("type", "error");
			retMap.put("msg", "文件目录不存在");
			return retMap;
		}
		File[] listFiles = file.listFiles();
		for(File f:listFiles) {
			if(f!=null && f.getName().contains("png")){
				icons.add("icon-" + f.getName().substring(0,f.getName().indexOf(".")));
			}
		}
		retMap.put("type", "success");
		retMap.put("content", icons);
		return retMap;
	}
	
	
	/**
	 * 	菜单添加
	 * @param menu
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> add(Menu menu){
		Map<String, String> ret = new HashMap<String, String>();
		if(menu == null){
			ret.put("type", "error");
			ret.put("msg", "请填写正确的菜单信息!");
			return ret;
		}
		if(StringUtils.isEmpty(menu.getName())){
			ret.put("type", "error");
			ret.put("msg", "请填写菜单名称!");
			return ret;
		}
		if(StringUtils.isEmpty(menu.getIcon())){
			ret.put("type", "error");
			ret.put("msg", "请填写菜单图标类!");
			return ret;
		}
		
		if(menu.getParentId() == null){
			menu.setParentId(0l); 
		}
		 
		
		if(menuService.add(menu) <= 0){ 
			ret.put("type", "error"); 
			ret.put("msg","添加失败，请联系管理员!"); 
			return ret; 
		}
		 
		ret.put("type", "success");
		ret.put("msg", "添加成功!");
		return ret;
	}
	
	/**
	 * 	菜单修改
	 * @param menu
	 * @return
	 */
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> edit(Menu menu){
		Map<String, String> ret = new HashMap<String, String>();
		if(menu == null){
			ret.put("type", "error");
			ret.put("msg", "请选择正确的菜单信息!");
			return ret;
		}
		if(StringUtils.isEmpty(menu.getName())){
			ret.put("type", "error");
			ret.put("msg", "请填写菜单名称!");
			return ret;
		}
		if(StringUtils.isEmpty(menu.getIcon())){
			ret.put("type", "error");
			ret.put("msg", "请填写菜单图标类!");
			return ret;
		}
		
		if(menu.getParentId() == null){
			menu.setParentId(0l); 
		}
		 
		
		if(menuService.edit(menu) <= 0){ 
			ret.put("type", "error"); 
			ret.put("msg","修改失败，请联系管理员!"); 
			return ret; 
		}
		 
		ret.put("type", "success");
		ret.put("msg", "修改成功!");
		return ret;
	}
	
	/**
	 * 	删除菜单信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> delete(
			@RequestParam(name="id",required = true)Long id
			){
		Map<String, String> ret = new HashMap<String, String>();
		if(id == null){
			ret.put("type", "error");
			ret.put("msg", "请选择要删除的菜单信息!");
			return ret;
		}
		List<Menu> findChildrenList = menuService.findChildrenList(id);
		if(findChildrenList != null && findChildrenList.size()>0) {
			//表示该分类下存在子分类，不能删除
			ret.put("type", "error");
			ret.put("msg", "该分类下存在子分类，不能删除！");
			return ret;
		}
		if(menuService.delete(id) <= 0) {
			ret.put("type", "error");
			ret.put("msg", "删除失败，请联系管理员！");
			return ret;
		}
		
		ret.put("type", "success");
		ret.put("msg", "删除成功!");
		return ret;
	}
	
	
}
