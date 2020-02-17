package com.weijiayi.cms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.weijiayi.cms.pojo.Collect;

public interface CollectDao extends BaseDao<Collect>{

	int add(Collect collect);

	void delete(Integer id);

	List<Collect> selects();

	Collect selectText(@Param("text")String text, @Param("integer")Integer integer);

	List<Collect> selectCellects(Integer id);
}
