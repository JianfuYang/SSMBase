package com.gdutyjf.programmer.service.admin.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdutyjf.programmer.dao.admin.RoleDao;
import com.gdutyjf.programmer.entity.admin.Role;
import com.gdutyjf.programmer.service.admin.RoleService;


@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDao roleDao;
	
	@Override
	public int add(Role role) {
		// 
		return roleDao.add(role);
	}

	@Override
	public int edit(Role role) {
		// 
		return roleDao.edit(role);
	}

	@Override
	public int delete(Long id) {
		// 
		return roleDao.delete(id);
	}

	@Override
	public List<Role> findList(Map<String, Object> qureMap) {
		// 
		return roleDao.findList(qureMap);
	}

	@Override
	public int getTotal(Map<String, Object> qureMap) {
		// 
		return roleDao.getTotal(qureMap);
	}

	@Override
	public Role find(Long id) {
		// 
		return roleDao.find(id);
	}

}
