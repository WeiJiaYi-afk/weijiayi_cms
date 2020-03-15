package com.wjy.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.weijiayi.cms.dao.ArticleDao;
import com.weijiayi.cms.dao.ChannelDao;
import com.weijiayi.cms.dao.UserDao;
import com.weijiayi.cms.pojo.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:spring-beans.xml")
public class UserTests {
	@Autowired
	private UserDao userDao;
	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private ChannelDao channelDao;
	
	@Test
	public void test() {
		List<User> userList = userDao.select(null);
		System.out.println(userList);
		channelDao.select(null);
		
		User user = new User();
		user.setNickname("lisi");
//		userDao.insert(user);
		user.setId(200);
//		userDao.update(user);
		
//		userDao.delete("200");
		
		User selectById = userDao.selectById(199);
		System.out.println(selectById);
	}
}
