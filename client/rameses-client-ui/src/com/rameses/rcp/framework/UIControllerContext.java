package com.rameses.rcp.framework;

import com.rameses.rcp.common.StyleRule;
import com.rameses.rcp.framework.UIController.View;
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

public class UIControllerContext {
    
    private UIController controller;
    private Map<String, UIViewPanel> viewCache = new Hashtable();
    private Map<String, View> viewSource = new Hashtable();
    private UIViewPanel currentView;
    
    private String id;
    private String title;
    private String name;
    
    public UIControllerContext(UIController controller) {
        setController(controller);
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getTitle() { return controller.getTitle(); }
    public void setTitle(String title) { this.title = title; }
    
    public UIViewPanel getDefaultView() {
        return getView(controller.getDefaultView());
    }
    
    public void setCurrentView(String name) {
        if ( !ValueUtil.isEmpty(name) ) {
            currentView = getView(name);
        }
    }
    
    public UIViewPanel getCurrentView() {
        if ( currentView == null ) {
            currentView = getDefaultView();
        }
        return currentView;
    }
    
    public UIViewPanel getView(String name) {
        if ( !viewCache.containsKey(name) ) {
            viewCache.put(name, buildViewPanel( viewSource.get(name) ));
        }
        return viewCache.get(name);
    }
    
    public UIController getController() {
        return controller;
    }
    
    public void setController(UIController controller) {
        this.controller = controller;
        if ( id == null ) id = controller.getId();
        if ( name == null ) name = controller.getName();
        if ( title == null ) title = controller.getTitle();
        indexViews();
    }
    
    //<editor-fold defaultstate="collapsed" desc="  utility methods  ">
    private void indexViews() {
        for (View v: controller.getViews()) {
            viewSource.put(v.getName(), v);
        }
    }
    
    private UIViewPanel buildViewPanel(View view) {
        ClassLoader loader = ClientContext.getCurrentContext().getClassLoader();
        UIViewPanel viewPanel = null;
        try {
            JComponent panel = (JComponent) loader.loadClass(view.getTemplate()).newInstance();
            if ( panel instanceof UIViewPanel )
                throw new Exception("Template " + view.getTemplate() + " should not be an instance of UIViewPanel.");
            
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
            binding.setController(controller);
            
        } catch(Exception e) {
            throw new IllegalStateException(e);
        }
        
        return viewPanel;
    }
    
    private void loadStyleRules(Class pageClass, Binding binding) {
        if ( !pageClass.isAnnotationPresent(StyleSheet.class) ) return;
        
        List<String> sources = new ArrayList();
        StyleSheet ss = (StyleSheet) pageClass.getAnnotation(StyleSheet.class);
        String source = ss.value();
        if(source.trim().length()>0) {
            if ( source.indexOf(",") > -1 ) {
                for (String s: source.split("\\s*,\\s*")) {
                    sources.add( s );
                }
            } else {
                sources.add( source );
            }
        }
        
        List<StyleRule> newRules = new ArrayList();
        ClassLoader loader = ClientContext.getCurrentContext().getClassLoader();
        
        InputStream is = null;
        if( sources.size() > 0 ) {
            for ( String s : sources ) {
                is = loader.getResourceAsStream(s);
                List<StyleRule> sr = getStyles(is);
                if ( sr.size() > 0 ) {
                    newRules.addAll( sr );
                }
            }
        } else {
            is = pageClass.getResourceAsStream(pageClass.getSimpleName()+".style");
            List<StyleRule> sr = getStyles(is);
            if ( sr.size() > 0 ) {
                newRules.addAll( sr );
            }
        }
        
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
        
    }
    
    private List<StyleRule> getStyles(InputStream is) {
        if( is != null ) {
            try {
                StyleRuleParser parser = new StyleRuleParser();
                DefaultParseHandler handler = new DefaultParseHandler();
                parser.parse(is, handler);
                
                return handler.getList();
                
            } catch (Exception ign) {
            } finally {
                try { is.close(); } catch(Exception ign){;}
            }
        }
        
        return new ArrayList();
    }
    //</editor-fold>
    
}
