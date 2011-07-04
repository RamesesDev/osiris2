package com.rameses.rcp.control;

import com.rameses.rcp.common.GISDataModel;
import com.rameses.rcp.common.GISPrintFactory;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.util.ValueUtil;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.Beans;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import javax.imageio.spi.IIORegistry;
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
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.geotools.data.ows.CRSEnvelope;
import org.geotools.data.ows.Layer;
import org.geotools.data.ows.StyleImpl;
import org.geotools.data.wms.WMSUtils;
import org.geotools.data.wms.WebMapServer;
import org.geotools.data.wms.request.GetMapRequest;
import org.geotools.geometry.jts.ReferencedEnvelope;

/*
 * @author Windhel
 * 20110518
 *
 * Two maps are requested and rendered in this component the TopLayerMap and BottomLayerMap.
 * Only the TopLayerMap has support if it is a LayerGroup. (specified in geoserver)
 *
 */
public class XWMSViewer extends JPanel implements UIControl{
    
    private StyleImpl foregroundStyle;
    private StyleImpl backgroundStyle;
    private Layer foregroundLayer;
    private Layer backgroundLayer;
    private Image foregroundImage = null;
    private Image backgroundImage = null;
    private int background_image_index;
    private int image_index;
    private Object env;
    private String cqlFilter;
    
    private String[] depends;
    private Binding binding;
    private int index;
    
    private WebMapServer wms;
    private GISDataModel model;
    
    //for panning
    Cursor panningCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
    Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
    private double[] resultXY = new double[4];
    private int zoomDifference = 7;
    private double[] minXY;
    private double[] maxXY;
    private double fromX;
    private double fromY;
    private double toX;
    private double toY;
    
    public XWMSViewer() {}
    
