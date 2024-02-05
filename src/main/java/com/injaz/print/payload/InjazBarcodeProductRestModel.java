package com.injaz.print.payload;

import java.io.Serializable;
import java.math.BigDecimal;

public class InjazBarcodeProductRestModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String nameAr;
	private BigDecimal qty;
	
	
	public InjazBarcodeProductRestModel() {
		// TODO Auto-generated constructor stub
	}
	
	
	public InjazBarcodeProductRestModel(String name, String nameAr, BigDecimal qty) {
		super();
		this.name = name;
		this.nameAr = nameAr;
		this.qty = qty;
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


	public BigDecimal getQty() {
		return qty;
	}


	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}
	
	
	
	
	
}
