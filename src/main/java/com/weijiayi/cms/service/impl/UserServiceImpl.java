package com.weijiayi.cms.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weijiayi.cms.common.CmsMd5Util;
import com.weijiayi.cms.dao.UserDao;
import com.weijiayi.cms.pojo.User;
import com.weijiayi.cms.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao userDao;

	@Override
	public boolean register(User user) {
		/** 设置密码 **/
		user.setPassword(CmsMd5Util.md5(user.getPassword()));
		user.setLocked(0);
		user.setScore(0);
		user.setRole("0");
		user.setCreate_time(new Date());
		user.setUpdate_time(new Date());
		return userDao.insert(user)>0;
	}

	@Override
	public User getByUsername(String userName) {
		return userDao.selectByUsername(userName);
	}

	@Override
	public boolean locked(String userName) {
		User userInfo = userDao.selectByUsername(userName);
		return userInfo.getLocked()==1;
	}

	@Override
	public boolean set(User user) {
		User userInfo = userDao.selectById(user.getId());
		userInfo.setHeadimg(user.getHeadimg());
		userInfo.setNickname(user.getNickname());
		return userDao.update(userInfo)>0;
	}

	@Override
	public User getById(Integer id) {
		return userDao.selectById(id);
	}

	@Override
	public PageInfo<User> getPageInfo(User user, Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<User> userList = userDao.selects(user);
		return new PageInfo<>(userList);
	}

	@Override
	public boolean updateLocked(Integer id) {
		User user = userDao.selectById(id);
		if(user.getLocked()==0) {
			user.setLocked(1);
		}else {
			user.setLocked(0);
		}
		return userDao.update(user)>0;
	}

}
