/*
 * XChart.java
 *
 * Created on February 17, 2011, 2:26 PM
 * @author jaycverg
 */

package com.rameses.bi.control;

import com.rameses.bi.common.AbstractChartHandler;
import com.rameses.common.PropertyResolver;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.beans.Beans;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import org.jfree.chart.JFreeChart;


public class XChart extends JLabel implements UIControl {
    
    private Binding binding;
    private String[] depends;
    private int index;
    private String handler;
    private boolean dynamic;
    
    private AbstractChartHandler chartHandler;
    private JFreeChart chart;
    
    
    public XChart() {
        setVerticalAlignment(SwingConstants.TOP);
        
        if( Beans.isDesignTime() ) {
            setPreferredSize(new Dimension(50, 50));
            setOpaque(true);
            setBackground(Color.LIGHT_GRAY);
        } else {
            setIcon( new ChartIcon() );
        }
    }
    
    public void setLayout(LayoutManager mgr) {;}
        
    public void refresh() {
        if( chartHandler != null && dynamic ) {
            chart = chartHandler.createChart();
            super.repaint();
        }
        
    }
    
    public void load() {
        super.setText("");
        
        try {
            PropertyResolver resolver = ClientContext.getCurrentContext().getPropertyResolver();
            Object value = UIControlUtil.getBeanValue(this, handler);
            if( value instanceof AbstractChartHandler ) {
                chartHandler = (AbstractChartHandler) value;
            }
            
            if( !dynamic ) {
                chart = chartHandler.createChart();
            }
            
        } catch(Exception e) {}
    }
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    //<editor-fold defaultstate="collapsed" desc="  getters/setters  ">
    public void setName(String name) {
        super.setText(name);
        super.setName(name);
    }
    
    public void setText(String text) {}
    
    public String[] getDepends() {
        return depends;
    }
    
    public void setDepends(String[] depends) {
        this.depends = depends;
    }
    
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    
    public Binding getBinding() {
        return binding;
    }
    
    public void setBinding(Binding binding) {
        this.binding = binding;
    }
    
    public String getHandler() {
        return handler;
    }
    
    public void setHandler(String handler) {
        this.handler = handler;
    }
    
    public boolean isDynamic() {
        return dynamic;
    }
    
    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="  ChartIcon (class)  ">
    private class ChartIcon implements Icon {
        
        public void paintIcon(Component comp, Graphics g, int x, int y) {
            JLabel c = (JLabel) comp;
            int w=0, h=0;
            Dimension d = c.getSize();
            w = d.width;
            h = d.height;
            
            Insets in = c.getInsets();
            if( in != null ) {
                w -= in.left + in.right;
                h -= in.top + in.bottom;
            }
            Border b = c.getBorder();
            if( b != null && (in = b.getBorderInsets(c)) != null ) {
                w -= in.left + in.right;
                h -= in.top + in.bottom;
            }
            
            if( w <= 0 || h <= 0 ) return;
            
            if( chart != null ) {
                BufferedImage img = chart.createBufferedImage(w, h);
                
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.drawImage(img, x, y, XChart.this);
                g2.dispose();
            }
        }
        
        public int getIconWidth() { return 0; }
        public int getIconHeight() { return 0;}

    }
    //</editor-fold>
    
}
