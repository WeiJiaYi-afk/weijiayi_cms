package com.weijiayi.cms.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weijiayi.cms.dao.CommentDao;
import com.weijiayi.cms.dao.UserDao;
import com.weijiayi.cms.pojo.Comment;
import com.weijiayi.cms.pojo.User;
import com.weijiayi.cms.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService{

	@Resource
	private CommentDao commentDao;
	@Autowired
	private UserDao userDao;
	
	@Override
	public void addComment(Comment comment) {
		commentDao.addComment(comment);
		
	}
	@Override
	public PageInfo<Comment> selectService(Integer page, Integer pageSize, Integer id) {
		PageHelper.startPage(page, pageSize);
		List<Comment> list =  commentDao.selectComment(id);
		list.forEach(c->{
			User user = userDao.selectById(c.getUserId());
			c.setCname(user.getUsername());
		});
		return new PageInfo<Comment>(list);
	}

}
