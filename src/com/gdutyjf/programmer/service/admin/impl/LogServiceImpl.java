package com.gdutyjf.programmer.service.admin.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdutyjf.programmer.dao.admin.LogDao;
import com.gdutyjf.programmer.entity.admin.Log;
import com.gdutyjf.programmer.service.admin.LogService;


@Service
public class LogServiceImpl implements LogService {

	@Autowired
	LogDao logDao;
	
	@Override
	public int add(Log log) {
		
		return logDao.add(log);
	}

	@Override
	public List<Log> findList(Map<String, Object> queryMap) {
		
		return logDao.findList(queryMap);
	}

	@Override
	public int getTotal(Map<String, Object> queryMap) {
		// 
		return logDao.getTotal(queryMap);
	}

	@Override
	public int delete(String ids) {
		// 
		return logDao.delete(ids);
	}

	@Override
	public int add(String content) {
		Log log = new Log();
		//添加日志
		log.setContent(content);
		log.setCreateTime(new Date());
		return logDao.add(log);
	}

}
