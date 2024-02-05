package com.injaz.print.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import javax.imageio.ImageIO;

import org.apache.tika.Tika;

public class InjazUtility {
	
	  private static InjazUtility instance = new InjazUtility();
	  private static Tika tika;

	  private static void init() {
	    tika = new Tika();
	  }

	
	  public static InjazUtility getInstance() {
	    if (tika == null) {
	      init();
	    }
	    return instance;
	  }

	  public static void setInstace(InjazUtility ins) {
		  InjazUtility.instance = ins;
	  }
	  public Long[] computeImageSize(byte[] bytea) throws IOException {
		    ByteArrayInputStream bis = new ByteArrayInputStream(bytea);
		    BufferedImage rImage = ImageIO.read(bis);
		    Long[] size = new Long[2];
		    size[0] = new Long(rImage.getWidth());
		    size[1] = new Long(rImage.getHeight());
		    return size;
	  }
	  
	  public String getMimeTypeName(byte[] data) {
		    return tika.detect(data);
		  }

	  public BigDecimal getPricewithTax(BigDecimal price,Double rate) throws Exception{
		 BigDecimal priceWithTax = BigDecimal.ZERO;
		 if(price!=null && rate != null) {
			 Double rateInPer = rate/100;
			 BigDecimal taxValue = price.multiply(new BigDecimal((rateInPer)));
			 priceWithTax = price.add(taxValue);
		 }
		 return priceWithTax;
	  }
	  public BigDecimal getPricewithOutTax(BigDecimal priceWithTax,Double rate) throws Exception{
			 BigDecimal price = BigDecimal.ZERO;
			 if(price!=null && rate != null) {
				 Double rateInPer = rate/100;
				 BigDecimal value = BigDecimal.ONE.add(new BigDecimal((rateInPer)));
				 price = priceWithTax.divide(value,2, RoundingMode.HALF_UP);
			 }
			 return price;
		  }
	  
	  public Instant convertStringIntoInstant(String dateStr,String format) {
		  Instant returnDate = null;
		  DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		    String timestamp = dateStr+" 11:00:02";
		    TemporalAccessor temporalAccessor = formatter.parse(timestamp);
		    LocalDateTime localDateTime = LocalDateTime.from(temporalAccessor);
		    ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
		    returnDate = Instant.from(zonedDateTime);
		  return returnDate;
	  }
	  
	  public String convertInstantIntoString(Instant instDate,String format) {
		  String dateStr=null;
		  DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(format)
		            .withZone(ZoneId.systemDefault());
		  dateStr = DATE_TIME_FORMATTER.format(instDate);
		  return dateStr;
	  }
	  
	  

	  
	  public Instant convertStringIntoInstantNoTime(String dateStr,String format) {
		  Instant returnDate = null;
		  DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		    String timestamp = dateStr;
		    TemporalAccessor temporalAccessor = formatter.parse(timestamp);
		    LocalDateTime localDateTime = LocalDateTime.from(temporalAccessor);
		    ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
		    returnDate = Instant.from(zonedDateTime);
		  return returnDate;
	  }
}
