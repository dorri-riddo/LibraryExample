package pdfImage2;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

public class Main {

	public static void main(String[] args) {
		Main m = new Main();
		
		String destinationDir = "";
		String url = "";
		m.converPdfToImage_url(destinationDir, url);
		
		String sourceDir = "";
		m.converPdfToImage_file(destinationDir, sourceDir);

	}
	
	public void converPdfToImage_file(String destinationDir, String sourceDir) {
	      try {
	          File sourceFile = new File(sourceDir);
	          File destinationFile = new File(destinationDir);
	          if (!destinationFile.exists()) {
	              destinationFile.mkdir();
	          }
	          if (sourceFile.exists()) {	                   
	              PDDocument document = PDDocument.load(sourceFile);
	              String fileName = sourceFile.getName().replace(".pdf", "");             
	              int pageCount = 0;
	              PDFRenderer renderer = new PDFRenderer(document);
	              
	              for (PDPage page : document.getPages()) {
	            	  BufferedImage image = renderer.renderImageWithDPI(pageCount, 350, ImageType.RGB);
	                  File outputfile = new File(destinationDir + fileName +"_"+ (pageCount++) +".png");
	                  System.out.println("Image Created -> "+ outputfile.getName());
	                  ImageIO.write(image, "png", outputfile);
	              }
	              document.close();
	              System.out.println("Converted Images are saved at -> "+ destinationFile.getAbsolutePath());
	          } else {
	              System.err.println(sourceFile.getName() +" File not exists");
	          }

	      } catch (Exception e) {
	          e.printStackTrace();
	      }
	}
	
	public void converPdfToImage_url(String destinationDir, String url) {
       try {
    	   // URL 링크 처리
           InputStream is = new URL(url).openStream();
           
           File destinationFile = new File(destinationDir);
           if (!destinationFile.exists()) {
               destinationFile.mkdir();
           }
           PDDocument document = PDDocument.load(is);  
           String fileName = "PdfToImage";

           PDFRenderer renderer = new PDFRenderer(document);
           
           int pageCount = 0;
           for (PDPage page : document.getPages()) {
        	   BufferedImage image = renderer.renderImageWithDPI(pageCount, 350, ImageType.RGB);
               File outputfile = new File(destinationDir + fileName + "_" + (pageCount++) + ".png");
               System.out.println("Image Created -> "+ outputfile.getName());
               ImageIO.write(image, "png", outputfile);
           }
             
           document.close();
           System.out.println("Converted Images are saved at -> "+ destinationFile.getAbsolutePath());
           is.close();

       } catch (Exception e) {
           e.printStackTrace();
       }
	}
}
