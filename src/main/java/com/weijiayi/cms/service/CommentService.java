package com.weijiayi.cms.service;

import com.github.pagehelper.PageInfo;
import com.weijiayi.cms.pojo.Comment;


public interface CommentService {


	void addComment(Comment comment);

	PageInfo<Comment> selectService(Integer page, Integer pageSize, Integer id);
}
