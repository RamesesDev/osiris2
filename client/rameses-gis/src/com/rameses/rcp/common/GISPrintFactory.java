package com.rameses.rcp.common;

import com.keypoint.PngEncoder;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrintQuality;

/**
 *
 * @author Windhel
 * 20110610
 */
public class GISPrintFactory {
    
    private static String fileName = "samp";
    
    public GISPrintFactory(String fileName) {
        this.fileName = fileName;
    }
    
    //<editor-fold defaultstate="collapsed" desc=" setter/getter ">
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" printImage() ">
    /*
     *image needs to be saved before making the pdf because
     *the Document.class under iText.jar does not support java.awt.Image
     *in adding it in its page.
     *
     *  1. save image - fixed filename
     *  2. make pdf - fixed filename
     *  3. print
     */
    public static void printImage(boolean printForegroundImage, Image foregroundImage, boolean printBackgroundImage, Image backgroundImage, GISDataModel gdm) throws Exception{
        createPDF(printForegroundImage, foregroundImage,printBackgroundImage, backgroundImage, gdm);
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        determineDocumentAttribute(pras, gdm);
        PrintService ps = PrintServiceLookup.lookupDefaultPrintService();
        DocPrintJob job = ps.createPrintJob();
        FileInputStream fin = new FileInputStream(new File(fileName + ".pdf"));
        Doc doc = new SimpleDoc(fin, DocFlavor.INPUT_STREAM.PDF, null);
        job.print(doc, pras);
        fin.close();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" determineDocumentAttribute(PrintRequestAttributeSet pras, GISDataModel gdm) ">
    private static void determineDocumentAttribute(PrintRequestAttributeSet pras, GISDataModel gdm) {
        if(gdm.getPageOrientation().toUpperCase().equals("LANDSCAPE"))
            pras.add(OrientationRequested.LANDSCAPE);
        else
            pras.add(OrientationRequested.PORTRAIT);
        
        if(gdm.getPageSize().toUpperCase().equals("LEGAL"))
            pras.add(MediaSizeName.NA_LEGAL);
        else  //default paper size is LETTER
            pras.add(MediaSizeName.NA_LETTER);
        
        pras.add(new Copies(gdm.getCopies()));
        pras.add(PrintQuality.DRAFT);
        
        //implementing classes to DocAttribute
        //Chromaticity, Compression, DocumentName, Finishings,
        //Media, MediaPrintableArea, NumberUp, OrientationRequested,
        //PageRanges, PrinterResolution, PrintQuality, SheetCollate, Sides
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" saveImage() ">
    /*
     *PngEncoder is a class that makes a PNG out of java.awt.Image
     *PngEncoder(Image)
     *PngEncoderB(BufferedImage)
     */
    public static void saveImage(boolean printForegroundImage, Image foregroundImage, boolean printBackgroundImage, Image backgroundImage, GISDataModel gdm) throws Exception{
        //save foregroundImage to PNG
        Image image = null;
        if(printForegroundImage && !printBackgroundImage)
            image = getImage(foregroundImage,gdm);
        else if(!printForegroundImage && printBackgroundImage)
            image = getImage(backgroundImage, gdm);
        else
            image = combineImages(foregroundImage, backgroundImage, gdm);
        
        PngEncoder pngenc = new PngEncoder(image);
        pngenc.setCompressionLevel(9);
        byte[] byteImage = pngenc.pngEncode();
        OutputStream out = new FileOutputStream(fileName + ".png");
        try {
            out.write(byteImage);
        }finally{
            out.close();
        }
        //save foregroundImage to JPG
//        BufferedImage bi = getBufferedImage(foregroundImage, gdm);
//        OutputStream os = new FileOutputStream(fileName + ".jpg");
//        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
//        JPEGEncodeParam encParm = encoder.getDefaultJPEGEncodeParam(bi);
//
//        float quality = 1.0F;
//        encParm.setQuality(quality, true);
//        encoder.setJPEGEncodeParam(encParm);
//        encoder.encode(bi);
//        os.close();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" combineImages(Image image) ">
   private static Image combineImages(Image foregroundImage, Image backgroundImage, GISDataModel gdm) {
        int height = foregroundImage.getHeight(null);
        int width = foregroundImage.getWidth(null);
        
        Image bi = new BufferedImage(width, height, BufferedImage.OPAQUE);

        Graphics g = bi.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.drawImage(backgroundImage, 0, 0,null);
        g.drawImage(foregroundImage, 0, 0, null);
        
        g.dispose();
        return scaleImage(bi, gdm);
    }
    //</editor-fold>
   
    //<editor-fold defaultstate="collapsed" desc=" getImage(Image image) ">
    private static Image getImage(Image image, GISDataModel gdm) {
        int height = image.getHeight(null);
        int width = image.getWidth(null);
        
        Image bi = new BufferedImage(width, height, BufferedImage.OPAQUE);
        Graphics g = bi.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.drawImage(image, 0, 0, null);
        
        g.dispose();
        return scaleImage(bi, gdm);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" getBufferedImage(Image image) ">
    private static BufferedImage getBufferedImage(Image image, GISDataModel gdm) {
        int height = image.getHeight(null);
        int width = image.getWidth(null);
        
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.OPAQUE);
        Graphics g =  bi.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        //g.setComposite(AlphaComposite.Src);
        g.drawImage(image, 0, 0, null);
        
        g.dispose();
        return scaleBufferedImage(bi, gdm);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" scaleImage(Image image) ">
    /*
     *default pageSize is LETTER
     *LETTER: width = 8, height = 11
     *dots per inch = 72
     *margin: 1 in all sides
     */
    private static Image scaleImage(Image image, GISDataModel gdm) {
        double pageWidth = 7 * 72;
        double pageHeight = 10 * 72;
        double imageWidth = (double)image.getWidth(null);
        double imageHeight = (double)image.getHeight(null);
        double previewWidth =  (double)image.getWidth(null);
        double previewHeight = (double)image.getHeight(null);
        double scale = 1.0;
        
        if(gdm.getPageSize().equals("LEGAL")) {
            pageWidth = 7 * 72;
            pageHeight = 12 * 72;
        }
        
        if(pageHeight < imageHeight) {
            scale = pageHeight / imageHeight;
            previewHeight = pageHeight;
            previewWidth = scale * imageWidth;
        }
        
        if(pageWidth < imageWidth) {
            scale = pageWidth / imageWidth;
            previewWidth = pageWidth;
            previewHeight = scale * imageHeight;
        }
        
        Image prev = image.getScaledInstance((int)previewWidth, (int)previewHeight, Image.SCALE_SMOOTH);
        Image output = new BufferedImage((int)previewWidth, (int)previewHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics g = output.getGraphics();
        g.drawImage(prev, 0, 0, null);
        
        return output;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" scaleBufferedImage(Image image) ">
    /*
     *default pageSize is LETTER
     *LETTER: width = 8, height = 11
     *dots per inch = 72
     *margin: 1 in all sides
     */
    private static BufferedImage scaleBufferedImage(Image image, GISDataModel gdm) {
        double pageWidth = 7 * 72;
        double pageHeight = 10 * 72;
        double imageWidth = (double)image.getWidth(null);
        double imageHeight = (double)image.getHeight(null);
        double previewWidth =  (double)image.getWidth(null);
        double previewHeight = (double)image.getHeight(null);
        double scale = 1.0;
        
        if(gdm.getPageSize().equals("LEGAL")) {
            pageWidth = 7 * 72;
            pageHeight = 12 * 72;
        }
        
        if(pageHeight < imageHeight) {
            scale = pageHeight / imageHeight;
            previewHeight = pageHeight;
            previewWidth = scale * imageWidth;
        }
        
        if(pageWidth < imageWidth) {
            scale = pageWidth / imageWidth;
            previewWidth = pageWidth;
            previewHeight = scale * imageHeight;
        }
        
        Image prev = image.getScaledInstance((int)previewWidth, (int)previewHeight, Image.SCALE_SMOOTH);
        BufferedImage output = new BufferedImage((int)previewWidth, (int)previewHeight, BufferedImage.OPAQUE);
        Graphics g = output.getGraphics();
        g.drawImage(prev, 0, 0, null);
        
        return output;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" createPDF() ">
    /*
     *headerPhrase - for Headers
     *footerPhrase - for Footers
     *par_01 - for the paragraph after the image
     */
    public static void createPDF(boolean includeForegroundImage, Image foregroundImage, boolean includeBackgroundImage, Image backgroundImage, GISDataModel gdm) throws Exception {
        saveImage(includeForegroundImage, foregroundImage, includeBackgroundImage, backgroundImage, gdm);
        Document doc = new Document(PageSize.LETTER);
        PdfWriter.getInstance(doc, new FileOutputStream(fileName + ".pdf"));
        
        Phrase headerPhrase = new Phrase();
        Font headerFont = new Font();
        headerFont.setColor(Color.BLACK);
        headerFont.setStyle(Font.BOLD);
        headerFont.setSize(14.0f);
        headerFont.setFamily("Tahoma");
        headerPhrase.setFont(headerFont);
        for(String str : gdm.getHeaders()) {
            headerPhrase.add(str + "\n");
        }
        
        HeaderFooter header = new HeaderFooter(headerPhrase, false);
        header.setAlignment(HeaderFooter.ALIGN_CENTER);
        header.setBorder(0);
        doc.setHeader(header);
        
//        Phrase footerPhrase = new Phrase();
//        for(String str : gdm.getFooters()) {
//            footerPhrase.add(str + "\n");
//        }
//        HeaderFooter footer = new HeaderFooter(footerPhrase, false);
//        footer.setAlignment(HeaderFooter.ALIGN_LEFT);
//        footer.setBorder(0);
//        doc.setFooter(footer);
        
        Paragraph par_01 = new Paragraph();
        par_01.setSpacingBefore(10.0f);
        for(String str : gdm.getFooters()) {
            par_01.add(str + "\n");
        }
        
        doc.open();
        doc.addCreator("Rameses Systems Inc. : ETRACS System V2 : GIS");
        doc.addAuthor("Rameses Systems Inc. : ETRACS System V2 : GIS");
        doc.addTitle("GIS Printout");
        
        com.lowagie.text.Image img =  com.lowagie.text.Image.getInstance(fileName + ".png");
        
        doc.add(img);
        doc.add(par_01);
        doc.close();
    }
    //</editor-fold>
    
}