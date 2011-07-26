
package com.rameses.rcp.control;

import com.rameses.rcp.control.camera.CameraModel;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import javax.media.Buffer;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.media.CaptureDeviceInfo;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.control.FrameGrabbingControl;
import javax.media.format.VideoFormat;
import javax.media.protocol.DataSource;
import javax.media.util.BufferToImage;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Windhel
 */
public class XCameraPanel extends JPanel implements UIControl{
    
    private JDialog popUpPane = new JDialog();
    private ImageCanvas outputImageCanvas = new ImageCanvas();
    private CropCanvas cropCanvas = new CropCanvas();
    private JPanel cropControls = new JPanel();
    private JButton openPopUpPane = new JButton("Take Picture");
    private JButton takeImage = new JButton("take");
    private JButton cropImage = new JButton("Crop");
    private JSlider zoomSlider = new JSlider(0, 200, 100);
    private JSlider pictureDimensionSlider = new JSlider(1,2,1);
    
    private Player player;
    private CaptureDeviceInfo captureDeviceInfo;
    private MediaLocator cameraLocator;
    private DataSource dataSource;
    private Component componentPlayer;
    
    private CameraModel model;
    private FrameGrabbingControl fgc;
    private Buffer buf;
    private Image image;
    
    //for crop
    private double zoomLevel = 1;
    private int width;
    private int height;
    private int mousePosX;
    private int mousePosY;
    private int xHalfPOS;
    private int xHalfNEG;
    private int yHalfPOS;
    private int yHalfNEG;
    private int outputDimension = 1;
    private int screenResolution = Toolkit.getDefaultToolkit().getScreenResolution();
    private int dim;
    private int imageWidth = 0;
    private int imageHeight = 0;
    
    private Binding binding;
    private String[] depends;
    private int index;
    
    private Dimension dimension = new Dimension(320,240);
    private Dimension outDim = new Dimension(screenResolution * outputDimension, screenResolution * outputDimension);
    
    private boolean showAdvancedOptions = false;
    
