package org.alexander.berg.JpgToPdfConverter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

public class App {
	
	private static final String JPG_DIR_PATH = "c:\\jpgs";
	
    public static void main(String[] args) {
    	renameSequence();
    	try (Stream<Path> walk = Files.walk(Paths.get(JPG_DIR_PATH))) {
    		Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(new File(JPG_DIR_PATH, "book.pdf")));
            document.open();
    		walk.map(x -> x.toString()).filter(f -> f.toLowerCase().endsWith(".jpg") || f.toLowerCase().endsWith(".jpeg")).forEach(filePath -> {
    			document.newPage();
				try {
					System.out.println(filePath);
					Image image = Image.getInstance(filePath);
					image.setAbsolutePosition(0, 0);
	                image.setBorderWidth(0);
	                image.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
	                
	                float x = (PageSize.A4.getWidth() - image.getScaledWidth()) / 2;
	                float y = (PageSize.A4.getHeight() - image.getScaledHeight()) / 2;
	                image.setAbsolutePosition(x, y);
	                document.add(image);
				} catch (IOException | DocumentException e) {
					e.printStackTrace();
				}
    		});
    		document.close();
    	} catch (IOException | DocumentException e) {
    		e.printStackTrace();
    	}
    }
    
    private static void renameSequence() {
    	try (Stream<Path> walk = Files.walk(Paths.get(JPG_DIR_PATH))) {
    		walk.map(x -> x.toString()).filter(f -> f.toLowerCase().endsWith(".jpg") || f.toLowerCase().endsWith(".jpeg")).forEach(filePath -> {
    			String sequence = filePath.substring(filePath.lastIndexOf("-") + 1, filePath.lastIndexOf("."));
    			System.out.println(sequence);
    			if (sequence.length() == 2) {
    				File file = new File(filePath);
    				file.renameTo(new File(file.toString().replace(sequence, "00" + sequence)));
    			} else if (sequence.length() == 3) {
    				File file = new File(filePath);
    				file.renameTo(new File(file.toString().replace(sequence, "0" + sequence)));
    			}
    		});
    		
    	} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
