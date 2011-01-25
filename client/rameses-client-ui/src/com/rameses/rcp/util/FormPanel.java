package com.rameses.rcp.util;

import com.rameses.common.ExpressionResolver;
import com.rameses.common.PropertyResolver;
import com.rameses.rcp.common.FormControl;
import com.rameses.rcp.common.FormPanelModel;
import com.rameses.rcp.common.ValidatorEvent;
import com.rameses.rcp.constant.UIConstants;
import com.rameses.rcp.control.XEditorPane;
import com.rameses.rcp.control.XLabel;
import com.rameses.rcp.control.border.XUnderlineBorder;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.BindingListener;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.ui.ActiveControl;
import com.rameses.rcp.ui.ControlProperty;
import com.rameses.rcp.ui.ControlContainer;
import com.rameses.rcp.ui.UIComposite;
import com.rameses.rcp.ui.UICompositeFocusable;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.ui.UIInput;
import com.rameses.rcp.ui.Validatable;
import com.rameses.util.ValueUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.beans.Beans;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;

public class FormPanel extends JPanel implements UIComposite, ControlContainer, Validatable, ActiveControl, UIConstants {
    
    private int cellspacing = 2;
    private Insets cellpadding = new Insets(0,0,0,0);
    private String orientation = UIConstants.VERTICAL;
    
    private Insets padding;
    private Border origBorder;
    
    //caption options
    private int captionWidth = 80;
    private String captionVAlignment = UIConstants.TOP;
    private String captionHAlignment = UIConstants.LEFT;
    private String captionOrientation = UIConstants.LEFT;
    private Font captionFont;
    private Color captionForeground;
    private Border captionBorder = new XUnderlineBorder();
    private Insets captionPadding = new Insets(0,1,0,5);
    private boolean addCaptionColon = true;
    
    private Binding binding;
    private String[] depends;
    private int index;
    private List<UIControl> controls = new ArrayList();
    private boolean dynamic;
    private ControlProperty property = new ControlProperty();
    private ActionMessage actionMessage = new ActionMessage();
    
    private List<UIControl> nonDynamicControls = new ArrayList();
    
    //internal flag
    private boolean _loaded;
    private boolean _reloaded;
    
    private String viewType;
    private String oldViewType;
    private boolean viewTypeSet;
    
    private LayoutManager layout;
    private JEditorPane htmlPane;
    
    private String emptyWhen;
    private String emptyText;
    private XLabel emptyLbl;
    
    private FormPanelModel model;
    private FormPanelModel.Listener  defaultListener;
    
    
    public FormPanel() {
        super.setLayout(layout = new Layout());
        setPadding(new Insets(5,5,5,5));
        setOpaque(false);
        
        setFont(Font.decode("Arial-plain-11"));
        setCaptionFont(Font.decode("Arial-plain-11"));
        setCaptionForeground(UIManager.getColor("Label.foreground"));
    }
    
    //<editor-fold defaultstate="collapsed" desc=" FormPanel implementations ">
    public void setLayout(LayoutManager mgr) {;}
    
    protected void addImpl(Component comp, Object constraints, int index) {
        ItemPanel p = null;
        Component control = comp;
        //check if it is a containable component
        if ( comp instanceof ActiveControl ) {
            p = new ItemPanel(this, comp);
        } else if ( comp instanceof JScrollPane ) {
            control = ((JScrollPane) comp).getViewport().getView();
            if ( control instanceof ActiveControl ) {
                p = new ItemPanel(this, control, comp);
            }
        }
        
        if ( p != null ) {
            if ( !_loaded && control instanceof UIControl )
                nonDynamicControls.add( (UIControl) control );
            
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
                if (p.getEditorWrapper() == comp) return p;
            }
        }
        return null;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  control support properties  ">
    public List<? extends UIControl> getControls() {
        return controls;
    }
    
    public String[] getDepends()             { return depends; }
    public void setDepends(String[] depends) { this.depends = depends; }
    
    public int getIndex()           { return index; }
    public void setIndex(int index) { this.index = index; }
    
