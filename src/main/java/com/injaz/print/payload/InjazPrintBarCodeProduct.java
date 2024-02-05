package com.injaz.print.payload;

import java.io.Serializable;

public class InjazPrintBarCodeProduct implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String nameAr;
	private String client;
	private String orderNo;
	
	
	public InjazPrintBarCodeProduct() {
		// TODO Auto-generated constructor stub
	}


	public InjazPrintBarCodeProduct(String name, String nameAr, String client, String orderNo) {
		super();
		this.name = name;
		this.nameAr = nameAr;
		this.client = client;
		this.orderNo = orderNo;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getNameAr() {
		return nameAr;
	}


	public void setNameAr(String nameAr) {
		this.nameAr = nameAr;
	}


	public String getClient() {
		return client;
	}


	public void setClient(String client) {
		this.client = client;
	}


	public String getOrderNo() {
		return orderNo;
	}


	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	
	

}
