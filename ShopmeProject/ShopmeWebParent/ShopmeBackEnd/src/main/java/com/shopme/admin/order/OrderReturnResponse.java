package com.shopme.admin.order;

public class OrderReturnResponse {
	
	
	private Integer orderId;
	private String status;
	
	public OrderReturnResponse() { }
	
	
	
	public OrderReturnResponse(Integer orderId, String status) {
		super();
		this.orderId = orderId;
		this.status = status;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public OrderReturnResponse(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}	
}