    public Binding getBinding()             { return binding; }
    public void setBinding(Binding binding) { this.binding = binding; }
    
    public boolean isDynamic()              { return dynamic; }
    public void setDynamic(boolean dynamic) { this.dynamic = dynamic; }
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    public void validateInput() {
        actionMessage.clearMessages();
        for(UIControl c: controls) {
            if( !(c instanceof Validatable) ) continue;
            
            Validatable v = (Validatable) c;
            v.validateInput();
            ActionMessage ac = v.getActionMessage();
            if( ac != null && ac.hasMessages() )
                actionMessage.addMessage(ac);
        }
    }
    
    public ActionMessage getActionMessage() {
        return actionMessage;
    }
    
    public void requestFocus() {
        focusFirstInput();
    }
    
    public boolean focusFirstInput() {
        List<UIControl> allControls = new ArrayList();
        if ( !nonDynamicControls.isEmpty() )
            allControls.addAll( nonDynamicControls );
        
        allControls.addAll(controls);
        
        try {
            for(UIControl c: allControls) {
                if( actionMessage.hasMessages() ) {
                    if( !(c instanceof Validatable) ) continue;
                    
                    Validatable v = (Validatable) c;
                    v.validateInput();
                    if( v.getActionMessage().hasMessages() ) {
                        ((Component) v).requestFocus();
                        return true;
                    }
                } else if ( c instanceof UICompositeFocusable ) {
                    UICompositeFocusable uis = (UICompositeFocusable) c;
                    if ( uis.focusFirstInput() ) return true;
                    
                } else if ( c instanceof UIInput ) {
                    UIInput u = (UIInput) c;
                    JComponent jc = (JComponent) c;
                    if ( u.isReadonly() || !jc.isFocusable() || !jc.isEnabled() || !jc.isVisible())
                        continue;
                    
                    jc.requestFocus();
                    return true;
                }
            }
            
        } catch(Exception e) {;} finally {
            allControls = null;
        }
        return false;
    }
    
