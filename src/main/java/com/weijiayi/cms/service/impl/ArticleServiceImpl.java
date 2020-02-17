package com.weijiayi.cms.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weijiayi.cms.dao.ArticleDao;
import com.weijiayi.cms.dao.CategoryDao;
import com.weijiayi.cms.dao.ChannelDao;
import com.weijiayi.cms.dao.UserDao;
import com.weijiayi.cms.pojo.Article;
import com.weijiayi.cms.pojo.Category;
import com.weijiayi.cms.pojo.Channel;
import com.weijiayi.cms.pojo.User;
import com.weijiayi.cms.service.ArticleService;

@Service
public class ArticleServiceImpl implements ArticleService{
	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private ChannelDao channelDao;
	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private UserDao userDao;

	@Override
	public List<Channel> getChannelAll() {
		return channelDao.selects(null);
	}

	@Override
	public List<Category> getCateListByChannelId(Integer channelId) {
		Category category = new Category();
		category.setChannel_id(channelId);
		return categoryDao.selects(category);
	}

	@Override
	public Article getById(Integer id) {
		return articleDao.selectById(id);
	}

	@Override
	public boolean save(Article article) {
		
		if(article.getId()==null) {
			/** 设置默认值 **/
			article.setHits(0);
			article.setHot(0);
			article.setDeleted(0);
			article.setTousuCnt(0);
			article.setCommentCnt(0);
			article.setCreated(new Date());
			article.setUpdated(new Date());
			/** 新增 **/
			articleDao.insert(article);
		}else {
			/** 修改 **/
			article.setUpdated(new Date());
			Article a = articleDao.selectById(article.getId());
			a.setTitle(article.getTitle());
			a.setPicture(article.getPicture());
			a.setChannel_id(article.getChannel_id());
			a.setCategory_id(article.getCategory_id());
			a.setContent(article.getContent());
			a.setStatus(article.getStatus());
			articleDao.update(a);
		}
		return true;
	}

	@Override
	public PageInfo<Article> getPageInfo(Article article, Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Article> articleList = articleDao.selects(article);
		/** 设置频道和分类的名称 **/
		articleList.forEach(a->{
			Channel channel = channelDao.selectById(a.getChannel_id());
			a.setChannel_name(channel.getName());
			Category cate = categoryDao.selectById(a.getCategory_id());
			a.setCategory_name(cate.getName());
			User user = userDao.selectById(a.getUser_id());
			a.setNickname(user.getNickname());
		});
		return new PageInfo<>(articleList);
	}

	@Override
	public boolean deleteById(Integer id) {
		Article article = articleDao.selectById(id);
		article.setDeleted(1);
		return articleDao.update(article)>0;
	}
	@Override
	public boolean deleteByIds(String ids) {
		String[] idArr = ids.split(",");
		for(String id:idArr) {
			deleteById(Integer.parseInt(id));
		}
		return true;
	}

	@Override
	public PageInfo<Article> getHotList(int pageNum, int pageSize) {
		Article article = new Article();
		article.setStatus(1);
		article.setHot(1);
		PageHelper.startPage(pageNum, pageSize);
		List<Article> articleList = articleDao.selects(article);
		articleList.forEach(a->{
			User user = userDao.selectById(a.getUser_id());
			a.setNickname(user.getNickname());
		});
		return new PageInfo<>(articleList);
	}

	@Override
	public PageInfo<Article> getList(Integer channelId, Integer cateId, Integer pageNum, Integer pageSize) {
		Article article = new Article();
		article.setStatus(1);
		article.setChannel_id(channelId);
		if(cateId>0) {
			article.setCategory_id(cateId);
		}
		PageHelper.startPage(pageNum, pageSize);
		List<Article> articleList = articleDao.selects(article);
		articleList.forEach(a->{
			User user = userDao.selectById(a.getUser_id());
			a.setNickname(user.getNickname());
		});
		return new PageInfo<>(articleList);
	}

	@Override
	public Channel getChannelByChannelId(Integer channelId) {
		return channelDao.selectById(channelId);
	}

	@Override
	public boolean check(Article article) {
		Article article2 = articleDao.selectById(article.getId());
		article2.setStatus(article.getStatus());
		return articleDao.update(article2)>0;
	}

	@Override
	public void setHitsAndHot(Integer id) {
		Article article = articleDao.selectById(id);
		article.setHits(article.getHits()+1);
		if(article.getHits()>=20) {
			article.setHot(1);
		}
		articleDao.update(article);
	}

	@Override
	public List<Article> getNewList(Integer pageSize) {
		PageHelper.startPage(1, pageSize);
		Article article = new Article();
		article.setStatus(1);
		return articleDao.selects(article);
	}

	//查找相关文章
	@Override
	public List<Article> selects(Integer id) {
		//先查找里面频道id
		Article article = articleDao.selectById(id);
		System.out.println(article);
		return articleDao.select(id,article.getChannel_id());
	}

	/**
	 * 查找最热文章
	 */
	@Override
	public List<Article> getHotList(Integer pageSize) {
		PageHelper.startPage(1, pageSize);
		List<Article> list = articleDao.gethotselect();
		for (Article article : list) {
			System.out.println("查找到的"+article);
		}
		return list;
	}

}