    //<editor-fold defaultstate="collapsed" desc=" Setter/Getter ">
    public String[] getDepends() { return depends; }
    public void setDepends(String[] depends) { this.depends = depends; }
    
    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }
    
    public void setBinding(Binding binding) { this.binding = binding; }
    public Binding getBinding() { return binding; }
    
    public int compareTo(Object o) { return UIControlUtil.compare(this, o); }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" load / refresh ">
    public void load() {
        try{
            if(Beans.isDesignTime()) return;
            
            addMouseListener(new MouseSupport());
            Object mdl = UIControlUtil.getBeanValue(this);
            if(ValueUtil.isEmpty(mdl)) throw new Exception("No model was found.:GISDataModel");
            
            if(mdl instanceof GISDataModel)
                model = (GISDataModel) mdl;
            cqlFilter = model.getCqlquery();
            
            model.setListener(new GISDataModel.Listener() {
                public void search(String filters, Object enve) {
                    model.setCqlquery(filters);
                    cqlFilter = filters;
                    
                    model.setBoundingBox(enve);
                    env = enve;
                    ((ReferencedEnvelope)env).expandBy(Double.valueOf(model.getExpandBy()));
                    model.rebuildRules();
                    getTopMap(null);
                }
                
                public void zoomIn(double reduceBy) {
                    ((ReferencedEnvelope)env).expandBy(reduceBy);
                    model.setBoundingBox(env);
                    getTopMap(null);
                }
                
                public void zoomOut(double expandBy) {
                    ((ReferencedEnvelope)env).expandBy(expandBy);
                    model.setBoundingBox(env);
                    getTopMap(null);
                }
                
                public void print(boolean printForegroundImage, boolean printBackgroundImage) {
                    try {
                        GISPrintFactory.printImage(printForegroundImage, foregroundImage, printBackgroundImage, backgroundImage, model);
                    }catch(Exception ex) { ex.printStackTrace(); }
                }
                
                public void save(boolean saveForegroundImage, boolean saveBackgroundImage) {
                    try{
                        GISPrintFactory.saveImage(saveForegroundImage, foregroundImage, saveBackgroundImage, backgroundImage,  model);
                    }catch(Exception ex) { ex.printStackTrace(); }
                }
                
                public void createPDF(boolean includeForegroundImage, boolean includeBackgroundImage) {
                    try{
                        GISPrintFactory.createPDF(includeForegroundImage, foregroundImage, includeBackgroundImage, backgroundImage, model);
                    }catch(Exception ex) { ex.printStackTrace(); }
                }
            });
            
            
            if(model.getBoundingBox() == null)
                throw new Exception("Bounding Box returns null. Check named layers if it exist in Geoserver.");
            
            env = model.getBoundingBox();
            minXY = ((ReferencedEnvelope)env).getLowerCorner().getCoordinate();
            maxXY = ((ReferencedEnvelope)env).getUpperCorner().getCoordinate();
            initializeMap();
        }catch(Exception ex) { ex.printStackTrace(); }
    }
    
    public void refresh() {}
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" initializeMap() ">
    private void initializeMap() throws Exception {
        wms = new WebMapServer(new URL("http://" + model.getServerURL() + "/geoserver/wms?service=WMS&request=GetCapabilities"));
        Set capabilities = WMSUtils.getQueryableLayers(wms.getCapabilities());
        List listOfLayers = new ArrayList(capabilities);
        if(listOfLayers.size() == 0)
            throw new Exception("Zero layers found.");
        
        if(ValueUtil.isEmpty(model.getTopLayerMap()) && ValueUtil.isEmpty(model.getBottomLayerMap()) )
            throw new Exception("No layer is set to be rendered.");
        
        for(Object obj : listOfLayers) {
            Layer layer = (Layer) obj;
            if(layer.getName().equals(model.getTopLayerMap()))
                image_index = listOfLayers.indexOf(layer);
            
            if(layer.getName().equals(model.getBottomLayerMap()))
                background_image_index = listOfLayers.indexOf(layer);
        }
        
        foregroundLayer = (Layer)listOfLayers.get(image_index);
        backgroundLayer = (Layer)listOfLayers.get(background_image_index);
        List styleBackground = backgroundLayer.getStyles();
        List style = foregroundLayer.getStyles();
        foregroundStyle = (StyleImpl) style.get(0);
        backgroundStyle = (StyleImpl) styleBackground.get(0);
        ((ReferencedEnvelope)env).expandBy(Double.valueOf(model.getExpandBy()));
        model.init(foregroundLayer.getName(), backgroundLayer.getName());
        
        getTopMap(null);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" getTopMap(double boundingbox[]) ">
    /*
     *if style is zero in size it might be a Layer Group or no default style is specified in geoserver.
     *
     **/
    private void getTopMap(double boundingbox[]) {
        try {
            if(!ValueUtil.isEmpty(model.getTopLayerMap())) {
                GetMapRequest mapRequest = wms.createGetMapRequest();
                if(!ValueUtil.isEmpty(cqlFilter))
                    mapRequest.setVendorSpecificParameter("CQL_FILTER", cqlFilter);
                //mapRequest.setVendorSpecificParameter("CQL_FILTER", model.getCqlquery());
                
                if(!ValueUtil.isEmpty(model.getTopLayout()))
                    mapRequest.setVendorSpecificParameter("format_options", "layout:" + model.getTopLayout());
                
                if(!ValueUtil.isEmpty(model.getTopCustomSLD()))
                    mapRequest.setVendorSpecificParameter("SLD", model.getTopCustomSLD());
                
                mapRequest.setTransparent(true);
                if(foregroundStyle == null)
                    mapRequest.addLayer(foregroundLayer);
                else
                    mapRequest.addLayer(foregroundLayer, foregroundStyle);
                
                CRSEnvelope box = foregroundLayer.getLatLonBoundingBox();
                box.setEPSGCode(model.getEpsgCode());
                if(boundingbox == null) {
                    ReferencedEnvelope refEnv = (ReferencedEnvelope) env;
                    box.setMaxX(refEnv.getMaxX());
                    box.setMinX(refEnv.getMinX());
                    box.setMaxY(refEnv.getMaxY());
                    box.setMinY(refEnv.getMinY());
                } else {
                    box.setMinX(boundingbox[0]);
                    box.setMinY(boundingbox[1]);
                    box.setMaxX(boundingbox[2]);
                    box.setMaxY(boundingbox[3]);
                    ((ReferencedEnvelope)env).init(box.getMinX(), box.getMaxX(), box.getMinY(), box.getMaxY());
                }
                
                minXY[0] = box.getMinX();
                minXY[1] = box.getMinY();
                maxXY[0] = box.getMaxX();
                maxXY[1] = box.getMaxY();
                mapRequest.setFormat("image/png");
                mapRequest.setVersion("1.3.3");
                mapRequest.setSRS(box.getEPSGCode());
                mapRequest.setBBox(box);
                mapRequest.setDimensions(getPreferredSize());
                String strURL = mapRequest.getFinalURL().toString().replaceAll("%'", "%25'").replaceAll(" ", "%20");
                
                System.out.println("foreground Image> " + strURL);
                model.setTopImageURL(strURL);
                
                URL url = new URL(strURL);
                ImageIcon load = new ImageIcon(url);
                foregroundImage = load.getImage();
            }
            
            if(!ValueUtil.isEmpty(model.getBottomLayerMap()))
                getBottomMap(boundingbox);
        }catch(Exception e){ e.printStackTrace(); }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" getBottomMap() ">
    private void getBottomMap(double boundingbox[]){
        try{
            GetMapRequest mapRequest = wms.createGetMapRequest();
            if(!ValueUtil.isEmpty(model.getBottomCustomSLD()))
                mapRequest.setVendorSpecificParameter("SLD", model.getBottomCustomSLD());
            
            if(ValueUtil.isEmpty(foregroundImage) && !ValueUtil.isEmpty(cqlFilter))
                mapRequest.setVendorSpecificParameter("CQL_FILTER", cqlFilter);
            
            if(!ValueUtil.isEmpty(model.getBottomLayout()))
                mapRequest.setVendorSpecificParameter("format_options", "layout:" + model.getBottomLayout());
            
            if(backgroundStyle == null)
                mapRequest.addLayer(backgroundLayer);
            else
                mapRequest.addLayer(backgroundLayer, backgroundStyle);
            
            CRSEnvelope box = backgroundLayer.getLatLonBoundingBox();
            box.setEPSGCode(model.getEpsgCode());
            if(boundingbox == null) {
                ReferencedEnvelope refEnv = (ReferencedEnvelope) env;
                box.setMaxX(refEnv.getMaxX());
                box.setMinX(refEnv.getMinX());
                box.setMaxY(refEnv.getMaxY());
                box.setMinY(refEnv.getMinY());
            } else {
                box.setMinX(boundingbox[0]);
                box.setMinY(boundingbox[1]);
                box.setMaxX(boundingbox[2]);
                box.setMaxY(boundingbox[3]);
            }
            
            mapRequest.setFormat("image/png");
            mapRequest.setVersion("1.3.3");
            mapRequest.setSRS(box.getEPSGCode());
            mapRequest.setBBox(box);
            mapRequest.setDimensions(getPreferredSize());
            String strURL = mapRequest.getFinalURL().toString().replaceAll("%'", "%25'").replaceAll(" ", "%20");
            
            System.out.println("background Image> " + strURL);
            model.setBottomImageURL(strURL);
            
            URL url = new URL(strURL);
            ImageIcon load = new ImageIcon(url);
            backgroundImage =  load.getImage();
//            model.setBottomImage(backgroundImage);
            revalidate();
            repaint();
        }catch(Exception e) { e.printStackTrace(); }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" paintComponent(Graphics g) ">
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if(Beans.isDesignTime()) return;
        
        Graphics2D g2 = (Graphics2D) g;
        if (backgroundImage != null) 
            g2.drawImage(backgroundImage, 0, 0, null); // the backgroundImage should be rendered first
            
        if (foregroundImage != null) 
            g2.drawImage(foregroundImage, 0, 0, null); // so that the foregroundImage will overlap the later.
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" MouseSupport - MouseAdapter ">
    private class MouseSupport extends MouseAdapter{
        public void mousePressed(MouseEvent e) {
            fromY = e.getPoint().getY();
            fromX = e.getPoint().getX();
            setCursor(panningCursor);
        }
        
        public void mouseReleased(MouseEvent e) {
            setCursor(defaultCursor);
            toX = e.getPoint().getX();
            toY = e.getPoint().getY();
            boolean panningRight = calculatePanningDirection(fromX, toX);
            boolean panningDown = calculatePanningDirection(fromY, toY);
            double zoomLevel = model.getExpandBy()==0 ? zoomDifference : model.getExpandBy() * zoomDifference;
            double coorResult[] = new double[2];
            coorResult[0] = fromX==toX ? 0.0 : Math.abs(fromX - toX) / zoomLevel;
            coorResult[1] = fromY==toY ? 0.0 : Math.abs(fromY - toY) / zoomLevel;
            resultXY[0] = !panningRight ? minXY[0] + coorResult[0] : minXY[0] - coorResult[0];
            resultXY[1] = panningDown ? minXY[1] + coorResult[1] : minXY[1] - coorResult[1];
            resultXY[2] = !panningRight ? maxXY[0] + coorResult[0] : maxXY[0] - coorResult[0];
            resultXY[3] = panningDown ? maxXY[1] + coorResult[1] : maxXY[1] - coorResult[1];
            getTopMap(resultXY);
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" calculatePanningDirection(double pointA, double pointB) ">
    private boolean calculatePanningDirection(double pointA, double pointB) {
        if(pointA < pointB)
            return true;
        
        return false;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" getGlobalPosition ">
    private double globeSize;
    double degreeRatio = 180d / Math.PI;;
    double radianRatio = Math.PI / 180d;;
    double halfGlobeSize;
    double pixelTileSize = 256d;
    
    private void initGlobalPosProp(int zoomLevel) {
        globeSize = pixelTileSize * Math.pow(2d, zoomLevel);
        degreeRatio = globeSize / 360d;
        radianRatio = globeSize / (2d * Math.PI);
        halfGlobeSize = globeSize / 2d;
    }
    
    private java.awt.Point FromCoordinatesToPixel(java.awt.Point point) {
        double longitude = (point.getX() - halfGlobeSize) / degreeRatio;
        double latitude = (2 * Math.atan(Math.exp((point.getY() - halfGlobeSize) / radianRatio)) - Math.PI / 2) * degreeRatio;
        
        return new java.awt.Point((int)longitude, (int)latitude);
    }
    
    private java.awt.Point FromPixelToCoordinates(java.awt.Point point) {
        double x = Math.round(halfGlobeSize + (point.getX() * degreeRatio));
        double f = Math.min( Math.max( Math.sin(point.getY() * radianRatio), -0.9999d), 0.9999d);
        double y = Math.round(halfGlobeSize + .5d * Math.log((1d + f) / (1d - f)) * radianRatio);
        
        return new java.awt.Point((int)x, (int)y);
    }
    //</editor-fold>
    
}
