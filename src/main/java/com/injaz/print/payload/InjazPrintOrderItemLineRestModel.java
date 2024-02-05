package com.injaz.print.payload;

import java.math.BigDecimal;

public class InjazPrintOrderItemLineRestModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String nameAr;
	private BigDecimal qty;
	private BigDecimal totalNetLine;
	private BigDecimal unitPrice;
	private BigDecimal priceWithoutTax;
	private String notes;
	
	public InjazPrintOrderItemLineRestModel() {
		// TODO Auto-generated constructor stub
	}
	
	

	public InjazPrintOrderItemLineRestModel(String name, String nameAr, BigDecimal qty) {
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



	public BigDecimal getTotalNetLine() {
		return totalNetLine;
	}



	public void setTotalNetLine(BigDecimal totalNetLine) {
		this.totalNetLine = totalNetLine;
	}



	public BigDecimal getUnitPrice() {
		return unitPrice;
	}



	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}



	public BigDecimal getPriceWithoutTax() {
		return priceWithoutTax;
	}



	public void setPriceWithoutTax(BigDecimal priceWithoutTax) {
		this.priceWithoutTax = priceWithoutTax;
	}



	public String getNotes() {
		return notes;
	}



	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	
	

}
