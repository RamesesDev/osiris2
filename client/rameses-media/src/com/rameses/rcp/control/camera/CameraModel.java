package com.rameses.rcp.control.camera;

import com.keypoint.PngEncoder;
import com.rameses.util.ValueUtil;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.rmi.server.UID;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Windhel
 */
public class CameraModel {
    
    private Image image;
    
    public CameraModel() {
    }
    
    
    //<editor-fold defaultstate="collapsed" desc=" setter / getter ">
    public Image getImage() { return image; }
    public void setImage(Image image) { this.image = image; }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc=" byte[] getBytes() ">
    public byte[] getBytes() {
        PngEncoder penc = new PngEncoder(getImage());
        return penc.pngEncode();
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc=" saveImage(String destination, String fileName) ">
    public void saveImage(String destination, String fileName) {
        try {
            if(ValueUtil.isEmpty(fileName))
                fileName = "IMG-" + generateFileName();

            if(ValueUtil.isEmpty(destination))
                destination = "temp\\";
            else if(!destination.endsWith("\\"))
                destination += "\\";
            
            File fileDest = new File(destination);
            if(!fileDest.exists())
                fileDest.mkdir();
            
            BufferedImage bi = getBufferedImage(image);
            OutputStream os = new FileOutputStream(destination + fileName + ".jpg");
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
            JPEGEncodeParam encParm = encoder.getDefaultJPEGEncodeParam(bi);
            
            float quality = 1.0F;
            encParm.setQuality(quality, true);
            encoder.setJPEGEncodeParam(encParm);
            encoder.encode(bi);
            os.close();
        }catch(Exception ex) { ex.printStackTrace(); }
    }
    
    private BufferedImage getBufferedImage(Image image) {
        int height = image.getHeight(null);
        int width = image.getWidth(null);
        
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.OPAQUE);
        Graphics g =  bi.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.drawImage(image, 0, 0, null);
        
        g.dispose();
        return bi;
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc=" String generateFileName() ">
    public String generateFileName() {
        StringBuffer sb = new StringBuffer();
        sb.append(new UID().toString().replaceAll("[-:^<>%$]", ""));
        return sb.toString();
    }
    //</editor-fold>
}
