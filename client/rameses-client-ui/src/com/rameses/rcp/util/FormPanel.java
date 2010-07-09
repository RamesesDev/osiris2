package com.rameses.rcp.util;
import com.rameses.rcp.ui.Containable;
import com.rameses.rcp.ui.ControlProperty;
import com.rameses.rcp.ui.Validatable;
import com.rameses.util.ValueUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class FormPanel extends JPanel {
    
    private int cellspacing = 3;
    private int captionWidth;
    
    // (NUM-PAD ARRANGEMENT) 2-top  5-center  8-bottom
    private int captionVAlignment = 2;
    private Color errorCaptionColor = Color.RED;
    
    public FormPanel() {
        super.setLayout(new Layout());
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        setOpaque(false);
    }
    
    //<editor-fold defaultstate="collapsed" desc=" FormPanel implementations ">
    public void setLayout(LayoutManager mgr) {;}
    
    public int getCellspacing() { return cellspacing; }
    public void setCellspacing(int cellspacing) { this.cellspacing = cellspacing; }
    
    public int getCaptionWidth() { return captionWidth; }
    public void setCaptionWidth(int captionWidth) { this.captionWidth = captionWidth; }
    
    public int getCaptionVAlignment() { return captionVAlignment; }
    public void setCaptionVAlignment(int captionVAlignment) { this.captionVAlignment = captionVAlignment; }
    
    public Color getErrorCaptionColor() {
        return errorCaptionColor;
    }
    
    public void setErrorCaptionColor(Color color) {
        this.errorCaptionColor = color;
    }
    
    protected void addImpl(Component comp, Object constraints, int index) {
        //check if it is a containable component
        if ( comp instanceof Containable ) {
            ItemPanel p = new ItemPanel(comp);
            super.addImpl(p, constraints, index);
        }
    }
    
    public void remove(Component comp) {
        if (comp instanceof ItemPanel) {
            super.remove(comp);
        } else {
            Component c = resolveComponent(comp);
            if (c != null) super.remove(c);
        }
    }
    
    private int indexOf(Component comp) {
        for (int i=0; i<getComponentCount(); i++) {
            Component c = getComponent(i);
            if (c == comp) return i;
            
            if (c instanceof ItemPanel) {
                ItemPanel p = (ItemPanel) c;
                if (p.getEditorComponent() == comp) return i;
            }
        }
        return -1;
    }
    
    private Component resolveComponent(Component comp) {
        for (int i=0; i<getComponentCount(); i++) {
            Component c = getComponent(i);
            if (c == comp) return c;
            
            if (c instanceof ItemPanel) {
                ItemPanel p = (ItemPanel) c;
                if (p.getEditorComponent() == comp) return p;
            }
        }
        return null;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" ItemPanel (Class) ">
    private class ItemPanel extends JPanel {
        
        private Component editor;
        private JLabel label;
        private ControlProperty property;
        
        public ItemPanel(Component editor) {
            this.editor = editor;
            Containable con = (Containable) editor;
            property = con.getControlProperty();
            
            label = new JLabel();
            label.setBorder(BorderFactory.createEmptyBorder(3,3,3,2));
            
            setOpaque(false);
            setLayout(new ItemPanelLayout(property));
            add(label, "label");
            add(editor, "editor");
            
            String caption = property.getCaption();
            boolean req = property.isRequired();
            label.setText(getCaption(req, caption));
            
            PropertyChangeListener listener = new ContainablePropetyListener(this);
            property.addPropertyChangeListener(listener);
        }
        
        public boolean match(Component editor) {
            if (editor == null) return false;
            
            return (this.editor == editor);
        }
        
        public Component getEditorComponent() { return editor; }
        public JLabel getLabelComponent() { return label; }
        public ControlProperty getControlProperty() { return property; }
        
        public String getCaption(boolean required, String caption) {
            return getCaption(required, caption, false);
        }
        
        public String getCaption(boolean required, String caption, boolean error) {
            required = required && editor instanceof Validatable;
            
            StringBuffer sb = new StringBuffer("<html>");
            String color = getCaptionColor();
            
            if( error ) {
                sb.append("<font color=\"" + color + "\">" + caption + "</font>");
            } else {
                sb.append(caption);
            }
            sb.append(" : ");
            if(required) {
                sb.append("<font color=\"" + color + "\">*</font></html>");
            }
            return sb.toString();
        }
        
        private String getCaptionColor() {
            StringBuffer color = new StringBuffer("rgb(");
            color.append( errorCaptionColor.getRed() + ",");
            color.append( errorCaptionColor.getGreen() + ",");
            color.append( errorCaptionColor.getBlue());
            color.append(")");
            return color.toString();
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" ContainablePropetyListener (Class) ">
    private class ContainablePropetyListener implements PropertyChangeListener {
        
        private ItemPanel panel;
        
        public ContainablePropetyListener(ItemPanel panel) {
            this.panel = panel;
        }
        
        public void propertyChange(PropertyChangeEvent evt) {
            String propName = evt.getPropertyName();
            Object value = evt.getNewValue();
            
            JComponent editor = (JComponent) panel.getEditorComponent();
            JLabel label = panel.getLabelComponent();
            ControlProperty prop = panel.getControlProperty();;
            
            if ( "caption".equals(propName) ) {
                label.setText( panel.getCaption(prop.isRequired(), value.toString()));
            } else if ( "required".equals(propName) ) {
                boolean req = Boolean.parseBoolean(value.toString());
                label.setText( panel.getCaption( req, prop.getCaption()) );
            } else if ( "captionWidth".equals(propName) ) {
                panel.revalidate();
            } else if ( "showCaption".equals(propName)) {
                panel.revalidate();
            } else if ( "errorMessage".equals(propName) ) {
                String message = value != null? value.toString() : null;
                boolean error = !ValueUtil.isEmpty(message);
                label.setText( panel.getCaption(prop.isRequired(), prop.getCaption(), error) );
                label.setToolTipText(message);
                editor.setToolTipText(message);
            }
        }
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" ItemPanelLayout (Class) ">
    private class ItemPanelLayout implements LayoutManager {
        
        private Component label;
        private Component editor;
        private ControlProperty property;
        
        public ItemPanelLayout(ControlProperty property) {
            this.property = property;
        }
        
        public void addLayoutComponent(String name, Component comp) {
            if ("label".equals(name)) label = comp;
            else if ("editor".equals(name)) editor = comp;
        }
        
        public void removeLayoutComponent(Component comp) {
            if (comp == null) ;
            else if (label == comp) label = comp;
            else if (editor == comp) editor = comp;
        }
        
        public Dimension preferredLayoutSize(Container parent) {
            return getLayoutSize(parent);
        }
        
        public Dimension minimumLayoutSize(Container parent) {
            Dimension dim = getLayoutSize(parent);
            return new Dimension(100, dim.height);
        }
        
        public void layoutContainer(Container parent) {
            synchronized (parent.getTreeLock()) {
                Insets margin = parent.getInsets();
                int x = margin.left;
                int y = margin.top;
                int w = parent.getWidth() - (margin.left + margin.right);
                int h = parent.getHeight() - (margin.top + margin.bottom);
                int captionWidth = getPreferredCaptionWidth();
                boolean captionHidden = !isShowCaption();
                
                if (label != null && !captionHidden) {
                    int cw = captionWidth;
                    Dimension dim = label.getPreferredSize();
                    if (cw <= 0) cw = dim.width;
                    
                    label.setBounds(x, y, cw, h);
                    x += cw;
                    w -= cw;
                }
                
                if (editor != null) {
                    Dimension dim = editor.getPreferredSize();
                    int cw = dim.width;
                    if ( cw > w || cw <= 0 ) {
                        cw = w;
                    }
                    editor.setBounds(x, y, cw, h);
                }
            }
        }
        
        public Dimension getLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                int w=0, h=0;
                int captionWidth = getPreferredCaptionWidth();
                boolean captionHidden = !isShowCaption();
                
                if (label != null) {
                    if ( !captionHidden ) {
                        applyCaptionStyles((JLabel) label);
                        
                        int cw = captionWidth;
                        Dimension dim = label.getPreferredSize();
                        if (cw <= 0) cw = dim.width;
                        
                        w += cw;
                        h = Math.max(h, dim.height);
                        label.setVisible(true);
                    } else {
                        label.setVisible(false);
                    }
                }
                
                if (editor != null) {
                    Dimension dim = editor.getPreferredSize();
                    w += dim.width;
                    h = Math.max(h, dim.height);
                }
                
                Insets margin = parent.getInsets();
                w += (margin.left + margin.right);
                h += (margin.top + margin.bottom);
                return new Dimension(w, h);
            }
        }
        
        private void applyCaptionStyles(JLabel label) {
            int valign = getCaptionVAlignment();
            if (valign == 5) label.setVerticalAlignment(SwingConstants.CENTER);
            else if (valign == 8) label.setVerticalAlignment(SwingConstants.BOTTOM);
            else label.setVerticalAlignment(SwingConstants.TOP);
        }
        
        private int getPreferredCaptionWidth() {
            return property.getCaptionWidth() <= 0? getCaptionWidth(): property.getCaptionWidth();
        }
        
        private boolean isShowCaption() {
            return property.isShowCaption();
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" Layout (Class) ">
    private class Layout implements LayoutManager {
        
        public void addLayoutComponent(String name, Component comp) {;}
        public void removeLayoutComponent(Component comp) {;}
        
        public Dimension preferredLayoutSize(Container parent) {
            return getLayoutSize(parent);
        }
        
        public Dimension minimumLayoutSize(Container parent) {
            Dimension dim = getLayoutSize(parent);
            return new Dimension(100, dim.height);
        }
        
        public void layoutContainer(Container parent) {
            synchronized (parent.getTreeLock()) {
                Insets margin = parent.getInsets();
                int x = margin.left;
                int y = margin.top;
                int w = parent.getWidth() - (margin.left + margin.right);
                int h = parent.getHeight() - (margin.top + margin.bottom);
                
                Component[] comps = parent.getComponents();
                for (int i=0; i<comps.length; i++) {
                    Component c = comps[i];
                    Dimension dim = c.getPreferredSize();
                    
                    //add cellspacing
                    if (y > 0) y += getCellspacing();
                    
                    c.setBounds(x, y, w, dim.height);
                    y += dim.height;
                }
            }
        }
        
        public Dimension getLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                int w=0, h=0;
                
                Component[] comps = parent.getComponents();
                for (int i=0; i<comps.length; i++) {
                    Component c = comps[i];
                    if (c instanceof ItemPanel) {
                        Dimension dim = c.getPreferredSize();
                        if (w == 0) w = dim.width;
                        
                        w = Math.min(w, dim.width);
                        
                        //add cellspacing
                        if (h > 0) h += getCellspacing();
                        
                        h += dim.height;
                    }
                }
                
                Insets margin = parent.getInsets();
                w += (margin.left + margin.right);
                h += (margin.top + margin.bottom);
                return new Dimension(w, h);
            }
        }
    }
    //</editor-fold>
    
}
