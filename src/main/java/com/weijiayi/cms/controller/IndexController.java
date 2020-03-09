package com.weijiayi.cms.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.weijiayi.cms.common.CmsConst;
import com.weijiayi.cms.pojo.Article;
import com.weijiayi.cms.pojo.Category;
import com.weijiayi.cms.pojo.Channel;
import com.weijiayi.cms.pojo.Comment;
import com.weijiayi.cms.pojo.Link;
import com.weijiayi.cms.pojo.Slide;
import com.weijiayi.cms.pojo.User;
import com.weijiayi.cms.service.ArticleService;
import com.weijiayi.cms.service.CommentService;
import com.weijiayi.cms.service.LinkService;
import com.weijiayi.cms.service.SlideService;
import com.weijiayi.cms.service.UserService;

@Controller
public class IndexController {
	@Autowired
	private ArticleService articleService;
	@Autowired
	private SlideService slideService;
	@Autowired
	private UserService userService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private LinkService linkService;
	/**
	 * @Title: index   
	 * @Description: 首页   
	 * @param: @param model
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	@RequestMapping("/")
	public String index(Model model) {
		hot(model, 1);
		return "index";
	}
	/**
	 * @Title: hot   
	 * @Description: 热门分页   
	 * @param: @param model
	 * @param: @param pageNum
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	@RequestMapping("/hot/{pageNum}.html")
	public String hot(Model model,@PathVariable Integer pageNum) {
		List<Channel> channelList = articleService.getChannelAll();
		List<Slide> slideList = slideService.getAll();
		PageInfo<Article> pageInfo = articleService.getHotList(pageNum,4);
		//最新文章
		List<Article> newArticleList = articleService.getNewList(6);
		//最热文章
		List<Article> srticleList = articleService.getHotList(20);
		//最新图片
		List<Article> newAritcleImage = articleService.getNewImage(pageNum);
		//添加友情链接
		List<Link> LinkList = linkService.selects();
		model.addAttribute("LinkList", LinkList);
		model.addAttribute("channelList", channelList);
		model.addAttribute("slideList", slideList);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("newArticleList", newArticleList);
		model.addAttribute("ArticleList", srticleList);
		model.addAttribute("newAritcleImage", newAritcleImage);
		return "index";
	}
	
	/**
	 * @Title: channel   
	 * @Description: 频道页   
	 * @param: @param model
	 * @param: @param channelId
	 * @param: @param cateId
	 * @param: @param pageNum
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	@RequestMapping("/{channelId}/{cateId}/{pageNum}.html")
	public String channel(Model model,@PathVariable Integer channelId,@PathVariable Integer cateId,@PathVariable Integer pageNum) {
		List<Channel> channelList = articleService.getChannelAll();
		List<Slide> slideList = slideService.getAll();
		PageInfo<Article> pageInfo = articleService.getList(channelId,cateId,pageNum,2);
		List<Category> cateList = articleService.getCateListByChannelId(channelId);
		Channel channel = articleService.getChannelByChannelId(channelId);
		//最新文章
		List<Article> newArticleList = articleService.getNewList(6);
		//最新图片
		List<Article> newAritcleImage = articleService.getNewList(10);
		//查最热文章
		List<Article> srticleList = articleService.getHotList(20);
		model.addAttribute("channelList", channelList);
		model.addAttribute("cateList", cateList);
		model.addAttribute("slideList", slideList);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("channel", channel);
		model.addAttribute("newArticleList", newArticleList);
		model.addAttribute("ArticleList", srticleList);
		model.addAttribute("newAritcleImage", newAritcleImage);
		return "index";
	}
	/**
	 * @Title: articleDetail   
	 * @Description: 文章详情页  
	 * @param: @param id
	 * @param: @param model
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	@RequestMapping("/article/detail/{id}.html")
	public String articleDetail(@PathVariable Integer id,Model model,@RequestParam(defaultValue = "1")Integer page,@RequestParam(defaultValue = "3")Integer pageSize,HttpSession session) {
		Article article = articleService.getById(id);
		System.out.println("文章详情页："+article);
		User user = userService.getById(article.getUser_id());
		System.out.println("文章详情页："+user);
		article.setNickname(user.getNickname());
		model.addAttribute("article", article);
		//添加友情链接
		List<Link> LinkList = linkService.selects();
		/** 设置文章点击量，若点击量大于20成为热点文章 **/
		articleService.setHitsAndHot(id);
		//查找相关文章
		List<Article> newArticleList = articleService.selects(id);
		List<Article> newAritcleImage = articleService.selects(id);
		model.addAttribute("newArticleList", newArticleList);
		model.addAttribute("newAritcleImage", newAritcleImage);
		model.addAttribute("article", article);
		model.addAttribute("user", user);
		model.addAttribute("id",id);
		model.addAttribute("LinkList",LinkList);
		User us = (User) session.getAttribute(CmsConst.UserSessionKey);
		model.addAttribute("us", us);
		//进行添加评论
		PageInfo<Comment> info = commentService.selectService(page,pageSize,id);
		model.addAttribute("info", info);
		return "article-detail";
	}
}
