package com.rameses.rcp.control.barcode;

import com.rameses.util.ValueUtil;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author Windhel
 */
public class BarcodeModel {
    
    private String serverURL = "localhost:8080";
    private String data = "12345";
    private String type = "Code128B";
    private String width = "2";
    private String height = "2";
    private String resolution = "40";
    private String checksum = "false";
    private String headless = "false";
    private String drawText = "false";
    
    public BarcodeModel() {
    }
    
    //<editor-fold defaultstate="collapsed" desc=" Setter/Getter ">
    public String getServerURL() { return serverURL; }
    public void setServerURL(String serverURL) { this.serverURL = serverURL; }
    
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getWidth() { return width; }
    public void setWidth(String width) { this.width = width; }
    
    public String getHeight() { return height; }
    public void setHeight(String height) { this.height = height; }
    
    public String getResolution() { return resolution; }
    public void setResolution(String resolution) { this.resolution = resolution; }
    
    public String getChecksum() { return checksum; }
    public void setChecksum(String checksum) { this.checksum = checksum; }
    
    public String getHeadless() { return headless; }
    public void setHeadless(String headless) { this.headless = headless; }
    
    public String getDrawText() { return drawText; }
    public void setDrawText(String drawText) { this.drawText = drawText;}
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" helper methods ">
    public String generateStringURL(String data) {
        StringBuffer sb = new StringBuffer();
        sb.append("http://"+ getServerURL() +"/barbecue/barcode?data=");
        if(ValueUtil.isEmpty(data))
            sb.append(getData());
        else
            sb.append(data);
        
        sb.append("&type=" + getType());
        sb.append("&width=" + getWidth());
        sb.append("&height=" + getHeight());
        sb.append("&resolution=" + getResolution());
        sb.append("&checksum=" + getChecksum());
        sb.append("&headless=" + getHeadless());
        sb.append("&drawText=" + getDrawText());
        
        return sb.toString();
    }
    
    public URL generateURL(String data) {
        URL url = null;
        try {
            url = new URL(generateStringURL(data));
        }catch(Exception ex) {
            ex.printStackTrace() ;
        }finally{
            return url;
        }
    }
    
    public Image generateImage(String data) {
        ImageIcon imageIcon = null;
        try {
            imageIcon = new ImageIcon(generateURL(data));
        }catch(Exception ex) {
            ex.printStackTrace();
        }finally {
            return imageIcon.getImage();
        }
    }
    //</editor-fold>
}
