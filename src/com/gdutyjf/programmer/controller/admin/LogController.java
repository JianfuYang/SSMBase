package com.gdutyjf.programmer.controller.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.gdutyjf.programmer.entity.admin.Log;
import com.gdutyjf.programmer.page.admin.Page;
import com.gdutyjf.programmer.service.admin.LogService;

/**
 * 日志管理控制器
 * @author Jianf
 *
 */
@RequestMapping("/admin/log")
@Controller
public class LogController {

	@Autowired
	private LogService logService;
	
	/**
	 * 	日志列表页面
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public ModelAndView list(ModelAndView modelAndView) {
		
	
		modelAndView.setViewName("log/list");
		
		return modelAndView;
	}
	
	/**
	 *  获取日志列表
	 * @param page
	 * @param username
	 * @param roleId
	 * @param sex
	 * @return
	 */
	@RequestMapping(value = "/list",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getList(Page page,
			@RequestParam(name = "content",required = false,defaultValue = "") String content
			
			){
		Map<String, Object> reMap = new HashMap<String, Object>();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("content", content);
		queryMap.put("offset", page.getOffset());
		queryMap.put("pageSize", page.getRows());
		reMap.put("rows", logService.findList(queryMap));
		reMap.put("total",logService.getTotal(queryMap));
		return reMap;
	}
	
	/**
	 * 添加日志
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/add",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> add(Log log){
		Map<String, String> retMap = new HashMap<String, String>();
		if(log == null) {
			retMap.put("type", "error");
			retMap.put("msg", "请填写正确的日志信息！");
			return retMap;
		}
		
		if(StringUtils.isEmpty(log.getContent())) {
			retMap.put("type", "error");
			retMap.put("msg", "请填写日志内容！");
			return retMap;
		}
		log.setCreateTime(new Date());
		if(logService.add(log) <= 0) {
			retMap.put("type", "error");
			retMap.put("msg", "日志添加失败，请联系管理员！");
			return retMap;
		}
		
		retMap.put("type", "success");
		retMap.put("msg", "添加成功");
		return retMap;
	}
	
	/**
	 *  批量删除日志
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
		
		if(logService.delete(ids) <= 0) {
			retMap.put("type", "error");
			retMap.put("msg", "日志删除失败，请联系管理员！");
			return retMap;
		}
		
		retMap.put("type", "success");
		retMap.put("msg", "日志删除成功！");
		return retMap;
	}
	
	
}
