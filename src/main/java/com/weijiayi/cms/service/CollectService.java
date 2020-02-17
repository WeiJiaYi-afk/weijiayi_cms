package com.weijiayi.cms.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.PageInfo;
import com.weijiayi.cms.exception.CMSRuntimeException;
import com.weijiayi.cms.pojo.Collect;


public interface CollectService {

	int add(Collect collect) throws CMSRuntimeException;

	PageInfo<Collect> selectCellects(Integer id, Integer page, Integer pageSize);

	void delete(Integer id);

	List<Collect> selects();

	Collect selectText(String text, Integer integer);
}
