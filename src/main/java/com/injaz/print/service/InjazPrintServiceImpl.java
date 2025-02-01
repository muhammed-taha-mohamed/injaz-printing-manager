package com.injaz.print.service;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.injaz.print.base.api.EscPos;
import com.injaz.print.base.api.EscPosConst;
import com.injaz.print.base.api.Style;
import com.injaz.print.base.api.image.Bitonal;
import com.injaz.print.base.api.image.BitonalThreshold;
import com.injaz.print.base.api.image.EscPosImage;
import com.injaz.print.base.api.image.GraphicsImageWrapper;
import com.injaz.print.base.api.output.PrinterOutputStream;
import com.injaz.print.payload.*;
import com.injaz.print.util.InjazAppConstants;
import com.injaz.print.util.InjazUtility;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.print.PrintService;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;
import java.util.*;

@Service
public class InjazPrintServiceImpl implements InjazPrintService  {

	@Autowired
	private Environment env;
	
	@Autowired
	private InjazReportService reportService;
	
	@Override
	public void printKitchenReceipt(InjazPrintOrderRestModel order) throws Exception {


        PrintService printService = PrinterOutputStream.getPrintServiceByName(order.getPrinter());
	     EscPos escpos;
	     escpos = new EscPos(new PrinterOutputStream(printService));
         Bitonal algorithm = new BitonalThreshold(127); 
         
         GraphicsImageWrapper imageWrapper = new GraphicsImageWrapper();

         escpos.feed(2); 
         
         imageWrapper.setJustification(EscPosConst.Justification.Right);
         escpos.write(imageWrapper,new EscPosImage(getKitchenHeaderTextImage("*" + env.getProperty("KITCHEN_RECEIPT_TITLE") + "*"), algorithm));
         
         imageWrapper.setJustification(EscPosConst.Justification.Center);
         escpos.write(imageWrapper,new EscPosImage(getKitchenHeaderTextImage(env.getProperty("DATE_TIME_LABEL")+" "+InjazUtility.getInstance().convertInstantIntoString(Instant.now(),InjazAppConstants.DATE_FORMAT_RECEIPT_PRINT)), algorithm));

         imageWrapper.setJustification(EscPosConst.Justification.Right);
         escpos.write(imageWrapper,new EscPosImage(getKitchenHeaderTextImage(env.getProperty("ORDER_NO_LABEL")+" "+order.getOrderNo()), algorithm));
        
         escpos.write(imageWrapper,new EscPosImage(getKitchenHeaderTextImage(env.getProperty("TOKEN_NO_LABEL")+" "+order.getTokenNo()+" "), algorithm));


        if (order.isCancel()) {
            escpos.write(imageWrapper, new EscPosImage(getKitchenHeaderTextImage(env.getProperty("CANCEL_ITEMS_LABEL")), algorithm));
        }

        if(order.getTableName()!=null && !order.getTableName().equals("false")){
            imageWrapper.setJustification(EscPosConst.Justification.Center);
            escpos.write(imageWrapper, new EscPosImage(getKitchenHeaderTextImage(env.getProperty("TABLE_LABEL") + " : " + order.getTableName()), algorithm));

            imageWrapper.setJustification(EscPosConst.Justification.Center);
            escpos.write(imageWrapper, new EscPosImage(getKitchenHeaderTextImage(env.getProperty("FLOOR_LABEL") + " : " + order.getFloorName()), algorithm));

            imageWrapper.setJustification(EscPosConst.Justification.Center);
            escpos.write(imageWrapper, new EscPosImage(getKitchenHeaderTextImage(env.getProperty("TABLE_LABEL") + " : " + order.getTableName()), algorithm));

            imageWrapper.setJustification(EscPosConst.Justification.Center);
            escpos.write(imageWrapper, new EscPosImage(getKitchenHeaderTextImage(env.getProperty("FLOOR_LABEL") + " : " + order.getFloorName()), algorithm));
        }


        escpos.feed(1);

        if (order.getLines() != null) {
            escpos.writeLF(getHeaderStyle(),env.getProperty("DETAILS_LABEL"));
        	 imageWrapper.setJustification(EscPosConst.Justification.Left_Default);
             escpos.write(imageWrapper,new EscPosImage(getKitchenHeaderTextImage(env.getProperty("NOTES_LABEL")+"   "+env.getProperty("QTY_LABEL")+"   "+env.getProperty("PRODUCT_LABEL")), algorithm));
             escpos.writeLF(getHeaderStyle(),env.getProperty("SEPARATOR"));
        	   int ixd=1;
        	 for(InjazPrintOrderLineRestModel l :order.getLines()) {
        		 
        		 escpos.writeLF(getLeftLineStyle(),ixd+"."+l.getName());
        		 escpos.writeLF(getHeaderStyleWithBold(),String.valueOf(l.getQty().doubleValue()));
        		 imageWrapper.setJustification(EscPosConst.Justification.Left_Default);
                 escpos.write(imageWrapper,new EscPosImage(getKitchenHeaderTextImage(l.getNameAr()), algorithm));
                 if(!InjazAppConstants.isEmptyString(l.getNotes()) && !l.getNotes().equals("null")) {
                 escpos.write(imageWrapper,new EscPosImage(getKitchenHeaderTextImage(l.getNotes()), algorithm));
                 }
                
                 
                 if(l.getItems()!=null && !l.getItems().isEmpty()) {
                	 int ixd2=1;
                	 for(InjazPrintOrderItemLineRestModel i :l.getItems()) {
                	 
                		/* escpos.writeLF(getHeaderStyle()," * "+i.getName());
                		 escpos.writeLF(getHeaderStyle(),String.valueOf(i.getQty().doubleValue()));*/
                		 imageWrapper.setJustification(EscPosConst.Justification.Left_Default);
                         escpos.write(imageWrapper,new EscPosImage(getHeaderWithoutBoldTextImageItem(ixd+"."+ixd2+"."+i.getName()+"  "+i.getNameAr()), algorithm));
                         escpos.writeLF(getHeaderStyleWithBold(),String.valueOf(i.getQty().doubleValue()));
                         if(!InjazAppConstants.isEmptyString(i.getNotes()) && !i.getNotes().equals("null")) {
                         escpos.write(imageWrapper,new EscPosImage(getHeaderWithoutBoldTextImage(i.getNotes()), algorithm));
                         }
                         ixd2++; 
                	 }
                	 
                 }
                 
                 escpos.writeLF(getHeaderStyle(),env.getProperty("SEPARATOR"));
                 ixd++;
        	 }
         }
         
        /* imageWrapper.setJustification(EscPosConst.Justification.Center);
         escpos.write(imageWrapper,new EscPosImage(getHeaderTextImage(order.getFooter()), algorithm));
         */
         escpos.feed(5);
         escpos.cut(EscPos.CutMode.FULL);
         
         
         
         escpos.close();
         
	     
	     
	}
	

