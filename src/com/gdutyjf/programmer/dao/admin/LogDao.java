package com.gdutyjf.programmer.dao.admin;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gdutyjf.programmer.entity.admin.Log;

/** 
 *  系统日志
 * @author Jianf
 *
 */
@Repository
public interface LogDao {

	public int add(Log log);
	public List<Log> findList(Map<String, Object> queryMap);
	public int getTotal(Map<String, Object> queryMap);
	public int delete(String ids);
	
}