    public boolean isHasNonDynamicContents() {
        return !nonDynamicControls.isEmpty();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  refresh/load  ">
    public void refresh() {
        if( _reloaded || (viewTypeSet && !ValueUtil.isEqual(oldViewType, viewType))) {
            refreshForm();
            oldViewType = viewType;
            _reloaded = false;
        }
    }
    
    public void load() {
        binding.addBindingListener(new FormPanelBindingListener());
        build();
        _loaded = true;
        _reloaded = true;
    }
    
    public void reload() {
        build();
        _reloaded = true;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  helper method  ">
    private void build() {
        if ( ValueUtil.isEmpty(getName()) ) return;
        
        //remove only dynamic controls
        for(UIControl u: controls) {
            remove((Component) u);
            //let garbage collector collect the reference
            u = null;
        }
        
        controls.clear();
        property.setRequired(false);
        boolean htmlView = HTML_VIEW.equals(viewType);
        
        List<FormControl> list = getFormControls();
        FormControlUtil fcUtil = FormControlUtil.getInstance();
        for(FormControl fc: list) {
            UIControl uic = fcUtil.getControl(fc);
            if ( uic == null ) continue;
            
            uic.setBinding(binding);
            uic.load();
            
            if( uic instanceof Validatable && ((Validatable) uic).isRequired() )
                property.setRequired(true);
            
            controls.add( uic );
        }
    }
    
    private void refreshForm() {
        //check if view is html
        boolean htmlView = HTML_VIEW.equals(viewType);
        
        if( !htmlView && htmlPane != null ) {
            remove( htmlPane );
            htmlPane = null;
        }
        
        if( htmlView ) {
            //remove controls
            for(UIControl u : nonDynamicControls) {
                remove((Component)u);
            }
            
            for(UIControl u: controls) {
                remove((Component) u);
            }
            
            if( htmlPane == null ) {
                initHtmlPane();
            }
        } else {
            super.setLayout(layout);
        }
        
        boolean empty = false;
        
        //visibility and empty text support
        if( controls.size() == 0 && nonDynamicControls.size() == 0 && !ValueUtil.isEmpty(emptyText) ) {
            empty = true;
        } else {
            if( !ValueUtil.isEmpty(emptyWhen) ) {
                ExpressionResolver er = ClientContext.getCurrentContext().getExpressionResolver();
                Object value = er.evaluate(binding.getBean(), emptyWhen);
                if( value != null )
                    empty = !"false".equals(value.toString());
            }
        }
        
        if( !empty ) {
            if( emptyLbl != null ) {
                remove(emptyLbl);
                emptyLbl = null;
            }
            if( htmlView ) {
                FormControlUtil fcUtil = FormControlUtil.getInstance();
                htmlPane.setText( fcUtil.renderHtml(getAllControls(), this) );
            } else {
                //if previous view is htmlView
                //add again the nonDynamicControls
                if( !ValueUtil.isEqual(oldViewType, viewType) ) {
                    for(UIControl u : nonDynamicControls) {
                        add((Component)u);
                        u.refresh();
                    }
                }
                
                for(UIControl u : controls) {
                    u.refresh();
                    if( !htmlView ) {
                        if(layout != super.getLayout()) super.setLayout(layout);
                        
                        //add component if form panel is reloaded
                        //this happends if the form panel is dynamic
                        if( _reloaded ) add( (Component)u );
                        u.refresh();
                    }
                }
            }
        } else {
            if( htmlView ) {
                Font f = getFont();
                String html = "<font face='"+f.getFamily()+"' size='"+f.getSize()+"pt'>"+(emptyText==null?"":emptyText)+"</font>";
                htmlPane.setText(html);
            } else {
                if( emptyText != null ) {
                    if( emptyLbl == null ) {
                        emptyLbl = new XLabel();
                        emptyLbl.setShowCaption(false);
                    }
                    emptyLbl.setExpression(emptyText);
                    add( emptyLbl );
                }
            }
        }
        
        SwingUtilities.updateComponentTreeUI(this);
    }
    
    private void initHtmlPane() {
        XEditorPane editorPane = new XEditorPane();
        editorPane.setBinding(binding);
        super.setLayout(new BorderLayout());
        super.addImpl( editorPane, null, 0 );
        htmlPane = editorPane;
    }
    
    private List getFormControls() {
        List list = new ArrayList();
        
        Object value = null;
        try {
            value = UIControlUtil.getBeanValue(this);
        } catch(Exception e) {;}
        
        if (value instanceof FormPanelModel) {
            if( model != null ) {
                model.setListener(null);
                model = null;
            }
            
            model = (FormPanelModel) value;
            if( defaultListener == null ) defaultListener = new ModelListener();
            model.setListener(defaultListener);
            value = model.getFormControls();
        }
        
        if (value == null) {
            //do nothing
        } else if (value.getClass().isArray()) {
            for (FormControl fc: (FormControl[]) value) {
                list.add(fc);
            }
        } else if (value instanceof Collection) {
            list.addAll((Collection) value);
        }
        
        return list;
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
        updateLabelsCaption();
    }
    
    private void updateLabelsCaption() {
        for(Component c: getComponents()) {
            if ( c instanceof ItemPanel ) {
                XLabel lbl = ((ItemPanel)c).getLabelComponent();
                lbl.setAddCaptionColon(addCaptionColon);
            }
        }
    }
    
    public Font getCaptionFont() { return captionFont; }
    public void setCaptionFont(Font captionFont) {
        this.captionFont = captionFont;
        updateLabelsFont();
    }
    
    private void updateLabelsFont() {
        for(Component c: getComponents()) {
            if ( c instanceof ItemPanel ) {
                XLabel lbl = ((ItemPanel)c).getLabelComponent();
                lbl.setFont(captionFont);
            }
        }
    }
    
    public Color getCaptionForeground() { return captionForeground; }
    public void setCaptionForeground(Color captionForeground) {
        this.captionForeground = captionForeground;
        updateLabelsForeground();
    }
    
    private void updateLabelsForeground() {
        for(Component c: getComponents()) {
            if ( c instanceof ItemPanel ) {
                XLabel lbl = ((ItemPanel)c).getLabelComponent();
                lbl.setForeground(captionForeground);
            }
        }
    }
    
    public Border getCaptionBorder() {
        return captionBorder;
    }
    
    public void setCaptionBorder(Border captionBorder) {
        this.captionBorder = captionBorder;
        updateLabelsBorder();
    }
    
    private void updateLabelsBorder() {
        for(Component c: getComponents()) {
            if ( c instanceof ItemPanel ) {
                XLabel lbl = ((ItemPanel)c).getLabelComponent();
                lbl.setBorder(captionBorder);
            }
        }
    }
    
    public void setRequired(boolean required) {}
    public boolean isRequired() {
        return property.isRequired();
    }
    
    
    public String getViewType() { return viewType; }
    public void setViewType(String viewType) {
        if( !viewTypeSet )
            oldViewType = viewType;
        else
            oldViewType = this.viewType;
        
        this.viewType = viewType;
        viewTypeSet = true;
    }
    
    public boolean isChildrenAcceptStyles() {
        return false;
    }
    
    public String getEmptyText()               { return emptyText; }
    public void setEmptyText(String emptyText) { this.emptyText = emptyText; }
    
    public List<UIControl> getAllControls() {
        List<UIControl> allControls = new ArrayList();
        allControls.addAll(nonDynamicControls);
        allControls.addAll(controls);
        return allControls;
    }
    
    public String getEmptyWhen() {
        return emptyWhen;
    }
    
    public void setEmptyWhen(String emptyWhen) {
        this.emptyWhen = emptyWhen;
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
    
    //<editor-fold defaultstate="collapsed" desc="  FormPanelBindingListener (class)  ">
    private class FormPanelBindingListener implements BindingListener {
        
        public void notifyDepends(UIControl u, Binding parent) {
            if( ValueUtil.isEqual(viewType, HTML_VIEW) ) return;
            
            if ( ValueUtil.isEmpty(u.getName()) ) return;
            Set<UIControl> refreshed = new HashSet();
            for( UIControl control : controls ) {
                if ( !isDependent( u.getName(), control ) ) continue;
                _doRefresh( control, refreshed );
            }
            refreshed.clear();
            refreshed = null;
        }
        
        private boolean isDependent( String parentName, UIControl child ) {
            if ( child.getDepends() != null ) {
                for(String s : child.getDepends()) {
                    if ( parentName.matches(s) ) return true;
                }
            }
            return false;
        }
        
        public void refresh(String regEx) {
            if( ValueUtil.isEqual(viewType, HTML_VIEW) ) return;
            
            Set<UIControl> refreshed = new HashSet();
            for( UIControl uu : controls ) {
                String name = uu.getName();
                if ( regEx != null && name != null && !name.matches(regEx) ){
                    continue;
                }
                
                _doRefresh( uu, refreshed );
            }
            refreshed.clear();
            refreshed = null;
        }
        
        private void _doRefresh( UIControl u, Set refreshed ) {
            if( refreshed.add(u) ) {
                u.refresh();
            }
        }
        
        public void validate(ActionMessage actionMessage, Binding parent) {}
        public void validateBean(ValidatorEvent evt) {}
        public void formCommit() {}
        public void update() {}
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  ModelListener (clas)  ">
    private class ModelListener implements FormPanelModel.Listener {
        
        public void onPropertyUpdated(String name, Object value) {
            FormPanel handle = FormPanel.this;
            PropertyResolver res = ClientContext.getCurrentContext().getPropertyResolver();
            try {
                res.setProperty(handle, name, value);
            } catch(Exception e){;}
        }
        
        public String getHtmlFormat(boolean partial) {
            FormControlUtil fcUtil = FormControlUtil.getInstance();
            return fcUtil.renderHtml(getAllControls(), FormPanel.this, partial);
        }
        
        public void onReload() {
            reload();
            refresh();
        }
        
    }
    //</editor-fold>
    
}
