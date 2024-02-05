package com.injaz.print.payload;

import java.io.Serializable;
import java.util.List;

public class InjazPrintBarCodeProductRestModel  implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String orderNo;
	
	private String clientName;
	
	private String printer;
	
	private List<InjazBarcodeProductRestModel> products;
	
	public InjazPrintBarCodeProductRestModel() {
		// TODO Auto-generated constructor stub
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getPrinter() {
		return printer;
	}

	public void setPrinter(String printer) {
		this.printer = printer;
	}

	public List<InjazBarcodeProductRestModel> getProducts() {
		return products;
	}

	public void setProducts(List<InjazBarcodeProductRestModel> products) {
		this.products = products;
	}
	
	
	
	
}
