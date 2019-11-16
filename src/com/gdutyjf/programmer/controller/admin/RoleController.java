package com.gdutyjf.programmer.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.gdutyjf.programmer.entity.admin.Authority;
import com.gdutyjf.programmer.entity.admin.Menu;
import com.gdutyjf.programmer.entity.admin.Role;
import com.gdutyjf.programmer.page.admin.Page;
import com.gdutyjf.programmer.service.admin.AuthorityService;
import com.gdutyjf.programmer.service.admin.MenuService;
import com.gdutyjf.programmer.service.admin.RoleService;

/**
 * 	角色role控制器
 * @author Jianf
 *
 */
@RequestMapping("/admin/role")
@Controller
public class RoleController {
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private MenuService menuService;
	
	/**
	 * 	角色列表页面
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public ModelAndView list(ModelAndView modelAndView) {
		modelAndView.setViewName("/role/list");
		return modelAndView;
	}
	
	
	/**
	 * 	获取角色列表
	 * @param page
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/list",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getList(Page page,
			@RequestParam(name = "name",required = false,defaultValue = "") String name
			){
		Map<String, Object> reMap = new HashMap<String, Object>();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("name", name);
		queryMap.put("offset", page.getOffset());
		queryMap.put("pageSize", page.getRows());
		reMap.put("rows", roleService.findList(queryMap));
		reMap.put("total", roleService.getTotal(queryMap));
		return reMap;
	}
	
	
	/**
	 * 	角色添加
	 * @param role
	 * @return
	 */
	@RequestMapping(value = "/add",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> add(Role role){
		Map<String, String> retMap = new HashMap<String, String>();
		if(role == null) {
			retMap.put("type", "error");
			retMap.put("msg", "请填写正确的角色信息！");
			return retMap;
		}
		
		if(StringUtils.isEmpty(role.getName())) {
			retMap.put("type", "error");
			retMap.put("msg", "请填写角色名称！");
			return retMap;
		}
		if(roleService.add(role) <= 0) {
			retMap.put("type", "error");
			retMap.put("msg", "角色添加失败，请联系管理员！");
			return retMap;
		}
		
		retMap.put("type", "success");
		retMap.put("msg", "添加成功");
		return retMap;
	}
	
	/**
	 * 	角色修改
	 * @param role
	 * @return
	 */
	@RequestMapping(value = "/edit",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> edit(Role role){
		Map<String, String> retMap = new HashMap<String, String>();
		if(role == null) {
			retMap.put("type", "error");
			retMap.put("msg", "请填写正确的角色信息！");
			return retMap;
		}
		
		if(StringUtils.isEmpty(role.getName())) {
			retMap.put("type", "error");
			retMap.put("msg", "请填写角色名称！");
			return retMap;
		}
		if(roleService.edit(role) <= 0) {
			retMap.put("type", "error");
			retMap.put("msg", "角色修改失败，请联系管理员！");
			return retMap;
		}
		
		retMap.put("type", "success");
		retMap.put("msg", "修改成功");
		return retMap;
	}
	
	/**
	 * 	删除角色信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> delete(Long id){
		Map<String, String> retMap = new HashMap<String, String>();
		if(id == null) {
			retMap.put("type", "error");
			retMap.put("msg", "请选择要删除的角色");
			return retMap;
		}
		
		try {
			if(roleService.delete(id) <= 0) {
				retMap.put("type", "error");
				retMap.put("msg", "删除失败，请联系管理员！");
				return retMap;
			}
		}catch (Exception e) {
			retMap.put("type", "error");
			retMap.put("msg", "该角色下存在权限或用户信息，不能删除！");
			return retMap;
		}
		
		retMap.put("type", "success");
		retMap.put("msg", "角色删除成功！");
		return retMap;
	}
	
	/**
	 * 	获取所有的菜单信息
	 * @return
	 */
	@RequestMapping(value = "/get_all_menu",method = RequestMethod.POST)
	@ResponseBody
	public List<Menu> getAllMenu(){
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("offset", 0);
		queryMap.put("pageSize", 9999);
		return menuService.findList(queryMap);
	}
	
	/**
	 * 	添加权限
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/add_authority",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> addAuthority(@RequestParam(name = "ids",required = true) String ids,
			@RequestParam(name = "roleId",required = true) Long roleId
			){
		Map<String, String> ret = new HashMap<String, String>();
		if(StringUtils.isEmpty(ids)) {
			ret.put("type", "error");
			ret.put("msg", "请选择相应的权限！");
			return ret;
		}
		if(roleId == null) {
			ret.put("type", "error");
			ret.put("msg", "请选择相应的角色！");
			return ret;
		}
		if(ids.contains(",")) {
			ids = ids.substring(0,ids.length()-1);
		}
		String[] idArr = ids.split(",");
		//先删掉所有的权限
		if(idArr.length > 0) {
			authorityService.deleteByRoleId(roleId);
		}
		for(String id:idArr) {
			Authority authority = new Authority();
			authority.setMenuId(Long.valueOf(id));
			authority.setRoleId(roleId);
			authorityService.add(authority);
			
		}
		
		ret.put("type", "success");
		ret.put("msg", "权限编辑成功！");
		return ret;
	}
	
	
	/**
	 * 	获取某个角色的所有权限
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value = "/get_role_authority",method = RequestMethod.POST)
	@ResponseBody
	public List<Authority> getRoleAuthority(@RequestParam(name = "roleId",required = true) Long roleId
			){
		
		return authorityService.findListByRoleId(roleId);
	}

	
	

}
