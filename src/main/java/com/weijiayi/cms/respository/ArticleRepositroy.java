package com.weijiayi.cms.respository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.weijiayi.cms.pojo.Article;

public interface ArticleRepositroy extends ElasticsearchRepository<Article, Integer>{

}
