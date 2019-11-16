package com.gdutyjf.programmer.service.admin;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gdutyjf.programmer.entity.admin.Menu;

/**
 * 菜单管理service
 * @author Jianf
 *
 */
@Service
public interface MenuService {
	
	public int add(Menu  menu);
	public List<Menu> findList(Map<String, Object> queryMap);
	public List<Menu> findTopList();
	public int getTotal(Map<String, Object> queryMap); //获取总数
	public int edit(Menu menu);
	public int delete(Long id);
	public List<Menu> findChildrenList(Long parentId);
	public List<Menu> findListByIds(String ids);
	

}
