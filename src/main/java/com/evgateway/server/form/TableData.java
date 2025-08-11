package com.evgateway.server.form;

public class TableData {

	private String order;
	private int pageSize;
	private int pageNumber;

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	@Override
	public String toString() {
		return "TableData [order=" + order + ", pageSize=" + pageSize + ", pageNumber=" + pageNumber + "]";
	}

}
