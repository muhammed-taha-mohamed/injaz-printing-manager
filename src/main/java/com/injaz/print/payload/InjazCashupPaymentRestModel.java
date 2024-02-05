package com.injaz.print.payload;

import java.io.Serializable;
import java.math.BigDecimal;

public class InjazCashupPaymentRestModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigDecimal paymentAmt;

	private String name;

	private String nameAr;

	
	public InjazCashupPaymentRestModel() {
		// TODO Auto-generated constructor stub
	}


	public BigDecimal getPaymentAmt() {
		return paymentAmt;
	}


	public void setPaymentAmt(BigDecimal paymentAmt) {
		this.paymentAmt = paymentAmt;
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
	
	
	

}
