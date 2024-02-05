package com.injaz.print.util;

import java.util.List;

public interface InjazAppConstants {
    String DEFAULT_PAGE_NUMBER = "0";
    String DEFAULT_PAGE_SIZE = "30";
    
    public static final String  SORT_ORDER_ASC = "ASC";
    public static final String  SORT_ORDER_DESC = "DESC";  
  
    
   int MAX_PAGE_SIZE = 50;
   public static final String  SYSTEM_CLIENT_ID = "00000000-0000-0000-0000-000000000000";
   public static final String  LANG_ENGLISH = "EN";
   public static final String  LANG_ARABIC = "AR";
   
   public static final String[] SYSTEM_CLIENT_SCREEN_LIST  = {"Client Setup","Screen Setup"};
   
   public static final String  YES_VALUE = "Y";
   public static final String  NO_VALUE = "N";
   
   public static final String  DATE_FORMAT_UI = "yyyy-MM-dd";
   public static final String  DATE_FORMAT_DB = "yyyy-MM-dd HH:mm:ss";
   public static final String  DATE_FORMAT_RECEIPT_PRINT = "dd-M-yyyy hh:mm:ss a";
   public static final String  DATE_FORMAT_RECEIPT_QR = "yyyy-MM-dd'T'HH:mm:ss'Z'";
   
   public static final String  PARAMETER_TYPE_INVOICE_STATUS = "INVOICE_STATUS";
   public static final String  PARAMETER_TYPE_INVOICE_STATUS_DRAFT = "DR";
   public static final String  PARAMETER_TYPE_INVOICE_STATUS_PROCESS = "PRC";
   public static final String  PARAMETER_TYPE_INVOICE_STATUS_COMPLETE = "CO";
   public static final String  PARAMETER_TYPE_INVOICE_STATUS_REACTIVATE = "RE";
   public static final String  PARAMETER_TYPE_INVOICE_STATUS_PAID = "P";
   public static final String  TABLE_NAME_INJAZ_PUR_INVOICE = "INJAZ_PUR_INVOICE";
   public static final String  TABLE_NAME_INJAZ_MOVEMENT = "INJAZ_MOVEMENT";
   public static final String  TABLE_NAME_INJAZ_PRODUCTION = "INJAZ_PRODUCTION";
   public static final String  TABLE_NAME_INJAZ_SALE_ORDER = "INJAZ_SALE_ORDER";
   public static final String  TABLE_NAME_INJAZ_PAYMENT = "INJAZ_PAYMENT";
   public static final String  TABLE_NAME_INJAZ_INVENTORY_CNT = "INJAZ_INVENTORY_CNT";
   public static final String  TABLE_NAME_INJAZ_ONLINE_ORDER = "INJAZ_ONLINE_ORDER";
   public static final String  TABLE_NAME_INJAZ_STOCK_MAINT = "INJAZ_STOCK_MAINT";
   public static final String  ACTION_BUTTON_COMPLETE = "CO";
   public static final String  ACTION_BUTTON_REACTIVATE = "RE";
   
   public static final String  PARAMETER_TYPE_PAYMENT_STATUS_PAID = "P";
   public static final String  PARAMETER_TYPE_CASHUP_STATUS_OPEN = "O";
   public static final String  PARAMETER_TYPE_CASHUP_STATUS_CLOSE = "C";
   
   public static final String  PARAMETER_TYPE_FLR_TABLE_STATUS_AVAILABLE = "A";
   public static final String  PARAMETER_TYPE_FLR_TABLE_STATUS_BOOKED= "B";
   
   public static final String  COST_TYPE_FIXED = "F";
   public static final String  COST_TYPE_AVERAGE = "A";
   
   
   public static final String  PARAMETER_TYPE_ORDER_STATUS = "ORDER_STATUS";
   public static final String  PARAMETER_TYPE_ORDER_STATUS_DRAFT = "DR";
   public static final String  PARAMETER_TYPE_ORDER_STATUS_KITCHEN = "K";
   public static final String  PARAMETER_TYPE_ORDER_STATUS_PAID = "P";
   public static final String  PARAMETER_TYPE_ORDER_STATUS_REFUND_INPROCES = "RI";
   public static final String  PARAMETER_TYPE_ORDER_STATUS_REFUNDED = "R";
   
   public static final String  TRANSACTION_TYPE_PAY = "P";
   public static final String  TRANSACTION_TYPE_REFUND = "R";
   
   
   public static final String  REFUND_STATUS_ACTIVE = "A";
   public static final String  REFUND_STATUS_COMPLETE = "CO";
   
   
   public static final String  DEFAULT_UOM_NAME = "UNIT";
   
   public static final String  PARAMETER_TYPE_ORDER_TYPE_DINE_IN = "DI";
   public static final String  PARAMETER_TYPE_ORDER_TYPE_TAKE_AWAY = "TA";
   public static final String  PARAMETER_TYPE_ORDER_TYPE_HOME_DELIVERY = "HD";
   public static final String  PARAMETER_TYPE_ORDER_TYPE_ONLINE = "OL";
   public static final String  DEFAULT_APP_USER = "InjazPos";
   
   public static final String  DEFAULT_APP_CLIENT = "SystemClient";
   
   public static final String  PARAMETER_TYPE_ONLINE_ORDER_STATUS = "ONLINE_ORDER_STATUS";
   public static final String  PARAMETER_TYPE_ONLINE_ORDER_STATUS_RECIEVED = "D";
   public static final String  PARAMETER_TYPE_ONLINE_ORDER_STATUS_ACCEPTED = "A";
   public static final String  PARAMETER_TYPE_ONLINE_ORDER_STATUS_REJECTED = "R";
   
   public static boolean isEmptyString(String str) {
	    if (str == null || str.trim().equals("")) {
	      return true;
	    } else {
	      return false;
	    }

	  }
 
   public static boolean isEmptyList(List list) {
	    if (list == null || list.size()==0 ) {
	      return true;
	    } else {
	      return false;
	    }

	  }
}
