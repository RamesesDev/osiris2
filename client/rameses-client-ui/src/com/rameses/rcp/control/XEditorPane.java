/*
 * XEditorPane.java
 *
 * Created on October 6, 2010, 9:39 AM
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.rcp.common.MsgBox;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.UIInput;
import com.rameses.rcp.util.ControlSupport;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.rcp.util.UIInputUtil;
import com.rameses.util.ValueUtil;
import java.beans.Beans;
import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;


public class XEditorPane extends JEditorPane implements UIInput {
    
    private Binding binding;
    private String[] depends;
    private int index;
    private boolean nullWhenEmpty = true;
    private boolean readonly;
    
    
    public XEditorPane() {
        super();
        
        //default content type
        setContentType("text/html");
        setReadonly(true);
        
        //for text/html content type only
        addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                processHyperlinkEvent(e);
            }
        });
    }
    
    //<editor-fold defaultstate="collapsed" desc="  processHyperlinkEvent  ">
    private void processHyperlinkEvent(HyperlinkEvent e) {
        EventType evt = e.getEventType();
        if (evt == EventType.ACTIVATED) {
            try {
                String desc = e.getDescription();
                if ( !ValueUtil.isEmpty(desc) )  {
                    String[] aa = desc.split("\\?");
                    Object[] param = null;
                    if ( aa.length > 1 ) param = new Object[]{ aa[1] };
                    Object outcome = ControlSupport.invoke(binding.getBean(), aa[0], param);
                    ControlSupport.fireNavigation(this, outcome);
                }
            } catch(Exception ex){
                MsgBox.err(new IllegalStateException(ex));
            }
        }
    }
    //</editor-fold>
    
    public void refresh() {
        Object value = UIControlUtil.getBeanValue(this);
        setValue(value);
        setCaretPosition(0);
    }
    
    public void load() {
        setInputVerifier(UIInputUtil.VERIFIER);
    }
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public void setName(String name) {
        super.setName(name);
        if ( Beans.isDesignTime() ) {
            setText(name);
        }
    }
    
    public Object getValue() {
        String txtValue = getText();
        if ( ValueUtil.isEmpty(txtValue) && nullWhenEmpty )
            return null;
        
        return txtValue;
    }
    
    public void setValue(Object value) {
        if ( value != null )
            setText(value.toString());
        else
            setText("");
    }
    
    public boolean isNullWhenEmpty() { return nullWhenEmpty; }
    public void setNullWhenEmpty(boolean nullWhenEmpty) { this.nullWhenEmpty = nullWhenEmpty; }
    
    public void setEditable(boolean editable) {
        setReadonly(!editable);
    }
    
    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
        super.setEditable(!readonly);
    }
    
    public boolean isReadonly() { return readonly; }
    
    public void setRequestFocus(boolean focus) {
        if ( focus ) requestFocus();
    }
    
    public boolean isImmediate() { return false; }
    
    public String[] getDepends() { return depends; }
    public void setDepends(String[] depends) { this.depends = depends; }
    
    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }
    
    public void setBinding(Binding binding) { this.binding = binding; }
    public Binding getBinding() { return binding; }
    //</editor-fold>
    
}
