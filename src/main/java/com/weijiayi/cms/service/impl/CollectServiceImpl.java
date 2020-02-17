package com.weijiayi.cms.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weijiayi.cms.dao.ArticleDao;
import com.weijiayi.cms.dao.CollectDao;
import com.weijiayi.cms.exception.CMSRuntimeException;
import com.weijiayi.cms.pojo.Article;
import com.weijiayi.cms.pojo.Collect;
import com.weijiayi.cms.service.CollectService;
import com.weijiayi.common.utils.StringUtil;

@Service
public class CollectServiceImpl implements CollectService{

	@Resource
	private CollectDao collectDao;
	@Autowired
	private ArticleDao articleDao;
	
	
	@Override
	public int add(Collect collect) throws CMSRuntimeException {
		//判断开头地址http是否符合
		if(StringUtil.isHttpUrl(collect.getUrl())) {
			return collectDao.add(collect);
		}else {
			throw new CMSRuntimeException("URL不合法重新进行添加");
		}
		
	}
	@Override
	public PageInfo<Collect> selectCellects(Integer id, Integer page, Integer pageSize) {
		PageHelper.startPage(page, pageSize);
		List<Collect> list = collectDao.selectCellects(id);
		//根据文章查找查找文章ID
		list.forEach(a->{
			Article cid =	articleDao.selectctId(a.getText());
			a.setCid(cid.getId());
		});
		return new PageInfo<Collect>(list);
	}
	@Override
	public void delete(Integer id) {
		collectDao.delete(id);
		
	}
	@Override
	public List<Collect> selects() {
		return collectDao.selects();
	}
	
	@Override
	public Collect selectText(String text, Integer integer) {
		return collectDao.selectText(text, integer);
	}
	

}
