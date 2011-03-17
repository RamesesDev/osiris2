package com.rameses.rcp.control;

import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.util.ValueUtil;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.beans.Beans;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
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

public class XImageViewer extends JPanel implements UIControl {
    
    private Binding binding;
    private int index;
    private boolean advanced;
    private boolean fitImage;
    private String[] depends;
    private boolean dynamic;
    
    private int width;
    private int height;
    
    private String emptyImage;
    private Icon emptyImageIcon;
    
    private BufferedImage image;
    
    private JSlider slider;
    private JCheckBox fitImgCheckBox = new JCheckBox("Fit:");
    private TitledBorder sliderBorder;
    private JScrollPane scrollPane = new JScrollPane();
    private ImageCanvas canvas  = new ImageCanvas();
    private JPanel columnHeader = new JPanel();
    private JLabel lblZoom = new JLabel("Zoom: 100%");
    
    private double zoomLevel = 1;
    private double fitPercentageWidth = 1.0;
    private double fitPercentageHeight = 1.0;
    private double scaleWidth = 1.0;
    private double scaleHeight = 1.0;
    private double scale = 1.0;
    private AffineTransform at;
    
    
    public XImageViewer() {
        super.setLayout(new BorderLayout());
        super.setBorder(BorderFactory.createEtchedBorder());
        
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setViewportView(canvas);
        
        super.add(scrollPane, BorderLayout.CENTER);
        fitImgCheckBox.setSelected( true );
        fitImgCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(fitImage)
                    fitImage = false;
                else
                    fitImage = true;
                
                repaint();
            }
        });
    }
    
    public void setLayout(LayoutManager mgr) {;}
    
    public void refresh() {
        if ( dynamic ) render();
    }
    
    public void load() {
        if ( !dynamic ) render();
    }
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    //<editor-fold defaultstate="collapsed" desc="  helper method(s)  ">
    private void attachAdvancedOptions() {
        if( advanced ) {
            //if ( columnHeader == null ) {
            columnHeader.setBorder(BorderFactory.createEtchedBorder());
            columnHeader.setLayout( new FlowLayout(FlowLayout.LEFT, 1, 1) );
            
            slider = new JSlider(10,200,100);
            //sliderBorder = BorderFactory.createTitledBorder("Zoom: " + (int)(zoomLevel * 100) + "%");
            //slider.setBorder(sliderBorder);
            slider.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    zoomLevel = (slider.getValue()/100.00);
                    width = (int) (image.getWidth(null) * zoomLevel);
                    height = (int) (image.getHeight(null) * zoomLevel);
                    //sliderBorder.setTitle("Zoom: " + (int)(zoomLevel * 100) + "%");
                    lblZoom.setText("Zoom: " + (int)(zoomLevel * 100) + "%");
                    canvas.repaint();
                    canvas.revalidate();
                }
            });
            columnHeader.add(lblZoom);
            columnHeader.add(slider);
            columnHeader.add(fitImgCheckBox);
            //}
            add(columnHeader, BorderLayout.NORTH);
        } else {
            remove(columnHeader);
        }
    }
    
    private void render() {
        try {
            Object value = null;
            
            try {
                value = UIControlUtil.getBeanValue(this);
            } catch(Exception e) {;}
            
            if( ValueUtil.isEmpty(value) ) {
                if( emptyImageIcon != null ) {
                    value = emptyImageIcon;
                } else if ( !ValueUtil.isEmpty(emptyImage) ) {
                    try {
                        value = UIControlUtil.getBeanValue(this, emptyImage);
                    } catch(Exception e) {;}
                }
            }
            
            if(value instanceof String) {
                image = ImageIO.read(new File(value.toString()));
            } else if(value instanceof byte[]) {
                image = ImageIO.read(new ByteArrayInputStream((byte[])value));
            } else if(value instanceof Image) {
                image = toBufferedImage( (Image)value );
            } else if(value instanceof ImageIcon) {
                image = toBufferedImage( ((ImageIcon)value).getImage() );
            } else if(value instanceof InputStream) {
                image = ImageIO.read((InputStream)value);
            } else if(value instanceof File) {
                image = ImageIO.read((File)value);
            } else if(value instanceof URL) {
                image = ImageIO.read((URL) value);
            }
            
            if ( image != null ) {
                width = image.getWidth(null);
                height = image.getHeight(null);
            }
            
            if(advanced == true) {
                fitImage = false;
                scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            }
            if(fitImage == true) {
                advanced = false;
                scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
            }
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    // This method returns a buffered image with the contents of an image
    private BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage)image;
        }
        
        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();
        
        // Determine if the image has transparent pixels
        boolean hasAlpha = false;
        try {
            PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
            pg.grabPixels();
            hasAlpha = pg.getColorModel().hasAlpha();
        } catch(Exception e) {;}
        
        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }
            
            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                    image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }
        
        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }
        
        // Copy image to buffered image
        Graphics g = bimage.createGraphics();
        
        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();
        
        return bimage;
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
    
    public boolean isDynamic() {
        return dynamic;
    }
    
    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="  ImageCanvas (class)  ">
    private class ImageCanvas extends JPanel {
        
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            if ( image == null ) return;
            
            if(isFitImage() == true && Beans.isDesignTime() == false) {
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
            if ( image == null ) return super.getPreferredSize();
            
            if(isFitImage() == true && Beans.isDesignTime() == false) {
                calculateFit();
                return new Dimension( image.getWidth(), image.getHeight());
            } else
                return new Dimension(width, height);
        }
        
        public void calculateFit() {
            scaleWidth = scrollPane.getViewport().getExtentSize().getWidth() / width;
            scaleHeight = scrollPane.getViewport().getExtentSize().getHeight() / height;
            scale = Math.min(scaleWidth, scaleHeight);
            fitPercentageWidth = (scrollPane.getViewport().getExtentSize().getWidth() - (scale * image.getWidth()))/2;
            fitPercentageHeight = (scrollPane.getViewport().getExtentSize().getHeight() - (scale * image.getHeight()))/2;
        }
    }
    //</editor-fold>
    
    public String getEmptyImage() {
        return emptyImage;
    }
    
    public void setEmptyImage(String emptyImageName) {
        this.emptyImage = emptyImageName;
    }
    
    public Icon getEmptyImageIcon() {
        return emptyImageIcon;
    }
    
    public void setEmptyImageIcon(Icon emptyImageIcon) {
        this.emptyImageIcon = emptyImageIcon;
    }
    
}