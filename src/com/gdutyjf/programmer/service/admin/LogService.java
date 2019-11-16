package com.gdutyjf.programmer.service.admin;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gdutyjf.programmer.entity.admin.Log;

/**
 *  日志接口
 * @author Jianf
 *
 */
@Service
public interface LogService {
	
	public int add(Log log);
	public int add(String content);  //此函数操作了add(Log log)函数，没有直接对应的mapper直接操作数据库
	public List<Log> findList(Map<String, Object> queryMap);
	public int getTotal(Map<String, Object> queryMap);
	public int delete(String ids);
}
