package com.wjy.test;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.pagehelper.PageInfo;
import com.weijiayi.cms.pojo.Comment;
import com.weijiayi.cms.service.ArticleService;
import com.weijiayi.cms.service.CommentService;
import com.weijiayi.cms.service.UserService;
import com.weijiayi.common.utils.DateUtil;
import com.weijiayi.common.utils.RandomUtil;
import com.weijiayi.common.utils.StringUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:/spring-beans.xml")
public class CommentTests {
	@Autowired
	private CommentService commentService;
	@Autowired
	private ArticleService articleService;
	@Autowired
	private UserService userService;
	
	@Test
	public void add() {
		Comment comment = new Comment();
		comment.setArticleId(160);
		comment.setContent("评论测试");
		comment.setUserId(223);
		commentService.add(comment);
	}
	
	@Test
	public void getPageInfo() {
		PageInfo<Comment> pageInfo = commentService.getPageInfo(160, 1, 6);
		System.out.println(pageInfo.getList().get(0));
	}
	@Test
	public void getRandom() {
		for (int i = 0; i < 100; i++) {
			Integer randomArticleId = articleService.getRandomArticleId();
			System.out.println(randomArticleId);
			Integer randomUserId = userService.getRandomUserId();
			System.out.println(randomUserId);
			int random = RandomUtil.random(100, 200);
			String randomChineseString = StringUtil.randomChineseString(random);
			System.out.println(randomChineseString);
			Date date1 = DateUtil.parse("2020-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
			Date date2 = new Date();
			Date randomDate = DateUtil.getRandomDate(date1, date2);
			System.out.println(DateUtil.format(randomDate,"yyyy-MM-dd HH:mm:ss"));
		}
	}
	
	@Test
	public void randomComment() {
		for (int i = 0; i < 1000; i++) {
			Comment randomComment = commentService.getRandomComment();
			System.out.println(randomComment);
			commentService.add(randomComment);
		}
	}
}
