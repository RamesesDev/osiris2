package com.rameses.rcp.framework;

import com.rameses.util.ValueUtil;
import java.util.Hashtable;
import java.util.Map;
import javax.swing.JComponent;


public abstract class UIController {
    
    protected Map<String, View> viewCache = new Hashtable();
    private UIViewPanel currentView;
    private String id;
    private String title;
    private boolean initialized = false;
    
    public UIController() {
        
    }
    
    public void initialize() {
        if ( initialized ) return;
        for (View v: getViews()) {
            viewCache.put(v.getName(), v);
        }
        initialized = true;
    }
    
    public abstract View[] getViews();
    public abstract UIViewPanel getDefaultView();
    public abstract Object getCodeBean();
    public abstract Object init(Map params, String action);
    
    public void setCurrentView(String name) {
        if ( !ValueUtil.isEmpty(name) ) {
            currentView = viewCache.get(name).getViewPanel();
        }
    }
    
    public UIViewPanel getCurrentView() {
        if ( currentView == null ) {
            currentView = getDefaultView();
        }
        return currentView;
    }
    
    public UIViewPanel getView(String name) {
        return viewCache.get(name).getViewPanel();
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    // <editor-fold defaultstate="collapsed" desc=" View Class">
    public class View {
        
        private String name;
        private String template;
        private UIViewPanel viewPanel;
        
        public View(String name) {
            this(name, null);
        }
        
        public View(String name, String template) {
            this.name = name;
            this.template = template;
        }
        
        public String getName() {
            return name;
        }
        
        public String getTemplate() {
            return template;
        }
        
        public UIViewPanel getViewPanel() {
            if ( viewPanel == null ) {
                ClassLoader loader = ClientContext.getCurrentContext().getClassLoader();
                try {
                    JComponent panel = (JComponent) loader.loadClass(template).newInstance();
                    if ( panel instanceof UIViewPanel )
                        throw new Exception("Template " + template + " should not be an instance of UIViewPanel.");
                    
                    viewPanel = new UIViewPanel();
                    viewPanel.add(panel);
                    Binding binding = viewPanel.getBinding();
                    binding.init();
                    binding.setBean(getCodeBean());
                } catch(Exception e) {
                    throw new IllegalStateException(e);
                }
            }
            return viewPanel;
        }
        
    }
    //</editor-fold>
    
}
