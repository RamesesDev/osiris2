/*
 * XStyleRule.java
 *
 * Created on July 23, 2010, 9:46 AM
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.rcp.common.StyleRule;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.util.ValueUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.Beans;
import java.util.ArrayList;
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
        
        List newRules = null;
        if( !ValueUtil.isEmpty(getName()) ) {
            Object o = UIControlUtil.getBeanValue(this);
            if(o==null) {
                //do nothing
            }
            else if(o instanceof StyleRule[]) {
                newRules = new ArrayList();
                for(StyleRule s: (StyleRule[])o ) {
                    newRules.add(s);
                }
            }
            else if( o instanceof List ) {
                newRules = (List)o;
            }
        }
        else if( styleRules!=null ) {
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
