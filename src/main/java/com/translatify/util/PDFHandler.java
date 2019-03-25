package com.translatify.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import org.springframework.stereotype.Repository;

//This does not work right

@Repository
public class PDFHandler {
	public String createPDF(String content, String filename) throws IOException {
		String filepath = "files/" + filename + ".pdf";
		PDDocument document = new PDDocument();
		PDPage page = new PDPage();
		document.addPage(page);
		PDPageContentStream contentStream = new PDPageContentStream(document, page);
		PDFont pdfFont = PDType1Font.TIMES_ROMAN;
	    float fontSize = 12;
	    float leading = 1.5f * fontSize;
	    PDRectangle mediabox = page.getMediaBox();
	    float margin = 72;
	    float width = mediabox.getWidth() - 2*margin;
	    float startX = mediabox.getLowerLeftX() + margin;
	    float startY = mediabox.getUpperRightY() - margin;
	    content = content.replaceAll("\n", " ").replaceAll("\r", " ").replaceAll("\t", " ");
	    String text = content; 
	    List<String> lines = new ArrayList<String>();
	    int lastSpace = -1;		
	    while (text.length() > 0) {
	        int spaceIndex = text.indexOf(' ', lastSpace + 1);
	        if (spaceIndex < 0)
	            spaceIndex = text.length();
	        String subString = text.substring(0, spaceIndex);
	        float size = fontSize * pdfFont.getStringWidth(subString) / 1000;
	        //System.out.printf("'%s' - %f of %f\n", subString, size, width);
	        if (size > width) {
	            if (lastSpace < 0)
	                lastSpace = spaceIndex;
	            subString = text.substring(0, lastSpace);
	            lines.add(subString);
	            text = text.substring(lastSpace).trim();
	            //System.out.printf("'%s' is line\n", subString);
	            lastSpace = -1;
	        } else if (spaceIndex == text.length()) {
	            lines.add(text);
	            //System.out.printf("'%s' is line\n", text);
	            text = "";
	        } else {
	        	lastSpace = spaceIndex;
	        }
	    }
	    contentStream.beginText();
	    contentStream.setFont(pdfFont, fontSize);
	    contentStream.newLineAtOffset(startX, startY);
	    for (String line: lines) {
	    	contentStream.showText(line);
	        contentStream.newLineAtOffset(0, -leading);
	    }
	    contentStream.endText(); 
	    contentStream.close();
		document.save(filepath);
		System.out.println("Doc saved!");
		document.close();
		return filepath;
	}
}
