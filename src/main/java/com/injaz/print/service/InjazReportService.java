package com.injaz.print.service;

import java.awt.print.PrinterJob;
import java.util.Map;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.injaz.print.util.InjazReplace;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@Service
public class InjazReportService {
	
	@Autowired
	private Environment env;
	
	
	public boolean generateBeanReport(String reportName, JRBeanCollectionDataSource jrBeanCollectionDataSource,
			Map<String, Object> parameters,String selectedPrinter) throws Exception {
		boolean success = false;
		try {

			String reportPath = env.getProperty("REPORTS_PATH");

			JasperDesign jasperDesign = JRXmlLoader.load(reportPath + reportName);

			Object[] reportParameters = jasperDesign.getParametersList().toArray();

			for (int i = 0; i < reportParameters.length; i++) {
				final JRDesignParameter parameter = (JRDesignParameter) reportParameters[i];
				if (parameter.getName().startsWith("SUBREP_")) {
					String parameterName = parameter.getName();
					String subReportName = InjazReplace.replace(parameterName, "SUBREP_", "") + ".jrxml";
					parameters.put(parameterName, JasperCompileManager.compileReport(reportPath + subReportName));
				}
			}

			JasperReport jasperReport = JasperCompileManager.compileReport(reportPath + reportName);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
					jrBeanCollectionDataSource);

			JasperExportManager.exportReportToPdfFile(jasperPrint, reportPath + "barcode_products.pdf");
			
			
			success = print(jasperPrint, selectedPrinter);
			
			
			return success;


		} catch (Exception e) {
			e.printStackTrace();
		  return false;
		}
	}

	
	public  boolean print(JasperPrint jasperPrint,String selectedPrinter) {
       
		boolean printSucceed = false;
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        PrintService selectedService = null;

        if(services.length != 0 || services != null){
            for(PrintService service : services){
                String existingPrinter = service.getName();
                if(existingPrinter.equalsIgnoreCase(selectedPrinter)){
                    selectedService = service;
                    break;
                }
            }

            if(selectedService != null){
            	try {
                printerJob.setPrintService(selectedService);
             printSucceed = JasperPrintManager.printReport(jasperPrint, false);
                return printSucceed;
            	}
            	catch (Exception e) {
        			e.printStackTrace();
        		  return printSucceed;
        		}
            }
           
        }
        return printSucceed;
}




}
