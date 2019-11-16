package com.gdutyjf.programmer.service.admin;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gdutyjf.programmer.entity.admin.Authority;

/**
 * 	权限Service接口
 * @author Jianf
 *
 */
@Service
public interface AuthorityService {
	
	public int add(Authority authority);
	public int deleteByRoleId(Long roleId);
	public List<Authority> findListByRoleId(Long roleId);
	

}
