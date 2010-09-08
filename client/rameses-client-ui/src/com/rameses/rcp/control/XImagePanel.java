package com.rameses.rcp.control;

import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.beans.Beans;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Windhel
 */

public class XImagePanel extends JPanel implements UIControl{
    
    private BufferedImage image;
    private Binding binding;
    private int index;
    private boolean advanced;
    private boolean fitImage;
    private String[] depends;
    
    private int width;
    private int height;
    private JSlider slider;
    private JCheckBox isFit;
    private TitledBorder sliderBorder;
    private JScrollPane jsp = new JScrollPane();
    private ImageCanvas imageCanvas  = new ImageCanvas();
    private JPanel columnHeader;
    private double zoomLevel = 1;
    private double fitPercentageWidth = 1.0;
    private double fitPercentageHeight = 1.0;
    private double scaleWidth = 1.0;
    private double scaleHeight = 1.0;
    private double scale = 1.0;
    private AffineTransform at;
    
    
    public XImagePanel() {
        super.setLayout(new BorderLayout());
        super.setBorder(BorderFactory.createEtchedBorder());
        
        jsp.setBorder(BorderFactory.createEmptyBorder());
        jsp.setViewportView(imageCanvas);
        
        super.add(jsp, BorderLayout.CENTER);
    }
    
    public void setLayout(LayoutManager mgr) {;}
    
    //<editor-fold defaultstate="collapsed" desc="  attachAdvancedOptions  ">
    private void attachAdvancedOptions() {
        if( advanced ) {
            if ( columnHeader == null ) {
                columnHeader = new JPanel();
                columnHeader.setBorder(BorderFactory.createEtchedBorder());
                columnHeader.setLayout( new FlowLayout(FlowLayout.LEFT, 1, 1) );
                
                slider = new JSlider(10,200,100);
                sliderBorder = BorderFactory.createTitledBorder("Zoom: " + (int)(zoomLevel * 100) + "%");
                slider.setBorder(sliderBorder);
                slider.addChangeListener(new ChangeListener() {
                    public void stateChanged(ChangeEvent e) {
                        zoomLevel = (slider.getValue()/100.00);
                        width = (int) (image.getWidth(null) * zoomLevel);
                        height = (int) (image.getHeight(null) * zoomLevel);
                        sliderBorder.setTitle("Zoom: " + (int)(zoomLevel * 100) + "%");
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
        attachAdvancedOptions();
    }
    
    public boolean isFitImage() {
        return fitImage;
    }
    
    public void setFitImage(boolean fitImage) {
        this.fitImage = fitImage;
    }
    //</editor-fold>
    
    public void refresh() {}
    
    public void load() {
        try {
            Object value = UIControlUtil.getBeanValue(this);
            if(value instanceof String) {
                image = ImageIO.read(new File(value.toString()));
            } else if(value instanceof byte[]) {
                image = ImageIO.read(new ByteArrayInputStream((byte[])value));
            } else if(value instanceof Image) {
                image = (BufferedImage)value;
            } else if(value instanceof ImageIcon) {
                image = (BufferedImage)((ImageIcon)value).getImage();
            } else if(value instanceof InputStream) {
                image = ImageIO.read((InputStream)value);
            } else if(value instanceof File) {
                image = ImageIO.read((File)value);
            }
            
            if ( image != null ) {
                width = image.getWidth(null);
                height = image.getHeight(null);
            }
            
            if(advanced == true) {
                fitImage = false;
                jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            }
            if(fitImage == true) {
                advanced = false;
                jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
            }
        } catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="  ImageCanvas (class)  ">
    private class ImageCanvas extends JPanel {
        
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            if ( image == null ) return;
            
            if( isFitImage() && !Beans.isDesignTime() ) {
                calculateFit();
                Graphics2D g2 = (Graphics2D)g.create();
                at = AffineTransform.getTranslateInstance(fitPercentageWidth, fitPercentageHeight);
                at.scale(scale, scale);
                g2.drawRenderedImage(image, at);
                g2.dispose();
            } else {
                g.drawImage(image, 0, 0, width, height, null);
                g.dispose();
            }
        }
        
        public Dimension getPreferredSize() {
            if ( image == null ) {
                return super.getPreferredSize();
            } else if( isFitImage() && !Beans.isDesignTime() ) {
                calculateFit();
                return new Dimension( image.getWidth(), image.getHeight());
            } else {
                return new Dimension(width, height);
            }
        }
        
        public void calculateFit() {
            scaleWidth = jsp.getViewport().getExtentSize().getWidth() / width;
            scaleHeight = jsp.getViewport().getExtentSize().getHeight() / height;
            scale = Math.min(scaleWidth, scaleHeight);
            fitPercentageWidth = (jsp.getViewport().getExtentSize().getWidth() - (scale * image.getWidth()))/2;
            fitPercentageHeight = (jsp.getViewport().getExtentSize().getHeight() - (scale * image.getHeight()))/2;
        }
        //</editor-fold>
    }
}