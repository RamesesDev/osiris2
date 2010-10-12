package com.rameses.rcp.util;

import com.rameses.rcp.constant.UIConstants;
import com.rameses.rcp.control.XLabel;
import com.rameses.rcp.ui.ActiveControl;
import com.rameses.rcp.ui.ControlProperty;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.beans.Beans;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class FormPanel extends JPanel implements ActiveControl, UIConstants {
    
    private int cellspacing = 3;
    private Insets cellpadding = new Insets(0,0,0,0);
    private String orientation = UIConstants.VERTICAL;
    
    private Insets padding;
    private Border origBorder;
    
    private int captionWidth = 80;
    private String captionVAlignment = UIConstants.TOP;
    private String captionHAlignment = UIConstants.LEFT;
    private String captionOrientation = UIConstants.LEFT;
    private Insets captionPadding = new Insets(0,0,0,5);
    private boolean addCaptionColon = true;
    
    private ControlProperty property = new ControlProperty();
    
    public FormPanel() {
        super.setLayout(new Layout());
        setPadding(new Insets(5,5,5,5));
        setOpaque(false);
    }
    
    //<editor-fold defaultstate="collapsed" desc=" FormPanel implementations ">
    public void setLayout(LayoutManager mgr) {;}
    
    protected void addImpl(Component comp, Object constraints, int index) {
        ItemPanel p = null;
        //check if it is a containable component
        if ( comp instanceof ActiveControl ) {
            p = new ItemPanel(comp);
        } else if ( comp instanceof JScrollPane ) {
            Component view = ((JScrollPane) comp).getViewport().getView();
            if ( view instanceof ActiveControl ) {
                p = new ItemPanel(view, comp);
            }
        }
        
        if ( p != null )
            super.addImpl(p, constraints, index);
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
                if (p.getEditorWrapper() == comp) return p;
            }
        }
        return null;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public ControlProperty getControlProperty() {
        return property;
    }
    
    public String getCaption() { return property.getCaption(); }
    public void setCaption(String caption) { property.setCaption(caption); }
    
    public char getCaptionMnemonic() { return property.getCaptionMnemonic(); }
    public void setCaptionMnemonic(char c) { property.setCaptionMnemonic(c); }
    
    public boolean isShowCaption() { return property.isShowCaption(); }
    public void setShowCaption(boolean show) { property.setShowCaption(show); }
    
    public int getCellspacing() { return cellspacing; }
    public void setCellspacing(int cellspacing) { this.cellspacing = cellspacing; }
    
    public void setBorder(Border border) {
        this.origBorder = border;
        if ( padding != null ) {
            Border inner = BorderFactory.createEmptyBorder(padding.top, padding.left, padding.bottom, padding.right);
            super.setBorder(BorderFactory.createCompoundBorder(border, inner));
        } else {
            super.setBorder(border);
        }
    }
    
    public Insets getPadding() { return padding; }
    public void setPadding(Insets padding) {
        this.padding = padding;
        setBorder(origBorder);
    }
    
    public int getCaptionWidth() { return captionWidth; }
    public void setCaptionWidth(int captionWidth) { this.captionWidth = captionWidth; }
    
    public String getCaptionVAlignment() { return captionVAlignment; }
    public void setCaptionVAlignment(String captionVAlignment) {
        if ( captionVAlignment != null )
            this.captionVAlignment = captionVAlignment.toUpperCase();
        else
            this.captionVAlignment = UIConstants.TOP;
    }
    
    public String getCaptionHAlignment() { return captionHAlignment; }
    public void setCaptionHAlignment(String captionHAlignment) {
        if ( captionHAlignment != null )
            this.captionHAlignment = captionHAlignment.toUpperCase();
        else
            this.captionHAlignment = UIConstants.LEFT;
    }
    
    public String getCaptionOrientation() { return captionOrientation; }
    public void setCaptionOrientation(String captionOrientation) {
        if ( captionOrientation != null )
            this.captionOrientation = captionOrientation.toUpperCase();
        else
            this.captionOrientation = UIConstants.LEFT;
    }
    
    public String getOrientation() { return orientation; }
    public void setOrientation(String orientation) {
        if ( orientation != null )
            this.orientation = orientation.toUpperCase();
        else
            this.orientation = UIConstants.VERTICAL;
    }
    
    public Insets getCaptionPadding() { return captionPadding; }
    public void setCaptionPadding(Insets captionPadding) { this.captionPadding = captionPadding; }
    
    public Insets getCellpadding() { return cellpadding; }
    public void setCellpadding(Insets cellpadding) {
        if ( cellpadding != null )
            this.cellpadding = cellpadding;
        else
            this.cellpadding = new Insets(0,0,0,0);
    }
    
    public boolean isAddCaptionColon() { return addCaptionColon; }
    public void setAddCaptionColon(boolean addCaptionColon) { 
        this.addCaptionColon = addCaptionColon;
        updateLabels();
    }
    
    private void updateLabels() {
        for(Component c: getComponents()) {
            if ( c instanceof ItemPanel ) {
                XLabel lbl = ((ItemPanel)c).getLabelComponent();
                lbl.setAddCaptionColon(addCaptionColon);
            }
        }
        revalidate();
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" ItemPanel (Class) ">
    private class ItemPanel extends JPanel {
        
        /**
         * wrapper is usually JScrollPane
         */
        private Component editorWrapper;
        private Component editor;
        private XLabel label;
        private ControlProperty property;
        
        public ItemPanel(Component editor) {
            this(editor, null);
        }
        
        public ItemPanel(Component editor, Component container) {
            this.editor = editor;
            this.editorWrapper = container;
            ActiveControl con = (ActiveControl) editor;
            property = con.getControlProperty();
            
            label = new XLabel(true);
            label.setLabelFor(editor);
            label.setAddCaptionColon(addCaptionColon);
            
            setOpaque(false);
            setLayout(new ItemPanelLayout(property));
            add(label, "label");
            if ( container != null ) {
                add(container, "editor");
            } else {
                add(editor, "editor");
            }
            
            PropertyChangeListener listener = new ContainablePropetyListener(this);
            property.addPropertyChangeListener(listener);
        }
        
        public boolean match(Component editor) {
            if (editor == null) return false;
            
            return (this.editor == editor);
        }
        
        public Component getEditorComponent() { return editor; }
        public Component getEditorWrapper() { return editorWrapper; }
        public XLabel getLabelComponent() { return label; }
        public ControlProperty getControlProperty() { return property; }
        
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
            
            if ( "captionWidth".equals(propName) ) {
                panel.revalidate();
            } else if ( "showCaption".equals(propName)) {
                panel.revalidate();
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
                String orient = getCaptionOrientation();
                
                if ( UIConstants.LEFT.equals(orient) || UIConstants.TOP.equals(orient) ) {
                    Rectangle rec = layoutLabel(x, y, w, h, captionWidth);
                    layoutEditor(rec.x, rec.y, rec.width, rec.height);
                } else {
                    Rectangle rec = layoutEditor(x, y, w, h);
                    layoutLabel(rec.x, rec.y, rec.width, rec.height, captionWidth);
                }
            }
        }
        
        //<editor-fold defaultstate="collapsed" desc="  layout helper  ">
        private Rectangle layoutLabel(int x, int y, int w, int h, int captionWidth) {
            if (label != null && isShowCaption()) {
                int cw = captionWidth;
                Dimension dim = label.getPreferredSize();
                
                if ( (UIConstants.TOP.equals(captionOrientation) || UIConstants.BOTTOM.equals(captionOrientation)) && editor != null )
                    cw = editor.getPreferredSize().width;
                else if (cw <= 0) 
                    cw = dim.width;
                
                label.setBounds(x, y, cw, h);
                
                if ( UIConstants.TOP.equals(captionOrientation) ) {
                    y += dim.height;
                    h -= dim.height;
                } else {
                    x += cw;
                    w -= cw;
                }
            }
            return new Rectangle(x, y, w, h);
        }
        
        private Rectangle layoutEditor(int x, int y, int w, int h) {
            if (editor != null) {
                Dimension dim = editor.getPreferredSize();
                int cw = dim.width;
                if ( cw > w || cw <= 0 ) {
                    cw = w;
                }
                editor.setBounds(x, y, cw, dim.height);
                if ( UIConstants.BOTTOM.equals(captionOrientation) ) {
                    y += dim.height;
                    h -= dim.height;
                } else {
                    x += cw;
                    w -= cw;
                }
            }
            return new Rectangle(x, y, w, h);
        }
        //</editor-fold>
        
        public Dimension getLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                int w=0, h=0;
                int captionWidth = getPreferredCaptionWidth();
                String orient = getCaptionOrientation();
                
                if (label != null) {
                    if ( isShowCaption() ) {
                        applyCaptionStyles((JLabel) label);
                        Dimension dim = label.getPreferredSize();
                        
                        if ( UIConstants.TOP.equals(orient) || UIConstants.BOTTOM.equals(orient) ) {
                            h += dim.height;
                            w = Math.max(w, dim.width);
                        } else {
                            int cw = captionWidth;
                            if (cw <= 0) cw = dim.width;
                            w += cw;
                            h = Math.max(h, dim.height);
                        }
                        
                        label.setVisible(true);
                    } else {
                        label.setVisible(false);
                    }
                }
                
                if (editor != null) {
                    Dimension dim = editor.getPreferredSize();
                    if ( UIConstants.TOP.equals(orient) || UIConstants.BOTTOM.equals(orient) ) {
                        h += dim.height;
                        w = Math.max(w, dim.width);
                    } else {
                        w += dim.width;
                        h = Math.max(h, dim.height);
                    }
                }
                
                Insets margin = parent.getInsets();
                w += (margin.left + margin.right);
                h += (margin.top + margin.bottom);
                return new Dimension(w, h);
            }
        }
        
        private void applyCaptionStyles(JLabel label) {
            //vertical alignment
            String valign = getCaptionVAlignment();
            if ( UIConstants.CENTER.equals(valign) )
                label.setVerticalAlignment(SwingConstants.CENTER);
            else if ( UIConstants.BOTTOM.equals(valign) )
                label.setVerticalAlignment(SwingConstants.BOTTOM);
            else
                label.setVerticalAlignment(SwingConstants.TOP);
            
            //horizontal alignment
            String halign = getCaptionHAlignment();
            if ( UIConstants.CENTER.equals(halign) )
                label.setHorizontalAlignment(SwingConstants.CENTER);
            else if ( UIConstants.RIGHT.equals(halign) )
                label.setHorizontalAlignment(SwingConstants.RIGHT);
            else
                label.setHorizontalAlignment(SwingConstants.LEFT);
            
            if ( captionPadding != null )
                ((XLabel) label).setPadding(captionPadding);
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
                    if ( UIConstants.HORIZONTAL.equals(orientation) ) {
                        if (x > 0) x += getCellspacing();
                        x += cellpadding.left;
                        c.setBounds(x, y, dim.width, dim.height);
                    } else {
                        if (y > 0) y += getCellspacing();
                        y += cellpadding.top;
                        c.setBounds(x, y, w, dim.height);
                    }
                                        
                    //increment
                    if ( UIConstants.HORIZONTAL.equals(orientation) )
                        x += dim.width + cellpadding.right;
                    else
                        y += dim.height + cellpadding.bottom;
                }
            }
        }
        
        public Dimension getLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                int w=0, h=0;
                
                Component[] comps = parent.getComponents();
                if ( Beans.isDesignTime() && comps.length == 0 ) {
                    return new Dimension(100, 100);
                }
                
                for (int i=0; i<comps.length; i++) {
                    Component c = comps[i];
                    if (c instanceof ItemPanel) {
                        Dimension dim = c.getPreferredSize();
                        if ( UIConstants.HORIZONTAL.equals(orientation) ) {
                            if (h == 0) h = dim.height;
                            h = Math.min(h, dim.height);
                            
                            //add cellspacing and cellpadding
                            if (w > 0) w += getCellspacing();
                            w += dim.width + cellpadding.left + cellpadding.right;
                            
                        } else {
                            if (w == 0) w = dim.width;
                            w = Math.min(w, dim.width);
                            
                            //add cellspacing and cellpadding
                            if (h > 0) h += getCellspacing();
                            h += dim.height + cellpadding.top + cellpadding.bottom;
                        }
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
