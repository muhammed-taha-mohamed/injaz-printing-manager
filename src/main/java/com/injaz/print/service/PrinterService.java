package com.injaz.print.service;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrinterResolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrinterService implements Printable {
	
	@Autowired
	private Image imageService;
	
	public List<String> getPrinters(){
		
		DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		
		PrintService printServices[] = PrintServiceLookup.lookupPrintServices(
				flavor, pras);
		
		List<String> printerList = new ArrayList<String>();
		for(PrintService printerService: printServices){
			printerList.add( printerService.getName());
		}
		
		return printerList;
	}

	@Override
	public int print(Graphics g, PageFormat pf, int page)
			throws PrinterException {
		if (page > 0) { /* We have only one page, and 'page' is zero-based */
			return NO_SUCH_PAGE;
		}

		/*
		 * User (0,0) is typically outside the imageable area, so we must
		 * translate by the X and Y values in the PageFormat to avoid clipping
		 */
		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(pf.getImageableX(), pf.getImageableY());
		/* Now we perform our rendering */

		g.setFont(new Font("Roman", 0, 8));
		g.drawString("Hello world !", 0, 10);

		return PAGE_EXISTS;
	}

	public void printString(String printerName, String text) {
		
		// find the printService of name printerName
		DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();

		PrintService printService[] = PrintServiceLookup.lookupPrintServices(
				flavor, pras);
		PrintService service = findPrintService(printerName, printService);

		DocPrintJob job = service.createPrintJob();

		try {

			byte[] bytes;

			// important for umlaut chars
			bytes = text.getBytes("CP437");

			Doc doc = new SimpleDoc(bytes, flavor, null);

			
			job.print(doc, null);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void printBytes(String printerName, FileInputStream fin) {
		
		DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();

		PrintService printService[] = PrintServiceLookup.lookupPrintServices(
				flavor, pras);
		PrintService service = findPrintService(printerName, printService);

		DocPrintJob job = service.createPrintJob();

		try {

			/*Doc doc = new SimpleDoc(bytes, flavor, null);

			job.print(doc, null);*/
			//pras.add(MediaSizeName.);
			pras.add(new PrinterResolution(125, 125, PrinterResolution.DPI));
			//pras.add(new MediaPrintableArea(2, 2, 110 - 4, 197 - 4, MediaPrintableArea.MM));
			
		    Doc doc = new SimpleDoc(fin, DocFlavor.INPUT_STREAM.JPEG, null);
            
		    job.print(doc, pras);
		    
		    fin.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private PrintService findPrintService(String printerName,
			PrintService[] services) {
		for (PrintService service : services) {
			if (service.getName().equalsIgnoreCase(printerName)) {
				return service;
			}
		}

		return null;
	}
	
	
public void printImage(BufferedImage  image) {
		

		try {
			imageService.printImage(image, "EPSON TM-T20II Receipt");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
