package com.wjy.pojo;

public class Article_tag {

	/** 文章Id **/
	private Integer aid;
	/** 标签Id **/
	private Integer tid;
	
	@Override
	public String toString() {
		return "Article_tag [aid=" + aid + ", tid=" + tid + "]";
	}

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public Integer getTid() {
		return tid;
	}

	public void setTid(Integer tid) {
		this.tid = tid;
	}

	
}
