package com.gdutyjf.programmer.dao.admin;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gdutyjf.programmer.entity.admin.Role;

/**
 * 	角色role dao
 * @author Jianf
 *
 */
@Repository
public interface RoleDao {

	public int add(Role role);
	public int edit(Role role);
	public int delete(Long id);
	public List<Role> findList(Map<String, Object> qureMap);
	public int getTotal(Map<String, Object> qureMap);
	public Role find(Long id);
	
}
