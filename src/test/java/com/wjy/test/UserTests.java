package com.wjy.test;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wjy.dao.UserDao;
import com.wjy.pojo.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-beans.xml")
public class UserTests {

	@Resource
	private UserDao userDao;
	
	@Test
	public void test() {
		
		//查询
		List<User> userList = userDao.selects(null);
		System.out.println(userList);
		
		//列表展示
		List<User> selects = userDao.selects(new User());
		for (User user : selects) {
			System.out.println(user);
		}
		
		//根据id查询
		User selectById = userDao.selectById(201);
		System.out.println(selectById);
		
		//添加
		Date date = new Date();
		User user = new User();
		user.setUsername("wjy");
		user.setPassword("666");
		user.setNickname("Wjy");
//		userDao.insert(user);
				
		//修改
		User u1 = new User();
		u1.setId(198);
		u1.setUsername("wjyy");
		u1.setPassword("6666");
		u1.setNickname("WjyW");
//		userDao.update(u1);
		
		//删除
		userDao.delete("201");
		
	}
	
}
