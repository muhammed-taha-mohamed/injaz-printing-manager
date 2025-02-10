package com.injaz.print.controller;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.validation.Valid;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.injaz.print.payload.InjazCashupCloseRestModel;
import com.injaz.print.payload.InjazPrintBarCodeProductRestModel;
import com.injaz.print.payload.InjazPrintOrderRestModel;
import com.injaz.print.service.InjazPrintService;
import com.injaz.print.service.PrinterService;





@RestController
@RequestMapping("/print")
public class InjazPrintingController {
	
	@Autowired
	private Environment env;

	@Autowired
	private InjazPrintService injazPrintService;
 
	
	 private final Logger logger = LoggerFactory.getLogger(this.getClass());
	  
	  @PostMapping("/kitchenReceipt")
	  public String   printKitchenReceipt(@Valid @RequestBody String jsonOrder) {
         try {
        	 //logger.info("** begin kitchenReceipt ***");
        	 ObjectMapper mapper = new ObjectMapper();
     	     InjazPrintOrderRestModel order = mapper.readValue(jsonOrder, InjazPrintOrderRestModel.class);
        	 injazPrintService.printKitchenReceipt(order);
        	 //logger.info("** end kitchenReceipt ***");
         }
         catch (Exception e) {
				e.printStackTrace();
				return "Error";
			}
			
		  
	  return "success";
	  }

	  @PostMapping("/orderReceipt")
	  public String   printOrderReceipt(@Valid @RequestBody String jsonOrder) {
         try {
        	 //logger.info("** begin orderReceipt ***");
        	 ObjectMapper mapper = new ObjectMapper();
     	     InjazPrintOrderRestModel order = mapper.readValue(jsonOrder, InjazPrintOrderRestModel.class);
        	 injazPrintService.printOrderReceipt(order);
        	// logger.info("** end orderReceipt ***");
         }
         catch (Exception e) {
				e.printStackTrace();
				return "Error";
			}
	  return "success";
	  }
	  
	  @PostMapping("/refundReceipt")
	  public String   printRefundReceipt(@Valid @RequestBody String jsonOrder) {
         try {
        	 //logger.info("** begin printRefundReceipt ***");
        	 ObjectMapper mapper = new ObjectMapper();
     	     InjazPrintOrderRestModel order = mapper.readValue(jsonOrder, InjazPrintOrderRestModel.class);
        	 injazPrintService.printRefundReceipt(order);
        	// logger.info("** end printRefundReceipt ***");
         }
         catch (Exception e) {
				e.printStackTrace();
				return "Error";
			}
	  return "success";
	  }
	  
	  @PostMapping("/checkPrint")
	  public String   printCheckPrint(@Valid @RequestBody String jsonOrder) {
         try {
        	 //logger.info("** begin orderReceipt ***");
        	 ObjectMapper mapper = new ObjectMapper();
     	     InjazPrintOrderRestModel order = mapper.readValue(jsonOrder, InjazPrintOrderRestModel.class);
        	 injazPrintService.printCheckPrint(order);
        	// logger.info("** end orderReceipt ***");
         }
         catch (Exception e) {
				e.printStackTrace();
				return "Error";
			}
	  return "success";
	  }
	
	  @PostMapping("/duplicateOrderReceipt")
	  public String   printDuplicateOrderReceipt(@Valid @RequestBody String jsonOrder) {
         try {
        	 //logger.info("** begin orderReceipt ***");
        	 ObjectMapper mapper = new ObjectMapper();
     	     InjazPrintOrderRestModel order = mapper.readValue(jsonOrder, InjazPrintOrderRestModel.class);
        	 injazPrintService.printDuplicateOrderReceipt(order);
        	// logger.info("** end orderReceipt ***");
         }
         catch (Exception e) {
				e.printStackTrace();
				return "Error";
			}
	  return "success";
	  }

	@PostMapping("/duplicateOrderReceipt_2")
	public String   printDuplicateOrderReceipt_2(@Valid @RequestBody String jsonOrder) {
		try {
			//logger.info("** begin orderReceipt ***");
			ObjectMapper mapper = new ObjectMapper();
			InjazPrintOrderRestModel order = mapper.readValue(jsonOrder, InjazPrintOrderRestModel.class);
			injazPrintService.rePrintOrderReceipt(order);
			// logger.info("** end orderReceipt ***");
		}
		catch (Exception e) {
			e.printStackTrace();
			return "Error";
		}
		return "success";
	}
	  
	  @PostMapping("/printCashUpCloseReceipt")
	  public String   printCashUpCloseReceipt(@Valid @RequestBody String jsonCashup) {
         try {
        	 //logger.info("** begin orderReceipt ***");
        	 ObjectMapper mapper = new ObjectMapper();
        	 InjazCashupCloseRestModel cashup = mapper.readValue(jsonCashup, InjazCashupCloseRestModel.class);
        	 injazPrintService.printCashUpCloseReceipt(cashup);
        	// logger.info("** end orderReceipt ***");
         }
         catch (Exception e) {
				e.printStackTrace();
				return "Error";
			}
	  return "success";
	  }
	  
	  @PostMapping("/printBarcodeProducts")
	  public String   printBarcodeProducts(@Valid @RequestBody String jsonmodel) {
         try {
        	 ObjectMapper mapper = new ObjectMapper();
        	 InjazPrintBarCodeProductRestModel model = mapper.readValue(jsonmodel, InjazPrintBarCodeProductRestModel.class);
        	 injazPrintService.printBarcodeProducts(model);
         }
         catch (Exception e) {
				e.printStackTrace();
				return "Error";
			}
	  return "success";
	  }
}
