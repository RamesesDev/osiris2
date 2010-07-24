/*
 * XStyleRule.java
 *
 * Created on July 23, 2010, 9:46 AM
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.rcp.common.StyleRule;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.support.StyleRuleParser;
import com.rameses.rcp.support.StyleRuleParser.DefaultParseHandler;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.util.ValueUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.Beans;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JPanel;


public class XStyleRule extends JPanel implements UIControl {
    
    private Binding binding;
    private StyleRule[] styleRules;
    private boolean loaded;
    
    
    public XStyleRule() {
        if(Beans.isDesignTime()) {
            setOpaque(true);
            setPreferredSize(new Dimension(20,20));
            setBackground(Color.RED);
            
        } else {
            setVisible(false);
        }
    }
    
    public String[] getDepends() { return null; }
    public int getIndex() { return 1000; }
    public void refresh() {}
    
    public void setBinding(Binding binding) {
        this.binding = binding;
    }
    
    public Binding getBinding() {
        return binding;
    }
    
    public void load() {
        if(loaded) return;
        loaded = true;
        
        Collection newRules = null;
        if( !ValueUtil.isEmpty(getName()) ) {
            Object value = UIControlUtil.getBeanValue(this);
            if(value==null) {
                //do nothing
            } else if(value instanceof StyleRule[]) {
                newRules = new ArrayList();
                for(StyleRule s: (StyleRule[])value ) {
                    newRules.add(s);
                }
            } else if( value instanceof Collection ) {
                newRules = (Collection) value;
            } else {
                newRules = loadStyleFromFile(value);
            }
        } else if( styleRules!=null ) {
            newRules = new ArrayList();
            for(StyleRule s: styleRules) {
                newRules.add(s);
            }
        }
        
        if(newRules!=null) {
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
    }
    
    private List<StyleRule> loadStyleFromFile(Object source) {
        InputStream is = null;
        
        try {
            if ( source instanceof InputStream ) {
                is = (InputStream) source;
            } else if ( source instanceof byte[] ) {
                is = new ByteArrayInputStream( (byte[])source );
            } else if ( source instanceof URL ) {
                is = ((URL) source).openStream();
            } else if ( source instanceof File ) {
                is = new FileInputStream((File) source);
            } else if ( source instanceof String ) {
                ClassLoader loader = ClientContext.getCurrentContext().getClassLoader();
                is = loader.getResourceAsStream( source.toString() );
            }
            
            StyleRuleParser sp = new StyleRuleParser();
            DefaultParseHandler handler =new DefaultParseHandler();
            sp.parse(is, handler);
            
            for ( Object o: handler.getList() ) {
                System.out.println( o );
            }
            
            return handler.getList();
            
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try{ is.close(); }catch(Exception ign){}
        }
        
        return null;
    }
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    public StyleRule[] getStyleRules() {
        return styleRules;
    }
    
    public void setStyleRules(StyleRule[] styleRules) {
        this.styleRules = styleRules;
    }
    
}
