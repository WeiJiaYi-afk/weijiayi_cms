package com.weijiayi.cms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.weijiayi.cms.pojo.Channel;

public interface ChannelDao extends BaseDao<Channel>{
	@Select("SELECT id FROM cms_channel")
	List<Integer> selectIdList();
}
