package com.injaz.print.payload;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class InjazCashupCloseRestModel implements Serializable{
	
  
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String branch;
	
	private String cashupDate;
	
	private String closeDate;
	
	private String header;
	
	private String footer;
	
	private String printer;
	
	private BigDecimal total;
	
	private List<InjazCashupPaymentRestModel> payments;
	
	
	public InjazCashupCloseRestModel() {
		// TODO Auto-generated constructor stub
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getCashupDate() {
		return cashupDate;
	}

	public void setCashupDate(String cashupDate) {
		this.cashupDate = cashupDate;
	}

	public String getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(String closeDate) {
		this.closeDate = closeDate;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

	public String getPrinter() {
		return printer;
	}

	public void setPrinter(String printer) {
		this.printer = printer;
	}

	public List<InjazCashupPaymentRestModel> getPayments() {
		return payments;
	}

	public void setPayments(List<InjazCashupPaymentRestModel> payments) {
		this.payments = payments;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	
	

}
