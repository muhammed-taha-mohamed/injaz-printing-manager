package com.injaz.print.service;

import java.io.UnsupportedEncodingException;

import com.injaz.print.payload.InjazCashupCloseRestModel;
import com.injaz.print.payload.InjazPrintBarCodeProductRestModel;
import com.injaz.print.payload.InjazPrintOrderRestModel;

public interface InjazPrintService {
	
	public void printKitchenReceipt(InjazPrintOrderRestModel order) throws Exception;
	public void printOrderReceipt(InjazPrintOrderRestModel order) throws Exception;
	public void printRefundReceipt(InjazPrintOrderRestModel order) throws Exception;
	public void printCheckPrint(InjazPrintOrderRestModel order) throws Exception;
	public void printDuplicateOrderReceipt(InjazPrintOrderRestModel order) throws Exception;
	public void printCashUpCloseReceipt(InjazCashupCloseRestModel cashup) throws Exception;
	public void printBarcodeProducts(InjazPrintBarCodeProductRestModel model) throws Exception;
	
}
