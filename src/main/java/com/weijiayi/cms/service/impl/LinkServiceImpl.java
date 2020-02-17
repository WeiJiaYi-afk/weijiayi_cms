package com.weijiayi.cms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weijiayi.cms.dao.LinkDao;
import com.weijiayi.cms.pojo.Link;
import com.weijiayi.cms.service.LinkService;

@Service
public class LinkServiceImpl implements LinkService{

	@Autowired
	private LinkDao linkDao;
	
	@Override
	public List<Link> selects() {
		return linkDao.selects();
	}

}
