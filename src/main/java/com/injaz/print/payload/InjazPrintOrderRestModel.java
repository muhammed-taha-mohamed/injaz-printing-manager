package com.injaz.print.payload;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class InjazPrintOrderRestModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String header;
	private String footer;
	private String orderNo;
	private String floorName;
	private String tableName;
	private String printer;
	private BigDecimal totalWithoutTax;
	private BigDecimal tax;
	private BigDecimal total;
	private BigDecimal customerPayAmount;
	private BigDecimal remainingAmount;
	private BigDecimal refund;
	private String customer;
	private String cashier;
	private String tokenNo;
	private String orderType;
	private boolean cancel;
 private String couponNo;
 
	 
	 private BigDecimal discountAmt;
	 
	 private BigDecimal actualAmt;
	
	
	 private BigDecimal extraCharge;
	
	 private BigDecimal paidAmt;
	 
	 private String taxNo;
	 
	 private String deliveryPerson;
	 
	 private String customerTaxNo;
	 
	 private String orderDate;
	 
	 private String orderId;
	
	public InjazPrintOrderRestModel(String header, String footer, String orderNo, String floorName, String tableName,
			String printer, List<InjazPrintOrderLineRestModel> lines) {
		super();
		this.header = header;
		this.footer = footer;
		this.orderNo = orderNo;
		this.floorName = floorName;
		this.tableName = tableName;
		this.printer = printer;
		this.lines = lines;
	}

	private List<InjazPrintOrderLineRestModel> lines;
	
	public InjazPrintOrderRestModel() {
		// TODO Auto-generated constructor stub
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

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getFloorName() {
		return floorName;
	}

	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<InjazPrintOrderLineRestModel> getLines() {
		return lines;
	}

	public void setLines(List<InjazPrintOrderLineRestModel> lines) {
		this.lines = lines;
	}

	public String getPrinter() {
		return printer;
	}

	public void setPrinter(String printer) {
		this.printer = printer;
	}

	public BigDecimal getTotalWithoutTax() {
		return totalWithoutTax;
	}

	public void setTotalWithoutTax(BigDecimal totalWithoutTax) {
		this.totalWithoutTax = totalWithoutTax;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getCustomerPayAmount() {
		return customerPayAmount;
	}

	public void setCustomerPayAmount(BigDecimal customerPayAmount) {
		this.customerPayAmount = customerPayAmount;
	}

	public BigDecimal getRemainingAmount() {
		return remainingAmount;
	}

	public void setRemainingAmount(BigDecimal remainingAmount) {
		this.remainingAmount = remainingAmount;
	}

	public BigDecimal getRefund() {
		return refund;
	}

	public void setRefund(BigDecimal refund) {
		this.refund = refund;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getCashier() {
		return cashier;
	}

	public void setCashier(String cashier) {
		this.cashier = cashier;
	}

	public String getTokenNo() {
		return tokenNo;
	}

	public void setTokenNo(String tokenNo) {
		this.tokenNo = tokenNo;
	}

	public String getCouponNo() {
		return couponNo;
	}

	public void setCouponNo(String couponNo) {
		this.couponNo = couponNo;
	}

	public BigDecimal getDiscountAmt() {
		return discountAmt;
	}

	public void setDiscountAmt(BigDecimal discountAmt) {
		this.discountAmt = discountAmt;
	}

	public BigDecimal getActualAmt() {
		return actualAmt;
	}

	public void setActualAmt(BigDecimal actualAmt) {
		this.actualAmt = actualAmt;
	}

	public BigDecimal getExtraCharge() {
		return extraCharge;
	}

	public void setExtraCharge(BigDecimal extraCharge) {
		this.extraCharge = extraCharge;
	}

	public BigDecimal getPaidAmt() {
		return paidAmt;
	}

	public void setPaidAmt(BigDecimal paidAmt) {
		this.paidAmt = paidAmt;
	}

	public boolean isCancel() {
		return cancel;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}

	public String getTaxNo() {
		return taxNo;
	}

	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getDeliveryPerson() {
		return deliveryPerson;
	}

	public void setDeliveryPerson(String deliveryPerson) {
		this.deliveryPerson = deliveryPerson;
	}

	public String getCustomerTaxNo() {
		return customerTaxNo;
	}

	public void setCustomerTaxNo(String customerTaxNo) {
		this.customerTaxNo = customerTaxNo;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	
}
