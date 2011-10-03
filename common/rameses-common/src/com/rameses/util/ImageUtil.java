/*
 * ImageUtil.java
 * Created on September 29, 2011, 2:33 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 */
package com.rameses.util;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;
/**
 *
 * @author windhel
 * 
 * example usage:
 *    ImageUtil u = new ImageUtil();
 *    u.createThumbnail( is, out, "jpg" ) 
 */

public class ImageUtil {
    
    public static String TYPE_JPG = "jpg";
    public static String TYPE_PNG = "png";
    public static String TYPE_GIF = "gif";
    
    private int size = 186;
    
    public ImageUtil() {
    }
    
    public void createThumbnail(String source, String target, String type) {
        try {
            BufferedImage bi = ImageIO.read(new File(source));
            ImageIO.write(makeThumbnail(bi), type, new File(target));
        }catch(Exception ex) { ex.printStackTrace(); }
    }
    
    public void createThumbnail(File source, File target, String type) {
        try {
            BufferedImage bi = ImageIO.read(source);
            ImageIO.write(makeThumbnail(bi), type, target);
        }catch(Exception ex){ ex.printStackTrace(); }
    }
    
    public void createThumbnail(InputStream source, OutputStream target, String type) {
        try{
            BufferedImage bi = ImageIO.read(source);
            ImageIO.write(makeThumbnail(bi), type, target);
        }catch(Exception ex) { ex.printStackTrace(); }
    }
    
    public BufferedImage makeThumbnail(BufferedImage image) throws Exception{
        int percentage = (int)( (double)getSize()/(double)image.getWidth() * 100 );
        int thumbwidth = getSize();
        int thumbheight = (int)( image.getHeight() * percentage / 100 );
        BufferedImage img = new BufferedImage(thumbwidth, thumbheight, image.getType());
        Graphics g = img.getGraphics();
        g.drawImage(image, 0, 0, thumbwidth, thumbheight, null);
        g.dispose();
        
        return (BufferedImage)img;
    }
    
    public int getSize() {
        return size;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
    
}

