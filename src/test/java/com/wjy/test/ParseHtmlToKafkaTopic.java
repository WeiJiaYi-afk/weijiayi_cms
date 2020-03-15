package com.wjy.test;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.weijiayi.cms.pojo.Article;
import com.weijiayi.cms.service.ArticleService;
import com.weijiayi.cms.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:/spring-beans.xml")
public class ParseHtmlToKafkaTopic {
	@Autowired
	private ArticleService articleService;
	@Autowired
	private UserService userService;
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	/** 解析html，封装成Article对象，发送到kafka的topic 
	 * @throws IOException **/
	@Test
	public void parseHtmlToKafkaTopic() throws IOException {
		/** 读取文件 **/
		File htmlFileDir = new File("C:\\html\\");
		File[] listFiles = htmlFileDir.listFiles();
		for (File htmlFile : listFiles) {
			Article article = new Article();
			/** Jsoup解析html，获取标题、文章内容、图片 **/
			Document htmlPage = Jsoup.parse(htmlFile, "utf-8");
			/** 解析title **/
			Elements titleE = htmlPage.select(".post_content_main h1");
			String title = titleE.text();
			article.setTitle(title);
			/** 解析content **/
			Elements contentE = htmlPage.select(".post_text");
			String content = contentE.html();
			article.setContent(content);
			/** 解析图片 **/
			Elements imgE = contentE.select("img");
			if(imgE.size()>0) {
				String pic = imgE.get(0).attr("src");
				article.setPicture(pic);
			}else {
				continue;
			}
			/** 随机生成频道id、分类id、用户Id **/
			Integer channelId = articleService.getRandomChannelId();
			Integer cateId = articleService.getRandomCateId(channelId);
			Integer userId = userService.getRandomUserId();
			article.setChannel_id(channelId);
			article.setCategory_id(cateId);
			article.setUser_id(userId);
			/** 设置文章对象的默认值：状态、删除、创建时间、更新时间 **/
			article.setStatus(1);
			/** 把文章Article转Json字符串发送到kakfa的topic **/
			String articleJsonStr = JSON.toJSONString(article);
			kafkaTemplate.send("articleTopic", articleJsonStr);
		}
		
	}
}
