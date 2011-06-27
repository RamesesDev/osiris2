package com.rameses.rcp.control.gis;

import java.awt.Font;
import java.util.List;
import java.util.Map;

/*
 * @author Windhel
 * 20110530
 */
public abstract class GISDataModel {
    
    private Listener listener;
    
    private Object boundingBox;
    private String tableName;
    private String serverURL;
    private String topLayerMap;
    private String bottomLayerMap;
    private String epsgCode = "EPSG:4326";
    private double expandBy = 1.0;
    private String cqlquery;
    private String topLayout = "layout:top";
    private String bottomLayout = "layout:bottom";
    private String topCustomSLD;
    private String bottomCustomSLD;
    
    private String topLayerName;
    private String bottomLayerName;
        
    //for printing
    private String topImageURL;
    private String bottomImageURL;
    private String headers[];
    private String footers[];
    private String pageOrientation = "PORTRAIT";
    private String pageSize = "LETTER";
    private Font font = new Font("Dialog", Font.PLAIN, 11);
    private int copies = 1;
    
    private List<SLDRuleModel> rules;
    
    public GISDataModel() {}
    
    public static interface Listener {
        void search(String filter, Object env);
        void zoomIn(double reduceBy);
        void zoomOut(double expandBy);
        void print(boolean printForegroundImage, boolean printBackgroundImage);
        void save(boolean saveForegroundImage, boolean saveBackgroundImage);
        void createPDF(boolean includeForegroundImage, boolean includeBackgroundImage);
    }
    
    //<editor-fold defaultstate="collapsed" desc=" helper methods ">
    public void save(boolean saveForegroundImage, boolean saveBackgroundImage) {
        if(listener != null)
            listener.save(saveForegroundImage, saveBackgroundImage);
    }
    
    public void search(String filter, Object env) {
        if(listener != null)
            listener.search(filter, env);
    }
    
    public void zoomIn() {
        if(listener != null)
            listener.zoomIn(-1.0);
    }
    
    public void zoomOut() {
        if(listener != null)
            listener.zoomOut(1.0);
    }
    
    public void print(boolean printForegroundImage, boolean printBackgroundImage) {
        if(listener != null)
            listener.print(printForegroundImage, printBackgroundImage);
    }
    
    public void createPDF(boolean includeForegroundImage, boolean includeBackgroundImage) {
        if(listener != null)
            listener.createPDF(includeForegroundImage, includeBackgroundImage);
    }
    
    public Map<String, List> getRules() {
        return null;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" Setter/Getter ">
    public String getBottomLayout() { return bottomLayout; }
    public void setBottomLayout(String bottomLayout) { this.bottomLayout =  bottomLayout; }
    
    public Listener getListener() { return listener; }
    public void setListener(Listener listener) { this.listener = listener; }
    
    public Object getBoundingBox() { return boundingBox; }
    public void setBoundingBox(Object boundingBox) { this.boundingBox = boundingBox; }
    
    public String getServerURL() { return serverURL; }
    public void setServerURL(String serverURL) { this.serverURL = serverURL; }
    
    public String getTopLayerMap() { return topLayerMap; }
    public void setTopLayerMap(String topLayerMap) { this.topLayerMap = topLayerMap; }
    
    public String getBottomLayerMap() { return bottomLayerMap; }
    public void setBottomLayerMap(String bottomLayerMap) { this.bottomLayerMap = bottomLayerMap; }
    
    public String getEpsgCode() {  return epsgCode; }
    public void setEpsgCode(String epsgCode) { this.epsgCode = epsgCode; }
    
    public double getExpandBy() { return expandBy; }
    public void setExpandBy(double expandBy) { this.expandBy = expandBy; }
    
    public String getCqlquery() { return cqlquery; }
    public void setCqlquery(String cqlquery) { this.cqlquery = cqlquery; }
    
    public String getTopLayout() { return topLayout; }
    public void setTopLayout(String layout) { this.topLayout = layout; }
    
    public String getTopCustomSLD() {  return topCustomSLD; }
    public void setTopCustomSLD(String topCustomSLD) { this.topCustomSLD = topCustomSLD; }
    
    public String getBottomCustomSLD() { return bottomCustomSLD; }
    public void setBottomCustomSLD(String bottomCustomSLD) { this.bottomCustomSLD = bottomCustomSLD; }
    
    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }
    
    public String getTopImageURL() { return topImageURL; }
    public String getBottomImageURL() { return bottomImageURL; }

    public void setTopImageURL(String topImageURL) { this.topImageURL = topImageURL; }
    public void setBottomImageURL(String bottomImageURL) { this.bottomImageURL = bottomImageURL; }
    
    public String[] getHeaders() { return headers; }
    public void setHeaders(String[] headers) { this.headers = headers; }

    public String[] getFooters() { return footers; }
    public void setFooters(String[] footers) { this.footers = footers; }

    public String getPageOrientation() { return pageOrientation; }
    public void setPageOrientation(String pageOrientation) { this.pageOrientation = pageOrientation; }

    public String getPageSize() { return pageSize; }
    public void setPageSize(String pageSize) throws Exception { 
        if(pageSize.equals("LEGAL")) {
            this.pageSize = pageSize; 
            return;
        } else if(pageSize.equals("LETTER")) {
            this.pageSize = pageSize;
            return;
        }
        
        throw new Exception("Paper Size : " + pageSize + " -  is not Currently Supported");
    }

    public Font getFont() { return font; }
    public void setFont(Font font) { this.font = font; }
    public void setFont(String fontName, String fontStyle, int fontSize) throws Exception {
        int style = Font.PLAIN;
        if(fontStyle.toUpperCase().equals("BOLD")) 
            style = Font.BOLD;
        
        this.font = new Font(fontName, style, fontSize);
    }

    public int getCopies() { return copies; }
    public void setCopies(int copies) { this.copies = copies; }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" init(String topLayerName, String bottomLayerName) ">
    public void init(String topLayerName, String bottomLayerName) {
        this.topLayerName = topLayerName;
        this.bottomLayerName = bottomLayerName;
        
        Map rules = getRules();
        if(rules!=null) {
            List topRules = (List) rules.get("top");
            if(topRules != null)
                topCustomSLD = SLDGenerator.createCustomSLD(topLayerName, topRules);
            
            List bottomRules = (List) rules.get("bottom");
            if(bottomRules != null)
                bottomCustomSLD = SLDGenerator.createCustomSLD(bottomLayerName, bottomRules);
        }
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" rebuildRules() ">
    public void rebuildRules() {
        Map rules = getRules();
        if(rules!=null) {
            List topRules = (List) rules.get("top");
            if(topRules != null)
                topCustomSLD = SLDGenerator.createCustomSLD(topLayerName, topRules);
            
            List bottomRules = (List) rules.get("bottom");
            if(bottomRules != null)
                bottomCustomSLD = SLDGenerator.createCustomSLD(bottomLayerName, bottomRules);
        }
    }
    //</editor-fold>
}