    public XCameraPanel() {
        buildComponent();
    }
    
    
    //<editor-fold defaultstate="collapsed" desc=" buildComponent ">
    private void buildComponent() {
        
        JPanel zoom = new JPanel();
        final JLabel lblZoom = new JLabel("Zoom: " + (zoomLevel * 100) + "%");
        zoom.add(lblZoom);
        zoom.add(zoomSlider);
        zoomSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                zoomLevel = zoomSlider.getValue()/100.00;
                lblZoom.setText("Zoom: " + (zoomLevel * 100) + "%");
                cropCanvas.revalidate();
                cropCanvas.repaint();
            }
        });
        
        popUpPane.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        openPopUpPane.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connectToDevice();
                popUpPane.add(player.getVisualComponent(), BorderLayout.CENTER);
                popUpPane.add(takeImage, BorderLayout.SOUTH);
                player.start();
                
                mousePosX = (int)(dimension.width/2);
                mousePosY = (int)(dimension.height/2);
                
                popUpPane.setVisible(true);
            }
        });
        
        takeImage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    System.out.println("taking picture......");
                    image = takeScreenShot();
                    //outputImageCanvas.setImage(image);
                    model.setImage(image);
                    cropCanvas.setImage(image);
                    popUpPane.remove(player.getVisualComponent());
                    popUpPane.remove(takeImage);
                    
                    mousePosX = (int)(dimension.width/2);
                    mousePosY = (int)(dimension.height/2);
                    
                    popUpPane.add(cropCanvas, BorderLayout.CENTER);
                    popUpPane.add(cropControls, BorderLayout.SOUTH);
                    SwingUtilities.updateComponentTreeUI(popUpPane);
                }catch(Exception ex) { ex.printStackTrace(); }
            }
        });
        
        cropImage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Image result = cropImage(model.getImage(), zoomLevel, dim, getOutputDimension());
                outputImageCanvas.setImage(result);
                model.setImage(result);
                popUpPane.remove(cropCanvas);
                popUpPane.remove(cropControls);
                popUpPane.setVisible(false);
            }
        });
        final JLabel lblPicDim = new JLabel();
        lblPicDim.setText("Dimension: " + outputDimension + "X" + outputDimension);
        pictureDimensionSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                outputDimension = pictureDimensionSlider.getValue();
                lblPicDim.setText("Dimension: " + outputDimension + "X" + outputDimension);
                cropCanvas.revalidate();
                cropCanvas.repaint();
            }
        });
        
        popUpPane.addWindowListener(new WindowAdapter() {
            public void windowDeactivated(WindowEvent e) {
                popUpPane.remove(cropCanvas);
                popUpPane.remove(cropControls);
                player.stop();
                dataSource.disconnect();
                System.out.println("device deactivated");
            }
        });
        if(isShowAdvancedOptions()) {
            cropControls.setLayout(new GridLayout(3,2));
            cropControls.add(lblZoom);
            cropControls.add(zoomSlider);
            cropControls.add(lblPicDim);
            cropControls.add(pictureDimensionSlider);
            cropControls.add(cropImage);
        } else {
            cropControls.setLayout(new GridLayout(1,1));
            cropControls.add(cropImage);
        }
        
        popUpPane.setBounds(mousePosX - (dim/2),mousePosY- (dim/2),dimension.width + 5, dimension.height + (int)cropControls.getPreferredSize().getHeight() + 5);
        //popUpPane.setResizable(false);
        popUpPane.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        popUpPane.setLayout(new BorderLayout());
        
        setLayout(new BorderLayout());
        add(outputImageCanvas, BorderLayout.CENTER);
        add(openPopUpPane, BorderLayout.NORTH);
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc=" cropImage(Image image, double zoomLevel, int dimension, int outputDimension) ">
    public Image cropImage(Image image, double zoomLevel, int dimension, int inches) {
        BufferedImage img = new BufferedImage(cropCanvas.getWidth(), cropCanvas.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        //g.drawImage(image, 0, 0, (int)(image.getWidth(null) * zoomLevel), (int)(image.getHeight(null) * zoomLevel), null);
        g.drawImage(image, 0, 0, cropCanvas.getWidth(), cropCanvas.getHeight(), null);
        
        img = img.getSubimage(xHalfNEG, yHalfNEG, dimension, dimension);
        
        model.setImage(img.getScaledInstance(dimension, dimension, 0));
        
        return img.getScaledInstance(dimension, dimension, 0);
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc=" connectToDevice() ">
    private void connectToDevice() {
        try {
            dataSource = null;
            player = null;
            MediaLocator ml = new MediaLocator("vfw:Microsoft WDM Image Capture (Win32):0");
            dataSource = Manager.createDataSource(ml);
            player = Manager.createRealizedPlayer(dataSource);
        }catch( Exception e ) {
            System.out.println("cannot detect any device registered from JMF.");
            e.printStackTrace();
        }
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc=" takeScreenShot ">
    public Image  takeScreenShot() throws Exception{
        fgc = (FrameGrabbingControl)player.getControl("javax.media.control.FrameGrabbingControl");
        buf = fgc.grabFrame();
        
        model.setImage(new BufferToImage((VideoFormat)buf.getFormat()).createImage(buf));
        
        return new BufferToImage((VideoFormat)buf.getFormat()).createImage(buf);
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc=" ImageCanvas ">
    private class ImageCanvas extends JPanel {
        
        private Image image;
        
        public ImageCanvas() {}
        
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            outDim = new Dimension(screenResolution * getOutputDimension(), screenResolution * getOutputDimension());
            g.drawImage(image, 0, 0, (int)outDim.getWidth(), (int)outDim.getHeight(), null);
            g.dispose();
        }
        
        public Image getImage() { return image;}
        public void setImage(Image image) {
            this.image = image;
            repaint();
        }
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc=" CropCanvas ">
    private class CropCanvas extends JPanel {
        
        private Image image;
        
        public CropCanvas(){
            addMouseMotionListener(new MouseSupport());
        }
        
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            
            if(getWidth() >= dim && getHeight() >= dim) {
                dim = screenResolution * getOutputDimension();
                xHalfPOS = mousePosX + (dim / 2);
                if(xHalfPOS < dim)
                    xHalfPOS = dim;
                xHalfNEG = mousePosX - (dim / 2);
                if(xHalfNEG < 0)
                    xHalfNEG = 0;
                yHalfPOS = mousePosY + (dim / 2);
                if(yHalfPOS < dim)
                    yHalfPOS = dim;
                yHalfNEG = mousePosY - (dim / 2);
                if(yHalfNEG < 0)
                    yHalfNEG = 0;
                //uncomment to support zoom
                //uncomment all uncomment and replace existing same like lines
//                xHalfPOS = (imageWidth/2) + (dim / 2);
//                xHalfNEG = (imageWidth/2) - (dim / 2);
//                yHalfPOS = (imageHeight/2) + (dim / 2);
//                yHalfNEG =(imageHeight/2) - (dim / 2);
//
//                g2.drawImage( image, 0, 0, (int)(imageWidth * zoomLevel), (int)(imageHeight * zoomLevel), null);
//                //topmost
//                g2.fillRect(0, 0, (int)(imageWidth * zoomLevel) , yHalfNEG);
//                //left
//                g2.fillRect(0, yHalfNEG, xHalfNEG , dim);
//                //bottom
//                g2.fillRect(0, yHalfPOS , (int)(imageWidth * zoomLevel), (int)(imageHeight * zoomLevel));
//                //rights
//                g2.fillRect(xHalfPOS, yHalfNEG, (int)(imageWidth * zoomLevel), dim);
                
                g2.drawImage( image, 0, 0, getWidth(), getHeight(), null);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.75f));
                g2.setColor(Color.WHITE);
                
                //topmost
                g2.fillRect(0, 0, getWidth() , yHalfNEG);
                //left
                g2.fillRect(0, yHalfNEG, xHalfNEG , dim);
                //bottom
                g2.fillRect(0, yHalfPOS , getWidth(), getHeight());
                //rights
                g2.fillRect(xHalfPOS, yHalfNEG, getWidth(), dim);
            } else {
                g2.setColor(Color.RED);
                g2.drawString("Image is smaller than the " + outputDimension + "x" + outputDimension + " picture size.",20,20);
            }
            g2.dispose();
        }
        
        public Image getImage() { return image; }
        public void setImage(Image image) {
            this.image = image;
            imageWidth = image.getWidth(null);
            imageHeight = image.getHeight(null);
        }
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc=" MouseSupport ">
    public class MouseSupport extends MouseMotionAdapter {
        public void mouseDragged(MouseEvent e) {
            //supports zoom
//            if(e.getX() >= (int)(xHalfPOS - xHalfNEG)/2)
//                if(e.getX() <= (int)((image.getWidth(null) * zoomLevel) - (xHalfPOS - xHalfNEG)/2))
//                    mousePosX = e.getX();
//
//            if(e.getY() >= (int)(yHalfPOS - yHalfNEG)/2)
//                if(e.getY() <= (int)((image.getHeight(null) * zoomLevel) - (yHalfPOS - yHalfNEG)/2))
//                    mousePosY = e.getY();
            
            if(e.getX() >= (int)(xHalfPOS - xHalfNEG)/2)
                if(e.getX() <= (int)((cropCanvas.getWidth()) - (xHalfPOS - xHalfNEG)/2))
                    mousePosX = e.getX();
            
            if(e.getY() >= (int)(yHalfPOS - yHalfNEG)/2)
                if(e.getY() <= (int)(cropCanvas.getHeight()  - ((yHalfPOS - yHalfNEG)/2)))
                    mousePosY = e.getY();
            
            cropCanvas.repaint();
        }
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc=" Getters/Setters ">
    public String[] getDepends() { return depends; }
    public void setDepends(String[] depends) { this.depends = depends; }
    
    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }
    
    public Binding getBinding() { return binding; }
    public void setBinding(Binding binding) { this.binding = binding; }
    
    public int compareTo(Object o) { return UIControlUtil.compare(this, o); }
    
    public boolean isShowAdvancedOptions() { return showAdvancedOptions; }
    public void setShowAdvancedOptions(boolean showAdvancedOptions) { this.showAdvancedOptions = showAdvancedOptions; }
    
    public Dimension getDimension() { return dimension; }
    public void setDimension(Dimension dimension) { this.dimension = dimension; }
    
    public int getOutputDimension() { return outputDimension; }
    public void setOutputDimension(int outputDimension) { this.outputDimension = outputDimension; }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc=" Load / Refresh ">
    public void refresh() {
    }
    
    public void load() {
        Object value = null;
        try {
            value = UIControlUtil.getBeanValue(this);
            if(value instanceof CameraModel)
                model = (CameraModel) value;
        } catch(Exception e) {;}
    }
    //</editor-fold>
}
