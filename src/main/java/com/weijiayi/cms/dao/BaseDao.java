package com.wjy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wjy.pojo.User;

public interface BaseDao<T>{

	//查询
	List<T> selects(T t);
	//根据id查询
	T selectById(Integer id);
	//添加
	int insert(T t);
	//修改
	int update(T t);
	//删除
	int delete(@Param("ids")String ids);
	
}
