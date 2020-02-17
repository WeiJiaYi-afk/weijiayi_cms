package com.weijiayi.cms.dao;

import java.util.List;

import com.weijiayi.cms.pojo.Link;

public interface LinkDao extends BaseDao<Link>{

	List<Link> selects();
}
