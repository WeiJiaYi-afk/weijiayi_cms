package com.weijiayi.cms.service.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
import com.weijiayi.cms.respository.ArticleRepositroy;
import com.weijiayi.cms.service.ArticleService;
import com.weijiayi.common.utils.RandomUtil;
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
	
	@SuppressWarnings("rawtypes")
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private ArticleRepositroy articleRepositroy;

	@Override
	public List<Channel> getChannelAll() {
		return channelDao.select(null);
	}

	@Override
	public List<Category> getCateListByChannelId(Integer channelId) {
		Category category = new Category();
		category.setChannel_id(channelId);
		return categoryDao.select(category);
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
		List<Article> articleList = articleDao.select(article);
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
			Article article = new Article();
			article.setId(Integer.parseInt(id));
			articleRepositroy.delete(article);
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public PageInfo<Article> getHotListByCache(int pageNum, int pageSize){
		List<Article> list = null;
		/** 只缓存第一页 **/
		if(pageNum!=1) {
			return getHotList(pageNum, pageSize);
		}
		/** 设置缓存的Key **/
		String cacheKey = "1710fhotlist:"+pageNum;
		/** redis是否已缓存了数据 **/
		list = ((List<Article>)redisTemplate.opsForValue().get(cacheKey));
		/** 如果已换成数据，则读redis数据直接返回 **/
		if(list!=null && list.size()!=0) {
			System.out.println("从缓存获取热点数据成功");
			return new PageInfo<Article>(list);
		}
		/** 如果未换成数据，则查询数据库，并换成到redis，设置缓存时间 **/
		PageInfo<Article> pageInfo = getHotList(pageNum, pageSize);
		/** 设置缓存 **/
		redisTemplate.opsForValue().set(cacheKey, pageInfo.getList());
		redisTemplate.expire(cacheKey, 60, TimeUnit.SECONDS);
		System.out.println("设置缓存数据成功");
		
		return pageInfo;
	}
	
	@Override
	public PageInfo<Article> getHotList(int pageNum, int pageSize) {
		Article article = new Article();
		article.setStatus(1);
		article.setHot(1);
		PageHelper.startPage(pageNum, pageSize);
		List<Article> articleList = articleDao.select(article);
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
		List<Article> articleList = articleDao.select(article);
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
		boolean result = articleDao.update(article2)>0;
		/** 审核通过，未删除的文章同步到索引库 **/
		article = getById(article.getId());
	    if(article.getStatus()==1 && article.getDeleted()==0) {
	        articleRepositroy.save(article);
	    }else {
	        /** 否则从索引库删除 **/
	        articleRepositroy.delete(article);
	    }
		return result;
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
		return articleDao.select(article);
	}

	@Override
	public List<Article> getRelArticelList(Integer channelId, Integer cateId, Integer articleId, Integer pageSize) {
		Article article = new Article();
		article.setChannel_id(channelId);
//		article.setCategory_id(cateId);
		article.setId(articleId);
		PageHelper.startPage(1, pageSize);
		List<Article> articleList = articleDao.select(article);
		return articleList;
	}

	@Override
	public boolean updateCommentCnt(Integer id) {
		Article article = articleDao.selectById(id);
		article.setCommentCnt(article.getCommentCnt()+1);
		return articleDao.update(article)>0;
	}

	@Override
	public Integer getRandomArticleId() {
		List<Integer> articleIdList = articleDao.selectIdList();
		int random = RandomUtil.random(0, articleIdList.size()-1);
		return articleIdList.get(random);
	}

	@Override
	public Integer getRandomChannelId() {
		List<Integer> channelIdList = channelDao.selectIdList();
		int random = RandomUtil.random(0, channelIdList.size()-1);
		return channelIdList.get(random);
	}

	@Override
	public Integer getRandomCateId(Integer channelId) {
		List<Integer> cateIdList = categoryDao.selectIdList(channelId);
		int random = RandomUtil.random(0, cateIdList.size()-1);
		return cateIdList.get(random);
	}

}
