package com.rameses.rcp.framework;

import com.rameses.rcp.common.StyleRule;
import com.rameses.rcp.support.StyleRuleParser;
import com.rameses.rcp.support.StyleRuleParser.DefaultParseHandler;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;
import com.rameses.util.ValueUtil;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JPanel;


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
    
    
    //<editor-fold defaultstate="collapsed" desc="  View (class)  ">
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
                    Binding binding = viewPanel.getBinding();
                    
                    Class pageClass = panel.getClass();
                    JPanel master = null;
                    if( pageClass.isAnnotationPresent(Template.class) ) {
                        Template t = (Template)pageClass.getAnnotation(Template.class);
                        Class mClass = (Class) t.value()[0];
                        master = (JPanel) mClass.newInstance();
                        if(master instanceof UIViewPanel)
                            throw new Exception("Master template " + mClass.getName() + " must not extend a UIViewPanel" );
                        
                        loadStyleRules(mClass, binding);
                    }
                    
                    loadStyleRules(pageClass, binding);
                    
                    if ( master != null ) {
                        master.add(panel);
                        viewPanel.add(master);
                    } else {
                        viewPanel.add(panel);
                    }
                    binding.init();
                    binding.setBean(getCodeBean());
                    
                } catch(Exception e) {
                    throw new IllegalStateException(e);
                }
            }
            return viewPanel;
        }
        
        private void loadStyleRules(Class pageClass, Binding binding) {
            if ( !pageClass.isAnnotationPresent(StyleSheet.class) ) return;
            
            StyleSheet ss = (StyleSheet) pageClass.getAnnotation(StyleSheet.class);
            String source = ss.value();
            
            ClassLoader loader = ClientContext.getCurrentContext().getClassLoader();
            InputStream is = null;
            
            try {
                is = loader.getResourceAsStream(source);
                
                StyleRuleParser parser = new StyleRuleParser();
                DefaultParseHandler handler = new DefaultParseHandler();
                parser.parse(is, handler);
                
                List<StyleRule> newRules = handler.getList();
                if ( newRules.size() == 0 ) return;
                
                StyleRule[] oldRules = binding.getStyleRules();
                List list = new ArrayList();
                if(oldRules!=null) {
                    for(StyleRule s : oldRules) {
                        list.add(s);
                    }
                }
                for(Object s: newRules) {
                    list.add((StyleRule)s);
                }
                StyleRule[] sr =(StyleRule[]) list.toArray(new StyleRule[]{});
                binding.setStyleRules(sr);
                
            } catch (Exception ign) {;} finally {
                try { is.close(); } catch(Exception ign){;}
            }
        }
        
    }
    //</editor-fold>
    
}
