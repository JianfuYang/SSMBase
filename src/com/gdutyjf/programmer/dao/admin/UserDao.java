package com.gdutyjf.programmer.dao.admin;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gdutyjf.programmer.entity.admin.User;

/**
 * user用户的dao
 * @author Jianf
 *
 */
@Repository
public interface UserDao {

	public User findByUsername(String username); 
	public int add(User user);
	public int edit(User user);
	public int editPassword(User user);
	public int delete(String ids);
	public List<User> findList(Map<String, Object> queryMap);
	public int getTotal(Map<String, Object> queryMap);
	
}