	@Override
	public void printOrderReceipt(InjazPrintOrderRestModel order) throws Exception {


		 PrintService printService = PrinterOutputStream.getPrintServiceByName(order.getPrinter());
	     EscPos escpos;
	     escpos = new EscPos(new PrinterOutputStream(printService));
         Bitonal algorithm = new BitonalThreshold(127); 
         
         
         // add logo
         BufferedImage  logoBufferedImage = ImageIO.read(new FileInputStream(env.getProperty("LOGO_PATH")));
         EscPosImage escposlogo = new EscPosImage(logoBufferedImage, algorithm); 
         GraphicsImageWrapper imageWrapper = new GraphicsImageWrapper();
         imageWrapper.setJustification(EscPosConst.Justification.Center);
         escpos.write(imageWrapper,escposlogo);
         
         //title
         imageWrapper.setJustification(EscPosConst.Justification.Right);
         escpos.write(imageWrapper,new EscPosImage(getHeaderWithoutBoldTextImage(env.getProperty("ORDER_RECEIPT_TITLE")), algorithm)); 
         
         //order number
         imageWrapper.setJustification(EscPosConst.Justification.Right);
         escpos.write(imageWrapper,new EscPosImage(getHeaderWithoutBoldTextImage(env.getProperty("ORDER_NO_LABEL")+" "+order.getOrderNo()), algorithm)); 
        
         //header
         if(!InjazAppConstants.isEmptyString(order.getHeader()) && !order.getHeader().equals("null")) {
             imageWrapper.setJustification(EscPosConst.Justification.Center);
             escpos.write(imageWrapper,new EscPosImage(getHeaderWithoutBoldTextImage2(order.getHeader()), algorithm));
             }
         
         //footer
         imageWrapper.setJustification(EscPosConst.Justification.Center);
         escpos.write(imageWrapper,new EscPosImage(getHeaderWithoutBoldTextImage2(order.getFooter()), algorithm));
         
         //tax number
         if(!InjazAppConstants.isEmptyString(order.getTaxNo()) && !order.getTaxNo().equals("null")) {
         imageWrapper.setJustification(EscPosConst.Justification.Center);
         escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(env.getProperty("TAX_NO_LABEL")+" : "+order.getTaxNo()), algorithm));
         }
         
         //date
         imageWrapper.setJustification(EscPosConst.Justification.Center);
         escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(env.getProperty("DATE_TIME_LABEL")+" "+InjazUtility.getInstance().convertInstantIntoString(Instant.now(),InjazAppConstants.DATE_FORMAT_RECEIPT_PRINT)), algorithm));
      
         //number and type 
         imageWrapper.setJustification(EscPosConst.Justification.Center);
         escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(env.getProperty("TOKEN_NO_LABEL")+""+order.getTokenNo()+"  --  "+env.getProperty("ORDER_TYPE_LABEL")+" "+order.getOrderType()), algorithm)); 
         
         //customer
         imageWrapper.setJustification(EscPosConst.Justification.Center);
         escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(env.getProperty("CUSTOMER_LABEL")+order.getCustomer()+""), algorithm)); 
         
         //DELIVERY_PERSON
         if(!InjazAppConstants.isEmptyString(order.getDeliveryPerson()) && order.getDeliveryPerson().equals("null")) {
             imageWrapper.setJustification(EscPosConst.Justification.Center);
        	 escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(env.getProperty("DELIVERY_PERSON_LABEL")+" "+order.getDeliveryPerson()), algorithm));
         }

         if(order.getCustomerTaxNo() != null) {
        	imageWrapper.setJustification(EscPosConst.Justification.Center);
        	 escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(env.getProperty("CUSTOMER_TAX_NO_LABEL")+order.getCustomerTaxNo()), algorithm));	
         }
         
         if(!InjazAppConstants.isEmptyString(order.getCouponNo())) {
        	 imageWrapper.setJustification(EscPosConst.Justification.Center);
             escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(env.getProperty("COUPON_NO_LABEL")+order.getCouponNo()), algorithm));
         }
         
         if(order.getFloorName()!=null &&  !order.getFloorName().equalsIgnoreCase("false")) {
        	 imageWrapper.setJustification(EscPosConst.Justification.Center);
        	 escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(env.getProperty("TABLE_LABEL")+" : "+order.getTableName()+"  --  "+env.getProperty("FLOOR_LABEL")+" : "+order.getFloorName()), algorithm));

         }
         
       
         int ixd=1;
         if(order.getLines()!=null) {
        	 escpos.writeLF(getHeaderStyle(),env.getProperty("DETAILS_LABEL"));
        	 imageWrapper.setJustification(EscPosConst.Justification.Center);
             escpos.write(imageWrapper,new EscPosImage(getFooterWithBoldTextImage(env.getProperty("TOTAL_AMT_LABEL")+"   "+env.getProperty("QTY_LABEL")+"   "+env.getProperty("TOTAL_WITHOUT_TAX_LABEL")+"            "+env.getProperty("PRODUCT_LABEL")), algorithm));	  	 
        	 escpos.writeLF(getHeaderStyle(),env.getProperty("SEPARATOR"));
        	 for(InjazPrintOrderLineRestModel l :order.getLines()) {
        		 
        		 escpos.writeLF(getLeftLineStyle(),ixd+"."+l.getName());
        		 escpos.writeLF(getRightLineWithoutBoldStyle(),String.valueOf(l.getUnitPrice().doubleValue())+"  x  "+String.valueOf(l.getQty().doubleValue()+"  =  "+String.valueOf(l.getUnitPrice().multiply(l.getQty()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())));
        		 imageWrapper.setJustification(EscPosConst.Justification.Left_Default);
                 escpos.write(imageWrapper,new EscPosImage(getHeaderWithoutBoldTextImage(l.getNameAr()), algorithm));
                 if(l.getItems()!=null && !l.getItems().isEmpty()) {
                	 int ixd2=1;
                	 for(InjazPrintOrderItemLineRestModel i :l.getItems()) {
                	 
                		/* escpos.writeLF(getHeaderStyle()," * "+i.getName());
                		 escpos.writeLF(getHeaderStyle(),String.valueOf(i.getQty().doubleValue()));*/
                		 imageWrapper.setJustification(EscPosConst.Justification.Right);
                         escpos.write(imageWrapper,new EscPosImage(getHeaderWithoutBoldTextImageItem(ixd+"."+ixd2+"."+i.getName()+"  "+i.getNameAr()), algorithm));
                         escpos.writeLF(getRightLineWithoutBoldStyle(),String.valueOf(i.getUnitPrice().doubleValue()) +"  x  "+String.valueOf(i.getQty().doubleValue()+"  =  "+String.valueOf(i.getUnitPrice().multiply(l.getQty()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())));
                         ixd2++; 
                	 }
                	 
                 }
                 
                 escpos.writeLF(getHeaderStyle(),env.getProperty("SEPARATOR"));
                 ixd++;
        	 }
         }


        // add tobacco fee fields
        if(order.getTobaccoFeeBeforeTax()!=null){
            imageWrapper.setJustification(EscPosConst.Justification.Center);
            escpos.write(imageWrapper,new EscPosImage(getFooterWithBoldTextImage(
                    env.getProperty("TOBACCO_FEE")+
                    " : "+ order.getTobaccoFeeBeforeTax() ), algorithm));

        }


        imageWrapper.setJustification(EscPosConst.Justification.Center);
        BigDecimal totalWithoutTax = order.getTotalWithoutTax().setScale(2, RoundingMode.HALF_UP);
        escpos.write(imageWrapper, new EscPosImage(
                getFooterWithBoldTextImage(env.getProperty("TOTAL_WITHOUT_TAX_LABEL") + " : " + totalWithoutTax),
                algorithm
        ));

        if(order.getTobaccoFeeBeforeTax()!=null) {
            imageWrapper.setJustification(EscPosConst.Justification.Center);
            BigDecimal totalInvoiceWithoutTax = order.getTotalWithoutTax().add(order.getTobaccoFeeBeforeTax())
                    .setScale(2, RoundingMode.HALF_UP);

            escpos.write(imageWrapper, new EscPosImage(getFooterWithBoldTextImage(env.getProperty("INVOICE_TOTAL_WITHOUT_TAX_LABEL")+ " : " + totalInvoiceWithoutTax), algorithm));
        }



        imageWrapper.setJustification(EscPosConst.Justification.Center);

        BigDecimal taxAmount = order.getTax().setScale(2, RoundingMode.HALF_UP);
        escpos.write(imageWrapper, new EscPosImage(
                getFooterWithBoldTextImage(env.getProperty("TAX_AMT_LABEL") + " : " + taxAmount),
                algorithm
        ));

        imageWrapper.setJustification(EscPosConst.Justification.Center);

        BigDecimal actualAmt = order.getActualAmt().setScale(2, RoundingMode.HALF_UP);
        escpos.write(imageWrapper, new EscPosImage(
                getFooterWithBoldTextImage(env.getProperty("TOTAL_AMT_LABEL") + " : " + actualAmt),
                algorithm
        ));



        if(!InjazAppConstants.isEmptyString(order.getCouponNo())) {
            imageWrapper.setJustification(EscPosConst.Justification.Center);
            escpos.write(imageWrapper, new EscPosImage(getFooterWithoutBoldTextImage(env.getProperty("DISCOUNT_AMT_LABEL") + " : " + order.getDiscountAmt()), algorithm));

            imageWrapper.setJustification(EscPosConst.Justification.Center);
            escpos.write(imageWrapper, new EscPosImage(getFooterWithoutBoldTextImage(env.getProperty("TOTAL_AFTER_DISCOUNT") + " : " + order.getTotal()), algorithm));
        }
         


      String name = new String(order.getHeader().getBytes("UTF-8"), "ISO-8859-1");
      String str = name;
      StringBuilder sb = new StringBuilder();
      char ch[] = str.toCharArray();
      for(int i = 0; i < ch.length; i++) {
         String hexString = Integer.toHexString(ch[i]);
         sb.append(hexString);
      }
      String hexa_name = sb.toString();
      String hexa_name_TLV;
      if(name.length()<= 15){
      hexa_name_TLV = "010"+Integer.toHexString(name.length())+hexa_name;
      }
      else{
      hexa_name_TLV = "01"+Integer.toHexString(name.length())+hexa_name;
      }
      
 
      String taxno = new String(order.getTaxNo().getBytes("UTF-8"), "ISO-8859-1");
      String str2 = taxno;
      StringBuilder sb2 = new StringBuilder();
      char ch2[] = str2.toCharArray();
      for(int i2 = 0; i2 < ch2.length; i2++) {
         String hexString2 = Integer.toHexString(ch2[i2]);
         sb2.append(hexString2);
      }
      String hexa_taxid = sb2.toString();
      String hexa_taxid_TLV;
      if(taxno.length() <= 15){
        hexa_taxid_TLV = "020"+Integer.toHexString(taxno.length())+hexa_taxid;
      }
      else{
        hexa_taxid_TLV = "02"+Integer.toHexString(taxno.length())+hexa_taxid;
      }
     
     /* SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
      Date currentDate = new Date();
      String date1 = sdf.format(currentDate);
      */
      String date1 = InjazUtility.getInstance().convertInstantIntoString(Instant.now(),InjazAppConstants.DATE_FORMAT_RECEIPT_QR);
      String date = new String(date1.toString().getBytes("UTF-8"), "ISO-8859-1");
      String str3 =date;
      StringBuilder sb3 = new StringBuilder();
      char ch3[] = str3.toCharArray();
      for(int i3 = 0; i3 < ch3.length; i3++) {
         String hexString3 = Integer.toHexString(ch3[i3]);
         sb3.append(hexString3);
      }
      String hexa_date = sb3.toString();
      String hexa_date_TLV ;
      if(date.length() <=15){
        hexa_date_TLV= "030"+Integer.toHexString(date.length())+hexa_date;
      }
      else{
        hexa_date_TLV= "03"+Integer.toHexString(date.length())+hexa_date;
      } 
      

      double totalDuple= order.getActualAmt().doubleValue();
      String total = Double.toString(totalDuple);
      String str4 = total;
      StringBuilder sb4 = new StringBuilder();
      char ch4[] = str4.toCharArray();
      for(int i4 = 0; i4 < ch4.length; i4++) {
         String hexString4 = Integer.toHexString(ch4[i4]);
         sb4.append(hexString4); 
      }
      String hexa_total = sb4.toString();
      String hexa_total_TLV;
      if(total.length()<= 15){
        hexa_total_TLV = "040"+Integer.toHexString(total.length())+hexa_total;
      }
      else{
        hexa_total_TLV = "04"+Integer.toHexString(total.length())+hexa_total;
      }

      
      double taxDuple=order.getTax().doubleValue();
      String tax = Double.toString(taxDuple);
      String str5 = tax;
      StringBuilder sb5 = new StringBuilder();
      char ch5[] = str5.toCharArray();
      for(int i5 = 0; i5 < ch5.length; i5++) {
         String hexString5 = Integer.toHexString(ch5[i5]);
         sb5.append(hexString5);
      }
      String hexa_tax = sb5.toString();
      String hexa_tax_TLV;
      if(tax.length() <= 15){
        hexa_tax_TLV = "050"+Integer.toHexString(tax.length())+hexa_tax;
      }
      else{
        hexa_tax_TLV = "05"+Integer.toHexString(tax.length())+hexa_tax;
      }
            
        
        String qrdata_as_hexa =hexa_name_TLV+hexa_taxid_TLV+hexa_date_TLV+hexa_total_TLV+hexa_tax_TLV;
        String qrdata_as_Base64=Base64.getEncoder().encodeToString(new BigInteger(qrdata_as_hexa,16).toByteArray());
         

         QRCodeWriter qrCodeWriter = new QRCodeWriter();
         BitMatrix bitMatrix = qrCodeWriter.encode(qrdata_as_Base64, BarcodeFormat.QR_CODE, 700, 350);
         imageWrapper.setJustification(EscPosConst.Justification.Center);
         escpos.write(imageWrapper,new EscPosImage(MatrixToImageWriter.toBufferedImage(bitMatrix), algorithm));
         
        
         escpos.feed(4);
         escpos.cut(EscPos.CutMode.FULL);
         
         
         
         escpos.close();
	}
	
	@Override
	public void printRefundReceipt(InjazPrintOrderRestModel order) throws Exception {
		 PrintService printService = PrinterOutputStream.getPrintServiceByName(order.getPrinter());
	     EscPos escpos;
	     escpos = new EscPos(new PrinterOutputStream(printService));
         Bitonal algorithm = new BitonalThreshold(127); 
         
         
         // add logo
         BufferedImage  logoBufferedImage = ImageIO.read(new FileInputStream(env.getProperty("LOGO_PATH")));
         EscPosImage escposlogo = new EscPosImage(logoBufferedImage, algorithm); 
         GraphicsImageWrapper imageWrapper = new GraphicsImageWrapper();
         imageWrapper.setJustification(EscPosConst.Justification.Center);
         escpos.write(imageWrapper,escposlogo);
         
         
         
         if(!InjazAppConstants.isEmptyString(order.getHeader()) && !order.getHeader().equals("null")) {
             imageWrapper.setJustification(EscPosConst.Justification.Center);
             escpos.write(imageWrapper,new EscPosImage(getHeaderTextImage(order.getHeader()), algorithm));
             }
         
         /*escpos.writeLF(getHeaderStyleWithBold(),env.getProperty("REFUND_RECEIPT_TITLE"));
         escpos.feed(2); 
         
         escpos.writeLF(getHeaderStyle(),env.getProperty("DATE_TIME_LABEL")+" "+InjazUtility.getInstance().convertInstantIntoString(Instant.now(),InjazAppConstants.DATE_FORMAT_RECEIPT_PRINT));
         escpos.feed(2);  
         
         
         escpos.writeLF(getHeaderStyleWithBold(),env.getProperty("ORDER_NO_LABEL")+" "+order.getOrderNo());
         escpos.feed(1); 
         escpos.writeLF(getHeaderStyleWithBold(),env.getProperty("TOKEN_NO_LABEL")+" "+order.getTokenNo());
        
         escpos.feed(1); */
         
         
         imageWrapper.setJustification(EscPosConst.Justification.Right);
         escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(env.getProperty("REFUND_RECEIPT_TITLE")), algorithm)); 
         imageWrapper.setJustification(EscPosConst.Justification.Center);
         escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(env.getProperty("DATE_TIME_LABEL")+" "+InjazUtility.getInstance().convertInstantIntoString(Instant.now(),InjazAppConstants.DATE_FORMAT_RECEIPT_PRINT)), algorithm));
         //escpos.writeLF(getHeaderStyle(),);
         imageWrapper.setJustification(EscPosConst.Justification.Right);
         escpos.write(imageWrapper,new EscPosImage(getHeaderWithoutBoldTextImage(env.getProperty("ORDER_NO_LABEL")+" "+order.getOrderNo()), algorithm)); 
        
         //imageWrapper.setJustification(EscPosConst.Justification.Left_Default);
         escpos.write(imageWrapper,new EscPosImage(getHeaderWithoutBoldTextImage(env.getProperty("TOKEN_NO_LABEL")+""+order.getTokenNo()+""), algorithm)); 
        
         
         if(order.getFloorName()!=null &&  !order.getFloorName().equalsIgnoreCase("false")) {
        	   imageWrapper.setJustification(EscPosConst.Justification.Left_Default);
               escpos.write(imageWrapper,new EscPosImage(getHeaderWithoutBoldTextImage(" Floor : "+order.getFloorName()), algorithm));	 
        	 
         }
         
         if(order.getTableName()!=null &&  !order.getTableName().equalsIgnoreCase("false")) {
      	   imageWrapper.setJustification(EscPosConst.Justification.Left_Default);
             escpos.write(imageWrapper,new EscPosImage(getHeaderWithoutBoldTextImage(" Table : "+order.getTableName()), algorithm));	 
      	 
       }
         escpos.feed(1);
         int ixd=1;
         if(order.getLines()!=null) {
        	 escpos.writeLF(getHeaderStyle(),env.getProperty("DETAILS_LABEL"));
        	 imageWrapper.setJustification(EscPosConst.Justification.Left_Default);
             escpos.write(imageWrapper,new EscPosImage(getHeaderWithoutBoldTextImage(env.getProperty("TOTAL_AMT_LABEL")+"   "+env.getProperty("QTY_LABEL")+"   "+env.getProperty("TOTAL_WITHOUT_TAX_LABEL")+"    "+env.getProperty("PRODUCT_LABEL")), algorithm));	  	 
        	 escpos.writeLF(getHeaderStyle(),env.getProperty("SEPARATOR"));

        	 for(InjazPrintOrderLineRestModel l :order.getLines()) {
        		 
        		 escpos.writeLF(getLeftLineWithoutBoldStyle(),ixd+"."+l.getName());
        		 escpos.writeLF(getRightLineWithoutBoldStyle(),String.valueOf(l.getPriceWithoutTax().doubleValue())+"  x  "+String.valueOf(l.getQty().doubleValue()+"  =  "+String.valueOf(l.getPriceWithoutTax().multiply(l.getQty()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())));
        		 imageWrapper.setJustification(EscPosConst.Justification.Left_Default);
                 escpos.write(imageWrapper,new EscPosImage(getHeaderWithoutBoldTextImage(l.getNameAr()), algorithm));
                 
                 if(l.getItems()!=null && l.getItems().size() >0) {
                	 int ixd2=1;
                	 for(InjazPrintOrderItemLineRestModel i :l.getItems()) {
                	 
                		/* escpos.writeLF(getHeaderStyle()," * "+i.getName());
                		 escpos.writeLF(getHeaderStyle(),String.valueOf(i.getQty().doubleValue()));*/
                		 imageWrapper.setJustification(EscPosConst.Justification.Left_Default);
                         escpos.write(imageWrapper,new EscPosImage(getHeaderWithoutBoldTextImageItem(ixd+"."+ixd2+"."+i.getName()+"  "+i.getNameAr()), algorithm));
                         escpos.writeLF(getRightLineWithoutBoldStyle(),String.valueOf(i.getPriceWithoutTax().doubleValue()) +"  x  "+String.valueOf(i.getQty().doubleValue()+"  =  "+String.valueOf(i.getPriceWithoutTax().multiply(l.getQty()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())));
                         ixd2++; 
                	 }
                	 
                 }
                 escpos.writeLF(getHeaderStyle(),env.getProperty("SEPARATOR"));
                 ixd++;
        	 }
         }
         escpos.writeLF(getHeaderStyle(),env.getProperty("SEPARATOR"));
         
         imageWrapper.setJustification(EscPosConst.Justification.Right);
         escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(order.getRefund() + " : "+env.getProperty("REFUND_AMT_LABEL") ), algorithm));
         
         /*imageWrapper.setJustification(EscPosConst.Justification.Right);
         escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(order.getTax() + " : "+env.getProperty("TAX_AMT_LABEL") ), algorithm));
         
         imageWrapper.setJustification(EscPosConst.Justification.Right);
         escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(order.getTotalWithoutTax()+ " : "+env.getProperty("TOTAL_WITHOUT_TAX_LABEL") ), algorithm));
         
         imageWrapper.setJustification(EscPosConst.Justification.Right);
         escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(order.getCustomerPayAmount()+ " : "+env.getProperty("CUSTOMER_PAY_AMT_LABEL") ), algorithm));
         
         imageWrapper.setJustification(EscPosConst.Justification.Right);
         escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(order.getRemainingAmount()+ " : "+env.getProperty("REMAINING_AMT_LABEL") ), algorithm));
         
         QRCodeWriter qrCodeWriter = new QRCodeWriter();
         BitMatrix bitMatrix = qrCodeWriter.encode("http://online.foodlocation.co/receipt?orderId="+order.getOrderId()+"", BarcodeFormat.QR_CODE, 400, 200);
         
         imageWrapper.setJustification(EscPosConst.Justification.Center);
         escpos.write(imageWrapper,new EscPosImage(MatrixToImageWriter.toBufferedImage(bitMatrix), algorithm));
         */
         /*QR DATA M Taha
          * ******************************************************************************************/
        

      String name = new String(order.getHeader().getBytes("UTF-8"), "ISO-8859-1");
      String str = name;
      StringBuilder sb = new StringBuilder();
      char ch[] = str.toCharArray();
      for(int i = 0; i < ch.length; i++) {
         String hexString = Integer.toHexString(ch[i]);
         sb.append(hexString);
      }
      String hexa_name = sb.toString();
      String hexa_name_TLV;
      if(name.length()<= 15){
      hexa_name_TLV = "010"+Integer.toHexString(name.length())+hexa_name;
      }
      else{
      hexa_name_TLV = "01"+Integer.toHexString(name.length())+hexa_name;
      }
      
 
      String taxno = new String(order.getTaxNo().getBytes("UTF-8"), "ISO-8859-1");
      String str2 = taxno;
      StringBuilder sb2 = new StringBuilder();
      char ch2[] = str2.toCharArray();
      for(int i2 = 0; i2 < ch2.length; i2++) {
         String hexString2 = Integer.toHexString(ch2[i2]);
         sb2.append(hexString2);
      }
      String hexa_taxid = sb2.toString();
      String hexa_taxid_TLV;
      if(taxno.length() <= 15){
        hexa_taxid_TLV = "020"+Integer.toHexString(taxno.length())+hexa_taxid;
      }
      else{
        hexa_taxid_TLV = "02"+Integer.toHexString(taxno.length())+hexa_taxid;
      }
     
     /* SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
      Date currentDate = new Date();
      String date1 = sdf.format(currentDate);
      */
      String date1 = InjazUtility.getInstance().convertInstantIntoString(Instant.now(),InjazAppConstants.DATE_FORMAT_RECEIPT_QR);
      String date = new String(date1.toString().getBytes("UTF-8"), "ISO-8859-1");
      String str3 =date;
      StringBuilder sb3 = new StringBuilder();
      char ch3[] = str3.toCharArray();
      for(int i3 = 0; i3 < ch3.length; i3++) {
         String hexString3 = Integer.toHexString(ch3[i3]);
         sb3.append(hexString3);
      }
      String hexa_date = sb3.toString();
      String hexa_date_TLV ;
      if(date.length() <=15){
        hexa_date_TLV= "030"+Integer.toHexString(date.length())+hexa_date;
      }
      else{
        hexa_date_TLV= "03"+Integer.toHexString(date.length())+hexa_date;
      } 
      

      double totalDuple= order.getTotal().doubleValue();
      String total = Double.toString(totalDuple);
      String str4 = total;
      StringBuilder sb4 = new StringBuilder();
      char ch4[] = str4.toCharArray();
      for(int i4 = 0; i4 < ch4.length; i4++) {
         String hexString4 = Integer.toHexString(ch4[i4]);
         sb4.append(hexString4); 
      }
      String hexa_total = sb4.toString();
      String hexa_total_TLV;
      if(total.length()<= 15){
        hexa_total_TLV = "040"+Integer.toHexString(total.length())+hexa_total;
      }
      else{
        hexa_total_TLV = "04"+Integer.toHexString(total.length())+hexa_total;
      }

      
      double taxDuple=order.getTax().doubleValue();
      String tax = Double.toString(taxDuple);
      String str5 = tax;
      StringBuilder sb5 = new StringBuilder();
      char ch5[] = str5.toCharArray();
      for(int i5 = 0; i5 < ch5.length; i5++) {
         String hexString5 = Integer.toHexString(ch5[i5]);
         sb5.append(hexString5);
      }
      String hexa_tax = sb5.toString();
      String hexa_tax_TLV;
      if(tax.length() <= 15){
        hexa_tax_TLV = "050"+Integer.toHexString(tax.length())+hexa_tax;
      }
      else{
        hexa_tax_TLV = "05"+Integer.toHexString(tax.length())+hexa_tax;
      }
            
        
        String qrdata_as_hexa =hexa_name_TLV+hexa_taxid_TLV+hexa_date_TLV+hexa_total_TLV+hexa_tax_TLV;
        String qrdata_as_Base64=Base64.getEncoder().encodeToString(new BigInteger(qrdata_as_hexa,16).toByteArray());
         

         QRCodeWriter qrCodeWriter = new QRCodeWriter();
         BitMatrix bitMatrix = qrCodeWriter.encode(qrdata_as_Base64, BarcodeFormat.QR_CODE, 700, 350);
         imageWrapper.setJustification(EscPosConst.Justification.Center);
         escpos.write(imageWrapper,new EscPosImage(MatrixToImageWriter.toBufferedImage(bitMatrix), algorithm));
         
        
         escpos.feed(4);
         escpos.cut(EscPos.CutMode.FULL);
         
         
         
         escpos.close();
	}
	
	
	@Override
	public void printCheckPrint(InjazPrintOrderRestModel order) throws Exception {
		 PrintService printService = PrinterOutputStream.getPrintServiceByName(order.getPrinter());
	     EscPos escpos;
	     escpos = new EscPos(new PrinterOutputStream(printService));
         Bitonal algorithm = new BitonalThreshold(127); 
         
         
         // add logo
         BufferedImage  logoBufferedImage = ImageIO.read(new FileInputStream(env.getProperty("LOGO_PATH")));
         EscPosImage escposlogo = new EscPosImage(logoBufferedImage, algorithm); 
         GraphicsImageWrapper imageWrapper = new GraphicsImageWrapper();
         imageWrapper.setJustification(EscPosConst.Justification.Center);
         escpos.write(imageWrapper,escposlogo);
         
         //title
         imageWrapper.setJustification(EscPosConst.Justification.Right);
         escpos.write(imageWrapper,new EscPosImage(getFooterWithBoldTextImage(env.getProperty("CHECK_PRINT_TITLE ")), algorithm)); 
         
         //order number
         imageWrapper.setJustification(EscPosConst.Justification.Right);
         escpos.write(imageWrapper,new EscPosImage(getHeaderWithoutBoldTextImage(env.getProperty("ORDER_NO_LABEL")+" "+order.getOrderNo()), algorithm)); 
        
         //header
         if(!InjazAppConstants.isEmptyString(order.getHeader()) && !order.getHeader().equals("null")) {
             imageWrapper.setJustification(EscPosConst.Justification.Center);
             escpos.write(imageWrapper,new EscPosImage(getHeaderWithoutBoldTextImage2(order.getHeader()), algorithm));
             }
         
         //footer
         imageWrapper.setJustification(EscPosConst.Justification.Center);
         escpos.write(imageWrapper,new EscPosImage(getHeaderWithoutBoldTextImage2(order.getFooter()), algorithm));
         
         //tax number
         if(!InjazAppConstants.isEmptyString(order.getTaxNo()) && !order.getTaxNo().equals("null")) {
         imageWrapper.setJustification(EscPosConst.Justification.Center);
         escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(env.getProperty("TAX_NO_LABEL")+" : "+order.getTaxNo()), algorithm));
         }
         
         //date
         imageWrapper.setJustification(EscPosConst.Justification.Center);
         escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(env.getProperty("DATE_TIME_LABEL")+" "+InjazUtility.getInstance().convertInstantIntoString(Instant.now(),InjazAppConstants.DATE_FORMAT_RECEIPT_PRINT)), algorithm));
      
         //number and type 
         imageWrapper.setJustification(EscPosConst.Justification.Center);
         escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(env.getProperty("TOKEN_NO_LABEL")+""+order.getTokenNo()+"  --  "+env.getProperty("ORDER_TYPE_LABEL")+" "+order.getOrderType()), algorithm)); 
         
         //customer
         imageWrapper.setJustification(EscPosConst.Justification.Center);
         escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(env.getProperty("CUSTOMER_LABEL")+order.getCustomer()+""), algorithm)); 
         

         if(order.getCustomerTaxNo() != null) {
        	imageWrapper.setJustification(EscPosConst.Justification.Center);
        	 escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(env.getProperty("CUSTOMER_TAX_NO_LABEL")+order.getCustomerTaxNo()), algorithm));	
         }
         
         if(!InjazAppConstants.isEmptyString(order.getCouponNo())) {
        	 imageWrapper.setJustification(EscPosConst.Justification.Center);
             escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(env.getProperty("COUPON_NO_LABEL")+order.getCouponNo()), algorithm));
         }
         
         if(order.getFloorName()!=null &&  !order.getFloorName().equalsIgnoreCase("false")) {
        	 imageWrapper.setJustification(EscPosConst.Justification.Center); 
        	 escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(env.getProperty("TABLE_LABEL")+" : "+order.getTableName()+"  --  "+env.getProperty("FLOOR_LABEL")+" : "+order.getFloorName()), algorithm));
        	 
         }
         
         
       
         int ixd=1;
         if(order.getLines()!=null) {
        	 escpos.writeLF(getHeaderStyle(),env.getProperty("DETAILS_LABEL"));
        	 imageWrapper.setJustification(EscPosConst.Justification.Center);
             escpos.write(imageWrapper,new EscPosImage(getFooterWithBoldTextImage(env.getProperty("TOTAL_AMT_LABEL")+"   "+env.getProperty("QTY_LABEL")+"   "+env.getProperty("TOTAL_WITHOUT_TAX_LABEL")+"           "+env.getProperty("PRODUCT_LABEL")), algorithm));	  	 
        	 escpos.writeLF(getHeaderStyle(),env.getProperty("SEPARATOR"));
        	 for(InjazPrintOrderLineRestModel l :order.getLines()) {
        		 
        		 escpos.writeLF(getLeftLineStyle(),ixd+"."+l.getName());
        		 escpos.writeLF(getRightLineWithoutBoldStyle(),String.valueOf(l.getUnitPrice().doubleValue())+"  x  "+String.valueOf(l.getQty().doubleValue()+"  =  "+String.valueOf(l.getUnitPrice().multiply(l.getQty()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())));
        		 imageWrapper.setJustification(EscPosConst.Justification.Left_Default);
                 escpos.write(imageWrapper,new EscPosImage(getHeaderWithoutBoldTextImage(l.getNameAr()), algorithm));
                 if(l.getItems()!=null && l.getItems().size() >0) {
                	 int ixd2=1;
                	 for(InjazPrintOrderItemLineRestModel i :l.getItems()) {
                	 
                		/* escpos.writeLF(getHeaderStyle()," * "+i.getName());
                		 escpos.writeLF(getHeaderStyle(),String.valueOf(i.getQty().doubleValue()));*/
                		 imageWrapper.setJustification(EscPosConst.Justification.Right);
                         escpos.write(imageWrapper,new EscPosImage(getHeaderWithoutBoldTextImageItem(ixd+"."+ixd2+"."+i.getName()+"  "+i.getNameAr()), algorithm));
                         escpos.writeLF(getRightLineWithoutBoldStyle(),String.valueOf(i.getUnitPrice().doubleValue()) +"  x  "+String.valueOf(i.getQty().doubleValue()+"  =  "+String.valueOf(i.getUnitPrice().multiply(l.getQty()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())));
                         ixd2++; 
                	 }
                	 
                 }
                 
                 
                 
                 escpos.writeLF(getHeaderStyle(),env.getProperty("SEPARATOR"));
                 ixd++;
        	 }
         }
        /* 
         if(order.getDiscountAmt()!=null && order.getDiscountAmt().compareTo(BigDecimal.ZERO) == 1 ) {
        	 imageWrapper.setJustification(EscPosConst.Justification.Right);
        	 escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(order.getActualAmt() + " : "+env.getProperty("ACTUAL_AMT_LABEL") ), algorithm));

        	 imageWrapper.setJustification(EscPosConst.Justification.Right);
        	 escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(order.getDiscountAmt() + " : "+env.getProperty("DISCOUNT_AMT_LABEL") ), algorithm));
         }
*/

         imageWrapper.setJustification(EscPosConst.Justification.Right);
         escpos.write(imageWrapper,new EscPosImage(getFooterWithBoldTextImage(order.getActualAmt() + " : "+env.getProperty("TOTAL_AMT_LABEL") ), algorithm));

     /*    imageWrapper.setJustification(EscPosConst.Justification.Right);
         escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(order.getTotalWithoutTax()+ " : "+env.getProperty("TOTAL_WITHOUT_TAX_LABEL") ), algorithm));
       */  
         
         imageWrapper.setJustification(EscPosConst.Justification.Right);
         escpos.write(imageWrapper,new EscPosImage(getFooterWithBoldTextImage(order.getTax() + " : "+env.getProperty("TAX_AMT_LABEL") ), algorithm));
     /*    
         imageWrapper.setJustification(EscPosConst.Justification.Right);
         escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(order.getExtraCharge()+ " : "+env.getProperty("EXTRA_CHARGE_LABEL") ), algorithm));
         
         imageWrapper.setJustification(EscPosConst.Justification.Right);
         escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(order.getPaidAmt() + " : "+env.getProperty("TOTAL_PAID_AMT_LABEL") ), algorithm));
         
         imageWrapper.setJustification(EscPosConst.Justification.Right);
         escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(order.getCustomerPayAmount()+ " : "+env.getProperty("CUSTOMER_PAY_AMT_LABEL") ), algorithm));
         
         imageWrapper.setJustification(EscPosConst.Justification.Right);
         escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(order.getRemainingAmount()+ " : "+env.getProperty("REMAINING_AMT_LABEL") ), algorithm));
      */   
         
        /* QRCodeWriter qrCodeWriter = new QRCodeWriter();
         // BitMatrix bitMatrix = qrCodeWriter.encode(order.getOrderId(), BarcodeFormat.QR_CODE, 400, 200);
         BitMatrix bitMatrix = qrCodeWriter.encode(domain+""+id+"", BarcodeFormat.QR_CODE, 400, 200);
         imageWrapper.setJustification(EscPosConst.Justification.Center);
         escpos.write(imageWrapper,new EscPosImage(MatrixToImageWriter.toBufferedImage(bitMatrix), algorithm));
         */
        
         escpos.feed(4);
         escpos.cut(EscPos.CutMode.FULL);
         
         
         
         escpos.close();
	}
	
	@Override
	public void printDuplicateOrderReceipt(InjazPrintOrderRestModel order) throws Exception {
		PrintService printService = PrinterOutputStream.getPrintServiceByName(order.getPrinter());
	     EscPos escpos;
	     escpos = new EscPos(new PrinterOutputStream(printService));
        Bitonal algorithm = new BitonalThreshold(127); 
        
        
        // add logo
        BufferedImage  logoBufferedImage = ImageIO.read(new FileInputStream(env.getProperty("LOGO_PATH")));
        EscPosImage escposlogo = new EscPosImage(logoBufferedImage, algorithm); 
        GraphicsImageWrapper imageWrapper = new GraphicsImageWrapper();
        imageWrapper.setJustification(EscPosConst.Justification.Center);
        escpos.write(imageWrapper,escposlogo);
        
        //title
        imageWrapper.setJustification(EscPosConst.Justification.Right);
        escpos.write(imageWrapper,new EscPosImage(getFooterWithBoldTextImage(env.getProperty("DUPLICATE_ORDER_RECEIPT_TITLE")), algorithm)); 
        

        //order number
        imageWrapper.setJustification(EscPosConst.Justification.Right);
        escpos.write(imageWrapper,new EscPosImage(getHeaderWithoutBoldTextImage(env.getProperty("ORDER_NO_LABEL")+" "+order.getOrderNo()), algorithm)); 
       
        //header
        if(!InjazAppConstants.isEmptyString(order.getHeader()) && !order.getHeader().equals("null")) {
            imageWrapper.setJustification(EscPosConst.Justification.Center);
            escpos.write(imageWrapper,new EscPosImage(getHeaderWithoutBoldTextImage2(order.getHeader()), algorithm));
            }
        
        //footer
        imageWrapper.setJustification(EscPosConst.Justification.Center);
        escpos.write(imageWrapper,new EscPosImage(getHeaderWithoutBoldTextImage2(order.getFooter()), algorithm));
        
        //tax number
        if(!InjazAppConstants.isEmptyString(order.getTaxNo()) && !order.getTaxNo().equals("null")) {
        imageWrapper.setJustification(EscPosConst.Justification.Center);
        escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(env.getProperty("TAX_NO_LABEL")+" : "+order.getTaxNo()), algorithm));
        }
        
        //date
        imageWrapper.setJustification(EscPosConst.Justification.Center);
        escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(env.getProperty("DATE_TIME_LABEL")+" "+order.getOrderDate()), algorithm));
     
        //number and type 
        imageWrapper.setJustification(EscPosConst.Justification.Center);
        escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(env.getProperty("TOKEN_NO_LABEL")+""+order.getTokenNo()+"  --  "+env.getProperty("ORDER_TYPE_LABEL")+" "+order.getOrderType()), algorithm)); 
        
        //customer
        imageWrapper.setJustification(EscPosConst.Justification.Center);
        escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(env.getProperty("CUSTOMER_LABEL")+order.getCustomer()+""), algorithm)); 
        
        //DELIVERY_PERSON
        if(!InjazAppConstants.isEmptyString(order.getDeliveryPerson()) && order.getDeliveryPerson().equals("null")) {
            imageWrapper.setJustification(EscPosConst.Justification.Center);
       	 escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(env.getProperty("DELIVERY_PERSON_LABEL")+""+order.getDeliveryPerson()), algorithm)); 
        }

        if(order.getCustomerTaxNo() != null) {
       	imageWrapper.setJustification(EscPosConst.Justification.Center);
       	 escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(env.getProperty("CUSTOMER_TAX_NO_LABEL")+order.getCustomerTaxNo()), algorithm));	
        }
        
        if(!InjazAppConstants.isEmptyString(order.getCouponNo())) {
       	 imageWrapper.setJustification(EscPosConst.Justification.Center);
            escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(env.getProperty("COUPON_NO_LABEL")+order.getCouponNo()), algorithm));
        }
        
        if(order.getFloorName()!=null &&  !order.getFloorName().equalsIgnoreCase("false")) {
       	 imageWrapper.setJustification(EscPosConst.Justification.Center); 
       	 escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(env.getProperty("TABLE_LABEL")+" : "+order.getTableName()+"  --  "+env.getProperty("FLOOR_LABEL")+" : "+order.getFloorName()), algorithm));
       	 
        }
        
      
        int ixd=1;
        if(order.getLines()!=null) {
       	 escpos.writeLF(getHeaderStyle(),env.getProperty("DETAILS_LABEL"));
       	 imageWrapper.setJustification(EscPosConst.Justification.Center);
            escpos.write(imageWrapper,new EscPosImage(getFooterWithBoldTextImage(env.getProperty("TOTAL_AMT_LABEL")+"   "+env.getProperty("QTY_LABEL")+"   "+env.getProperty("TOTAL_WITHOUT_TAX_LABEL")+"            "+env.getProperty("PRODUCT_LABEL")), algorithm));	  	 
       	 escpos.writeLF(getHeaderStyle(),env.getProperty("SEPARATOR"));
       	 for(InjazPrintOrderLineRestModel l :order.getLines()) {
       		 
       		 escpos.writeLF(getLeftLineStyle(),ixd+"."+l.getName());
       		 escpos.writeLF(getRightLineWithoutBoldStyle(),String.valueOf(l.getUnitPrice().doubleValue())+"  x  "+String.valueOf(l.getQty().doubleValue()+"  =  "+String.valueOf(l.getUnitPrice().multiply(l.getQty()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())));
       		 imageWrapper.setJustification(EscPosConst.Justification.Left_Default);
                escpos.write(imageWrapper,new EscPosImage(getHeaderWithoutBoldTextImage(l.getNameAr()), algorithm));
                if(l.getItems()!=null && l.getItems().size() >0) {
               	 int ixd2=1;
               	 for(InjazPrintOrderItemLineRestModel i :l.getItems()) {
               	 
               		/* escpos.writeLF(getHeaderStyle()," * "+i.getName());
               		 escpos.writeLF(getHeaderStyle(),String.valueOf(i.getQty().doubleValue()));*/
               		 imageWrapper.setJustification(EscPosConst.Justification.Right);
                        escpos.write(imageWrapper,new EscPosImage(getHeaderWithoutBoldTextImageItem(ixd+"."+ixd2+"."+i.getName()+"  "+i.getNameAr()), algorithm));
                        escpos.writeLF(getRightLineWithoutBoldStyle(),String.valueOf(i.getUnitPrice().doubleValue()) +"  x  "+String.valueOf(i.getQty().doubleValue()+"  =  "+String.valueOf(i.getUnitPrice().multiply(l.getQty()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())));
                        ixd2++; 
               	 }
               	 
                }
                
                escpos.writeLF(getHeaderStyle(),env.getProperty("SEPARATOR"));
                ixd++;
       	 }
        }
       /* 
        if(order.getDiscountAmt()!=null && order.getDiscountAmt().compareTo(BigDecimal.ZERO) == 1 ) {
       	 imageWrapper.setJustification(EscPosConst.Justification.Right);
       	 escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(order.getActualAmt() + " : "+env.getProperty("ACTUAL_AMT_LABEL") ), algorithm));

       	 imageWrapper.setJustification(EscPosConst.Justification.Right);
       	 escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(order.getDiscountAmt() + " : "+env.getProperty("DISCOUNT_AMT_LABEL") ), algorithm));
        }
*/

        imageWrapper.setJustification(EscPosConst.Justification.Center);
        escpos.write(imageWrapper,new EscPosImage(getFooterWithBoldTextImage(env.getProperty("TOTAL_AMT_LABEL")+ " : "+ order.getTotal() ), algorithm));

    /*    imageWrapper.setJustification(EscPosConst.Justification.Right);
        escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(order.getTotalWithoutTax()+ " : "+env.getProperty("TOTAL_WITHOUT_TAX_LABEL") ), algorithm));
      */  
        
        imageWrapper.setJustification(EscPosConst.Justification.Center);
        escpos.write(imageWrapper,new EscPosImage(getFooterWithBoldTextImage(env.getProperty("TAX_AMT_LABEL")+ " : "+ order.getTax() ), algorithm));
    /*    
        imageWrapper.setJustification(EscPosConst.Justification.Right);
        escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(order.getExtraCharge()+ " : "+env.getProperty("EXTRA_CHARGE_LABEL") ), algorithm));
        
        imageWrapper.setJustification(EscPosConst.Justification.Right);
        escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(order.getPaidAmt() + " : "+env.getProperty("TOTAL_PAID_AMT_LABEL") ), algorithm));
        
        imageWrapper.setJustification(EscPosConst.Justification.Right);
        escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(order.getCustomerPayAmount()+ " : "+env.getProperty("CUSTOMER_PAY_AMT_LABEL") ), algorithm));
        
        imageWrapper.setJustification(EscPosConst.Justification.Right);
        escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(order.getRemainingAmount()+ " : "+env.getProperty("REMAINING_AMT_LABEL") ), algorithm));
     */   
        
        /*QR DATA M Taha
         * ******************************************************************************************/
        
        String name = new String(order.getHeader().getBytes("UTF-8"), "ISO-8859-1");
        String str = name;
        StringBuilder sb = new StringBuilder();
        char ch[] = str.toCharArray();
        for(int i = 0; i < ch.length; i++) {
           String hexString = Integer.toHexString(ch[i]);
           sb.append(hexString);
        }
        String hexa_name = sb.toString();
        String hexa_name_TLV;
        if(name.length()<= 15){
        hexa_name_TLV = "010"+Integer.toHexString(name.length())+hexa_name;
        }
        else{
        hexa_name_TLV = "01"+Integer.toHexString(name.length())+hexa_name;
        }
        
   
        String taxno = new String(order.getTaxNo().getBytes("UTF-8"), "ISO-8859-1");
        String str2 = taxno;
        StringBuilder sb2 = new StringBuilder();
        char ch2[] = str2.toCharArray();
        for(int i2 = 0; i2 < ch2.length; i2++) {
           String hexString2 = Integer.toHexString(ch2[i2]);
           sb2.append(hexString2);
        }
        String hexa_taxid = sb2.toString();
        String hexa_taxid_TLV;
        if(taxno.length() <= 15){
          hexa_taxid_TLV = "020"+Integer.toHexString(taxno.length())+hexa_taxid;
        }
        else{
          hexa_taxid_TLV = "02"+Integer.toHexString(taxno.length())+hexa_taxid;
        }
       
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String date1 = sdf.format(order.getOrderDate());
        String date = new String(date1.toString().getBytes("UTF-8"), "ISO-8859-1");
        String str3 =date;
        StringBuilder sb3 = new StringBuilder();
        char ch3[] = str3.toCharArray();
        for(int i3 = 0; i3 < ch3.length; i3++) {
           String hexString3 = Integer.toHexString(ch3[i3]);
           sb3.append(hexString3);
        }
        String hexa_date = sb3.toString();
        String hexa_date_TLV ;
        if(date.length() <=15){
          hexa_date_TLV= "030"+Integer.toHexString(date.length())+hexa_date;
        }
        else{
          hexa_date_TLV= "03"+Integer.toHexString(date.length())+hexa_date;
        } 
        

        double totalDuple= order.getTotal().doubleValue();
        String total = Double.toString(totalDuple);
        String str4 = total;
        StringBuilder sb4 = new StringBuilder();
        char ch4[] = str4.toCharArray();
        for(int i4 = 0; i4 < ch4.length; i4++) {
           String hexString4 = Integer.toHexString(ch4[i4]);
           sb4.append(hexString4); 
        }
        String hexa_total = sb4.toString();
        String hexa_total_TLV;
        if(total.length()<= 15){
          hexa_total_TLV = "040"+Integer.toHexString(total.length())+hexa_total;
        }
        else{
          hexa_total_TLV = "04"+Integer.toHexString(total.length())+hexa_total;
        }

        
        double taxDuple=order.getTax().doubleValue();
        String tax = Double.toString(taxDuple);
        String str5 = tax;
        StringBuilder sb5 = new StringBuilder();
        char ch5[] = str5.toCharArray();
        for(int i5 = 0; i5 < ch5.length; i5++) {
           String hexString5 = Integer.toHexString(ch5[i5]);
           sb5.append(hexString5);
        }
        String hexa_tax = sb5.toString();
        String hexa_tax_TLV;
        if(tax.length() <= 15){
          hexa_tax_TLV = "050"+Integer.toHexString(tax.length())+hexa_tax;
        }
        else{
          hexa_tax_TLV = "05"+Integer.toHexString(tax.length())+hexa_tax;
        }
              
        
       
       String qrdata_as_hexa =hexa_name_TLV+hexa_taxid_TLV+hexa_date_TLV+hexa_total_TLV+hexa_tax_TLV;
       String qrdata_as_Base64=Base64.getEncoder().encodeToString(new BigInteger(qrdata_as_hexa,16).toByteArray());
        

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrdata_as_Base64, BarcodeFormat.QR_CODE, 700, 350);
        imageWrapper.setJustification(EscPosConst.Justification.Center);
        escpos.write(imageWrapper,new EscPosImage(MatrixToImageWriter.toBufferedImage(bitMatrix), algorithm));
        
       
        escpos.feed(4);
        escpos.cut(EscPos.CutMode.FULL);
        
        
        
        escpos.close();
	}
	
	
	@Override
	public void printCashUpCloseReceipt(InjazCashupCloseRestModel cashup) throws Exception {
		 PrintService printService = PrinterOutputStream.getPrintServiceByName(cashup.getPrinter());
	     EscPos escpos;
	     escpos = new EscPos(new PrinterOutputStream(printService));
         Bitonal algorithm = new BitonalThreshold(127); 
         
         
         // add logo
         BufferedImage  logoBufferedImage = ImageIO.read(new FileInputStream(env.getProperty("LOGO_PATH")));
         EscPosImage escposlogo = new EscPosImage(logoBufferedImage, algorithm); 
         GraphicsImageWrapper imageWrapper = new GraphicsImageWrapper();
         imageWrapper.setJustification(EscPosConst.Justification.Center);
         escpos.write(imageWrapper,escposlogo);
         
         
         
         if(!InjazAppConstants.isEmptyString(cashup.getHeader()) && !cashup.getHeader().equals("null")) {
             imageWrapper.setJustification(EscPosConst.Justification.Center);
             escpos.write(imageWrapper,new EscPosImage(getHeaderTextImage(cashup.getHeader()), algorithm));
             }
         
         /*escpos.writeLF(getHeaderStyleWithBold(),env.getProperty("CASHUP_CLOSE_RECEIPT_TITLE"));
         escpos.feed(2); */
         
          
         imageWrapper.setJustification(EscPosConst.Justification.Right);
         escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(env.getProperty("CASHUP_CLOSE_RECEIPT_TITLE")), algorithm)); 
         
         //escpos.writeLF(getHeaderStyle(),);
         imageWrapper.setJustification(EscPosConst.Justification.Right);
         escpos.write(imageWrapper,new EscPosImage(getHeaderWithoutBoldTextImage(env.getProperty("OPEN_TIME_LABEL")+" "+cashup.getCashupDate()), algorithm)); 
        
         //imageWrapper.setJustification(EscPosConst.Justification.Left_Default);
         escpos.write(imageWrapper,new EscPosImage(getHeaderWithoutBoldTextImage(env.getProperty("CLOSE_TIME_LABEL")+""+cashup.getCloseDate()+""), algorithm));

         
       
         
        /* escpos.writeLF(getHeaderStyleWithBold(),env.getProperty("OPEN_TIME_LABEL")+" "+cashup.getCashupDate());
         escpos.feed(1); 
         escpos.writeLF(getHeaderStyleWithBold(),env.getProperty("CLOSE_TIME_LABEL")+" "+cashup.getCloseDate());
        
         escpos.feed(1); */
         
         imageWrapper.setJustification(EscPosConst.Justification.Left_Default);
         escpos.write(imageWrapper,new EscPosImage(getHeaderWithoutBoldTextImage(env.getProperty("BRANCH_LABEL")+cashup.getBranch()), algorithm));	
         

         escpos.feed(1);
         int ixd=1;
         if(cashup.getPayments()!=null) {
        	 escpos.writeLF(getHeaderStyle(),env.getProperty("DETAILS_LABEL"));
        	 
        	 escpos.writeLF(getLeftLineStyle(),"Payment Method                    "+"Total Amount  ");
        	 escpos.writeLF(getHeaderStyle(),env.getProperty("SEPARATOR"));
        	 for(InjazCashupPaymentRestModel p :cashup.getPayments()) {
        		 
        		 escpos.writeLF(getLeftLineStyle(),ixd+"."+p.getName());
        		 escpos.writeLF(getRightLineWithoutBoldStyle(),String.valueOf(p.getPaymentAmt().doubleValue()) );
        		 imageWrapper.setJustification(EscPosConst.Justification.Left_Default);
                 escpos.write(imageWrapper,new EscPosImage(getHeaderWithoutBoldTextImage(p.getNameAr()), algorithm));
                 escpos.writeLF(getHeaderStyle(),env.getProperty("SEPARATOR"));
                 ixd++;
        	 }
         }
         
         /*imageWrapper.setJustification(EscPosConst.Justification.Left_Default);
         escpos.write(imageWrapper,new EscPosImage(getHeaderWithoutBoldTextImageForTotal(env.getProperty("TOTAL_LABEL")+"                                                           "+cashup.getTotal()), algorithm));	
         escpos.writeLF(getHeaderStyle(),env.getProperty("SEPARATOR"));*/
      
    	 
    	/* escpos.writeLF(getLeftLineStyle(),env.getProperty("TOTAL_LABEL")+"                                "+cashup.getTotal());*/
         /*escpos.writeLF(getLeftLineStyle(),env.getProperty("TOTAL_LABEL"));
		 escpos.writeLF(getRightLineWithoutBoldStyle(),String.valueOf(cashup.getTotal().doubleValue()) );*/
		 
		 imageWrapper.setJustification(EscPosConst.Justification.Right);
	        escpos.write(imageWrapper,new EscPosImage(getFooterWithoutBoldTextImage(cashup.getTotal().doubleValue() + " : "+env.getProperty("TOTAL_LABEL") ), algorithm));
	        
       
		 escpos.writeLF(getHeaderStyle(),env.getProperty("SEPARATOR"));
         
         imageWrapper.setJustification(EscPosConst.Justification.Center);
         escpos.write(imageWrapper,new EscPosImage(getHeaderTextImage(cashup.getFooter()), algorithm));
         escpos.feed(5);
         escpos.cut(EscPos.CutMode.FULL);
         
         
         
         escpos.close();
	
	}
	
	
	
	public Style getHeaderStyle() {
		return  new Style()
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setJustification(EscPosConst.Justification.Center);
	}
	
	public Style getHeaderStyleWithBold() {
		return  new Style().setBold(true)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setJustification(EscPosConst.Justification.Center);
	}
	
	public Style getLeftLineStyle() {
		return   new Style().setFontSize(Style.FontSize._1, Style.FontSize._1).setBold(true)
                .setJustification(EscPosConst.Justification.Left_Default);
	}
	
	public Style getLeftLineWithoutBoldStyle() {
		return   new Style().setFontSize(Style.FontSize._1, Style.FontSize._1).setBold(true)
                .setJustification(EscPosConst.Justification.Left_Default);
	}
	public Style getRightLineWithoutBoldStyle() {
		return   new Style().setFontSize(Style.FontSize._1, Style.FontSize._1).setBold(true)
                .setJustification(EscPosConst.Justification.Right);
	}
	
	public BufferedImage getHeaderTextImage(String text) {
		
		 BufferedImage img = new BufferedImage(400, 200,
                 BufferedImage.TYPE_INT_RGB);
         Graphics2D g = img.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 400, 200);
        Font f = new Font("TimesRoman",Font.BOLD,35);
        JLabel label = new JLabel(text);
        label.setFont(f);
        label.setBounds(1,1,400,200);
       
         label.paint(g);
         g.dispose();
         return img;
		
	}
	
	public BufferedImage getHeaderWithoutBoldTextImage(String text) {
		
		 BufferedImage img = new BufferedImage(400, 60,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
       g.setColor(Color.WHITE);
       g.fillRect(0, 0, 400, 60);
       Font f = new Font("TimesRoman",Font.BOLD,32);
       JLabel label = new JLabel(text);
       label.setFont(f);
       label.setBounds(200,1,400,60);
      
        label.paint(g);
        g.dispose();
        return img;
		
	}
	public BufferedImage getHeaderWithoutBoldTextImage2(String text) {
		
		 BufferedImage img = new BufferedImage(400, 60,
               BufferedImage.TYPE_INT_RGB);
       Graphics2D g = img.createGraphics();
      g.setColor(Color.WHITE);
      g.fillRect(0, 0, 400, 60);
      Font f = new Font("TimesRoman",Font.PLAIN,30);
      JLabel label = new JLabel(text);
      label.setFont(f);
      label.setBounds(200,1,400,60);
     
       label.paint(g);
       g.dispose();
       return img;
		
	}
	
	public BufferedImage getHeaderWithoutBoldNotesTextImage(String text) {
		
		 BufferedImage img = new BufferedImage(400,60,
               BufferedImage.TYPE_INT_RGB);
       Graphics2D g = img.createGraphics();
      g.setColor(Color.WHITE);
      g.fillRect(0, 0, 400,60);
      Font f = new Font("TimesRoman",Font.BOLD,30);
      JLabel label = new JLabel("<html>"+text+"</html>");
      label.setFont(f);
      label.setBounds(0,0,400,60);
     
       label.paint(g);
       g.dispose();
       return img;
		
	}
	
	public BufferedImage getHeaderWithoutBoldTextImageForTotal(String text) {
		
		 BufferedImage img = new BufferedImage(800, 60,
               BufferedImage.TYPE_INT_RGB);
       Graphics2D g = img.createGraphics();
      g.setColor(Color.WHITE);
      g.fillRect(0, 0, 800, 60);
      Font f = new Font("TimesRoman",Font.BOLD,30);
      JLabel label = new JLabel(text);
      label.setFont(f);
      label.setBounds(200,1,800,60);
     
       label.paint(g);
       g.dispose();
       return img;
		
	}
	
	public BufferedImage getFooterWithoutBoldTextImage(String text) {
		
		 BufferedImage img = new BufferedImage(400, 60,
               BufferedImage.TYPE_INT_RGB);
       Graphics2D g = img.createGraphics();
      g.setColor(Color.WHITE);
      g.fillRect(0, 0, 400, 60);
      Font f = new Font("TimesRoman",Font.BOLD,30);
      JLabel label = new JLabel(text);
      label.setFont(f);
      label.setBounds(1,1,1600,60);
     
       label.paint(g);
       g.dispose();
       return img;
		
	}
	public BufferedImage getFooterWithoutBoldTextImageNew(String text) {
		
		 BufferedImage img = new BufferedImage(500, 60,
              BufferedImage.TYPE_INT_RGB);
      Graphics2D g = img.createGraphics();
     g.setColor(Color.WHITE);
     g.fillRect(0, 0, 500, 60);
     Font f = new Font("TimesRoman",Font.PLAIN,25);
     JLabel label = new JLabel(text);
     label.setFont(f);
     label.setBounds(1,1,1600,60);
    
      label.paint(g);
      g.dispose();
      return img;
		
	}
	public BufferedImage getFooterWithBoldTextImage(String text) {
		
		 BufferedImage img = new BufferedImage(500, 60,
              BufferedImage.TYPE_INT_RGB);
      Graphics2D g = img.createGraphics();
     g.setColor(Color.WHITE);
     g.fillRect(0, 0, 500, 60);
     Font f = new Font("TimesRoman",Font.BOLD,28);
     JLabel label = new JLabel(text);
     label.setFont(f);
     label.setBounds(1,1,1600,60);
    
      label.paint(g);
      g.dispose();
      return img;
		
	}
	
	public BufferedImage getHeaderWithoutBoldTextImageItem(String text) {
		
		 BufferedImage img = new BufferedImage(500, 60,
               BufferedImage.TYPE_INT_RGB);
       Graphics2D g = img.createGraphics();
      g.setColor(Color.WHITE);
      g.fillRect(0, 0, 500, 60);
      Font f = new Font("TimesRoman",Font.BOLD,30);
      JLabel label = new JLabel(text);
      label.setFont(f);
      label.setBounds(200,1,500,60);
     
       label.paint(g);
       g.dispose();
       return img;
		
	}
	
	public BufferedImage getHeaderTextImageLine(String text) {
		
		 BufferedImage img = new BufferedImage(400, 200,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
       g.setColor(Color.WHITE);
       g.fillRect(0, 0, 400, 200);
       Font f = new Font("TimesRoman",Font.PLAIN,25);         
       JLabel label = new JLabel(text);
       label.setFont(f);
       label.setBounds(1,1,400,200);
      
        label.paint(g);
        g.dispose();
        return img;
		
	}
	
	@Override
	public void printBarcodeProducts(InjazPrintBarCodeProductRestModel model) throws Exception {
		List<InjazPrintBarCodeProduct> list = new ArrayList<InjazPrintBarCodeProduct>();
		/*list.add(new InjazPrintBarCodeProduct("burger", "burger", "KFC", "O1234"));
		list.add(new InjazPrintBarCodeProduct("burger1", "burger1", "KFC", "O12345"));
		list.add(new InjazPrintBarCodeProduct("burger2", "burger2", "KFC", "O12346"));*/
		String reportName="injaz_print_barcode_product.jrxml";
		
		
		List<InjazBarcodeProductRestModel> products = model.getProducts();
		
		for(InjazBarcodeProductRestModel p : products) {
			for(int i=0; i<p.getQty().intValue();i++) {
				list.add(new InjazPrintBarCodeProduct(p.getName(),p.getNameAr(), model.getClientName(),model.getOrderNo()));
			}
		}

		Map<String, Object> parameters = new HashMap<>();
		reportService.generateBeanReport(reportName, new JRBeanCollectionDataSource(list), parameters,model.getPrinter());
		
	}

    public BufferedImage getKitchenHeaderTextImage(String text) {

        BufferedImage img = new BufferedImage(400, 60,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 400, 60);
        Font f = new Font("TimesRoman",Font.BOLD,35);
        JLabel label = new JLabel(text);
        label.setFont(f);
        label.setBounds(200,1,400,60);

        label.paint(g);
        g.dispose();
        return img;

    }

}
