package com.rameses.bi.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.rmi.server.UID;
import java.util.Map;
import java.util.Random;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

/**
 * This is the basic Graph Handler class, this contains
 * all the shared properties for all graphs.
 *
 * @author elmo
 */
public abstract class AbstractChartHandler implements Serializable {
    
    private String objid;
    private String width= "500";
    private String height= "300";
    private String title = "<no title>";
    private String onclick;
    private String timer;
    private String topCaption= "";
    private String bottomCaption;
    private String xlabel = "category";
    private String ylabel = "value";
    private boolean display3D = true;
    private boolean includeLegend = true;
    private boolean includeTooltips = true;
    private boolean includeUrl = false;
    private byte[] bytes;
    private String id;
    
    public AbstractChartHandler() {
        objid = "CHRT" + new UID();
    }
    
    public Map getMap() {
        return null;
    }
    
    public void setAttributes( Map map ) {
        if(map.get("xlabel")!=null) setXlabel((String) map.get("xlabel"));
        if(map.get("title")!=null ) setTitle((String) map.get("title"));
        if(map.get("width")!=null ) setWidth((String) map.get("width"));
        if(map.get("height")!=null) setHeight((String) map.get("height"));
        if(map.get("ylabel")!=null ) setYlabel((String)map.get("ylabel"));
        if(map.get("topCaption")!=null ) setTopCaption((String) map.get("topCaption"));
        if(map.get("is3D") != null) setDisplay3d(Boolean.valueOf((String)map.get("is3D")));
        if(map.get("timer")!=null) setTimer((String) map.get("timer"));
    }
    
    // <editor-fold defaultstate="collapsed" desc="GETTER/SETTER">
    
    
    public String getObjid() {
        return objid;
    }
    
    public boolean is3D(){
        return this.display3D;
    }
    
    public String getWidth() {
        return width;
    }
    
    public String getHeight() {
        return height;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getTimer() {
        return timer;
    }
    
    public String getTopCaption() {
        return topCaption;
    }
    
    public String getBottomCaption() {
        return bottomCaption;
    }
    
    
    public void setWidth(String width) {
        this.width = width;
    }
    
    public void setHeight(String height) {
        this.height = height;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getOnclick() {
        return onclick;
    }
    
    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }
    
    public void setTimer(String timer) {
        this.timer = timer;
    }
    
    public void setTopCaption(String topCaption) {
        this.topCaption = topCaption;
    }
    
    public void setBottomCaption(String bottomCaption) {
        this.bottomCaption = bottomCaption;
    }
    
    public boolean isDisplay3d() {
        return display3D;
    }
    
    public void setDisplay3d(boolean is3D) {
        this.display3D = is3D;
    }
    
    public boolean isIncludeLegend() {
        return includeLegend;
    }
    
    public void setIncludeLegend(boolean includeLegend) {
        this.includeLegend = includeLegend;
    }
    
    public boolean isIncludeTooltips() {
        return includeTooltips;
    }
    
    public void setIncludeTooltips(boolean includeTooltips) {
        this.includeTooltips = includeTooltips;
    }
    
    public boolean isIncludeUrl() {
        return includeUrl;
    }
    
    public void setIncludeUrl(boolean includeUrl) {
        this.includeUrl = includeUrl;
    }
    
    public abstract JFreeChart createChart();
    
    public String getXlabel() {
        return xlabel;
    }
    
    public void setXlabel(String xlabel) {
        this.xlabel = xlabel;
    }
    
    public String getYlabel() {
        return ylabel;
    }
    
    public void setYlabel(String ylabel) {
        this.ylabel = ylabel;
    }
    // </editor-fold>
    
    public void generateChart() {
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            ChartUtilities.writeChartAsPNG( bos, createChart(), 500, 300 );
            bytes = bos.toByteArray();
        } catch(Exception ex) {
            //error
            System.out.println("error in grpah " + ex.getMessage() );
        } finally {
            try { bos.close(); } catch(Exception ign){;}
        }
    }
    
    public InputStream getInputStream() {
        return new ByteArrayInputStream( bytes );
    }
    
    public byte[] getBytes() {
        return bytes;
    }
    
    public void close() {
        bytes = null;
    }
        
}
