
package com.rameses.rcp.barcode;

import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.util.ValueUtil;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author Windhel
 */
public class BarcodeViewer extends JPanel implements UIControl {
    
    private Image barcode = null;
    
    private String[] depends;
    private Binding binding;
    private int index;
    
    private BarcodeModel model;
    
    public BarcodeViewer() {}
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(barcode, 0, 0, null);
        g.dispose();
    }
    
    //<editor-fold defaultstate="collapsed" desc=" Setter/Getter ">
    public String[] getDepends() { return depends; }
    public void setDepends(String[] depends) { this.depends = depends; }
    
    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }
    
    public void setBinding(Binding binding) { this.binding = binding; }
    public Binding getBinding() { return binding; }
    
    public int compareTo(Object o) { return UIControlUtil.compare(this, o); }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" helper methods">
    public void refresh() {
    }
    
    public void load() {
        try {
            Object mdl = UIControlUtil.getBeanValue(this);
            if(ValueUtil.isEmpty(mdl)) throw new Exception("No model was found.:BarcodeModel");
            if(mdl instanceof BarcodeModel)
                model = (BarcodeModel) mdl;
            
            ImageIcon load = new ImageIcon(model.generateURL(null));
            barcode = load.getImage();
            
            repaint();
        }catch(Exception ex) { ex.printStackTrace(); }
    }
    
    public void encodeData(String data) {
        
    }
    //</editor-fold>
}
