package com.linng.www.util;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

public class Page<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<T> result = Lists.newArrayList();
	private int totalPage = 0; //共多少页数据
	private int totalCount = 0;//共多少条数据
	private int pageSize = 20;//默认20条
	private int currentPage = 1;//当前页
	
	private int nextPage;//下一页
	private int prevPage;//上一页
	private boolean hasNextPage = true;
	private boolean hasPrevPage = true;
	
	public int getNextPage() {
		if (getTotalPage() > getCurrentPage()) {
			nextPage = getCurrentPage() + 1;
		}
		return nextPage;
	}
	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}
	public int getPrevPage() {
		if (getCurrentPage() > 1) {
			prevPage = getCurrentPage() - 1;
		}
		return prevPage;
	}
	public void setPrevPage(int prevPage) {
		this.prevPage = prevPage;
	}
	public boolean getHasNextPage() {
		hasNextPage = getTotalPage() > getCurrentPage();
		return hasNextPage;
	}
	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}
	public boolean getHasPrevPage() {
		hasPrevPage =  getCurrentPage() > 1;
		return hasPrevPage;
	}
	public void setHasPrevPage(boolean hasPrevPage) {
		this.hasPrevPage = hasPrevPage;
	}
	private Map<String, Object> params = new LinkedHashMap<>();
	
	public  int getFirstPage(){
		currentPage=1;
		return currentPage;
	}
	public int getFirst() {
		return ((currentPage - 1) * pageSize);
	}
	
	public List<T> getResult() {
		return result;
	}
	public void setResult(List<T> result) {
		this.result = result;
	}
	public int getTotalPage() {
		totalPage = totalCount / pageSize;
		if ((totalCount % pageSize ) > 0){
			++totalPage;
		}
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public Map<String, Object> getParams() {
		return params;
	}
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
}
