package com.gdutyjf.programmer.service.admin;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gdutyjf.programmer.entity.admin.Role;

/**
 * 	角色role Service
 * @author Jianf
 *
 */
@Service
public interface RoleService {

	public int add(Role role);
	public int edit(Role role);
	public int delete(Long id);
	public List<Role> findList(Map<String, Object> qureMap);
	public int getTotal(Map<String, Object> qureMap);
	public Role find(Long id);
}
