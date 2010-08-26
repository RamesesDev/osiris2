package com.rameses.rcp.control;

import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Windhel
 */

public class XImagePanel extends JPanel implements UIControl{
    
    private Image image;
    private Binding binding;
    private int index;
    private boolean advanced;
    private String[] depends;
    
    private int zoomLevel = 1;
    private int width;
    private int height;
    private JSlider slider;
    private TitledBorder sliderBorder;
    private ImageCanvas imageCanvas;
    private JPanel columnHeader;
    
    
    public XImagePanel() {
        super.setLayout(new BorderLayout());
        super.setBorder(BorderFactory.createEtchedBorder());
        
        imageCanvas = new ImageCanvas();
        JScrollPane jsp = new JScrollPane();
        jsp.setBorder(BorderFactory.createEmptyBorder());
        jsp.setViewportView(imageCanvas);
        
        super.add(jsp, BorderLayout.CENTER);
    }
    
    public void setLayout(LayoutManager mgr) {;}
    
    //<editor-fold defaultstate="collapsed" desc="  attachZoom  ">
    private void attachZoom() {
        if( advanced ) {
            if ( columnHeader == null ) {
                columnHeader = new JPanel();
                columnHeader.setBorder(BorderFactory.createEtchedBorder());
                columnHeader.setLayout( new FlowLayout(FlowLayout.LEFT, 1, 1) );
                
                slider = new JSlider(10,200,100);
                sliderBorder = BorderFactory.createTitledBorder("Zoom: " + (zoomLevel * 100) + "%");
                slider.setBorder(sliderBorder);
                slider.addChangeListener(new ChangeListener() {
                    public void stateChanged(ChangeEvent e) {
                        
                        zoomLevel = (int) (slider.getValue()/100.00);
                        width = (int) (image.getWidth(null) * zoomLevel);
                        height = (int) (image.getHeight(null) * zoomLevel);
                        sliderBorder.setTitle("Zoom: " + (zoomLevel * 100) + "%");
                        imageCanvas.repaint();
                        imageCanvas.revalidate();
                        
                    }
                });
                columnHeader.add(slider);
            }
            add(columnHeader, BorderLayout.NORTH);
        } else {
            remove(columnHeader);
        }
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
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
    
    public void setBinding(Binding binding) {
        this.binding = binding;
    }
    
    public Binding getBinding() {
        return binding;
    }
    
    public boolean isAdvanced() {
        return advanced;
    }
    
    public void setAdvanced(boolean advanced) {
        this.advanced = advanced;
        attachZoom();
    }
    //</editor-fold>
    
    public void refresh() {
        Object value = UIControlUtil.getBeanValue(this);
        try {
            if(value instanceof String) {
                image = ImageIO.read(new File(value.toString()));
            } else if(value instanceof byte[]) {
                image = ImageIO.read(new ByteArrayInputStream((byte[])value));
            } else if(value instanceof Image) {
                image = (Image)value;
            } else if(value instanceof ImageIcon) {
                image = ((ImageIcon)value).getImage();
            } else if(value instanceof InputStream) {
                image = ImageIO.read((InputStream)value);
            } else if(value instanceof File) {
                image = ImageIO.read((File)value);
            }
            
            if ( image != null ) {
                width = image.getWidth(null);
                height = image.getHeight(null);
            }
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void load() {}
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="  ImageCanvas (class)  ">
    private class ImageCanvas extends JPanel {
        
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if(image != null)
                g.drawImage(image, 0, 0, width, height, null);
        }
        
        public Dimension getPreferredSize() {
            return new Dimension(width, height);
        }
    }
    //</editor-fold>
}
