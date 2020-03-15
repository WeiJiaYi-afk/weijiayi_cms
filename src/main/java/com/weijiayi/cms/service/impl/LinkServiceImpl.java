package com.weijiayi.cms.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weijiayi.cms.dao.LinkDao;
import com.weijiayi.cms.pojo.Link;
import com.weijiayi.cms.service.LinkService;

@Service
public class LinkServiceImpl implements LinkService {
	@Autowired
	private LinkDao linkDao;

	@Override
	public boolean save(Link link) {
		if(link.getId()==null) {
			link.setCreated(new Date());
			return linkDao.insert(link)>0;
		}
		/** 不修改创建时间 **/
		Link link2 = linkDao.selectById(link.getId());
		link.setCreated(link2.getCreated());
		return linkDao.update(link)>0;
	}

	@Override
	public PageInfo<Link> getPageInfo(Link link, Integer pageNum) {
		PageHelper.startPage(pageNum, 6);
		List<Link> linkList = linkDao.select(link);
		return new PageInfo<>(linkList);
	}

	@Override
	public Link getById(Integer id) {
		return linkDao.selectById(id);
	}

	@Override
	public List<Link> getLinkListAll() {
		return linkDao.select(null);
	}

}
