/*
 * XSuggest.java
 *
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.common.ExpressionResolver;
import com.rameses.common.MethodResolver;
import com.rameses.common.PropertyResolver;
import com.rameses.rcp.common.Task;
import com.rameses.rcp.control.XComboBox.ComboItem;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.rcp.util.UIInputUtil;
import com.rameses.util.ValueUtil;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.Beans;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;

public class XSuggest extends XComboBox {
    
    private String onSuggest;
    private JTextField editor;
    private Collection suggestions;
    
    /**
     * @description
     *  - this property (if true) allows input that is not in the list of suggestions
     *  - this is only availabe to String type input values
     */
    private boolean allowNew = true;
    
    private boolean searching;
    private boolean showSuggestions;
    
    
    public XSuggest() {
        super();
    }
    
    public void refresh() {
        Object value = UIControlUtil.getBeanValue(this);
        setValue(value);
    }
    
    public void load() {
        updateSuggestions();
        setEditable(true);
        ComboBoxEditor cboxEditor = getEditor();
        editor = (JTextField) cboxEditor.getEditorComponent();
        editor.addKeyListener(new KeyAdapter() {
            
            public void keyPressed(KeyEvent e) { processKeyPressed(e); }
            public void keyTyped(KeyEvent e) { processKeyTyped(e); }
            
        });
        editor.addFocusListener(new FocusAdapter() {
            
            public void focusLost(FocusEvent e) {
                if ( e.isTemporary() ) return;
                if ( !allowNew && getSelectedIndex() < 0 ) {
                    refresh();
                }
            }
            
        });
        
        setEditor(new EditorWrapper(cboxEditor));
        
        if ( isImmediate() ) {
            super.addItemListener(this);
        } else {
            super.setInputVerifier(UIInputUtil.VERIFIER);
        }
    }
    
    private void processKeyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if ( code == KeyEvent.VK_ESCAPE ) {
            showSuggestions = false;
        } else if (code == KeyEvent.VK_ENTER) {
            if ( getSelectedIndex() < 0 ) {
                setSelectedIndex(0);
            }
            showSuggestions = false;
            updateSuggestions();
        } else {
            showSuggestions = true;
        }
    }
    
    private void processKeyTyped(KeyEvent e) {
        if ( showSuggestions && !searching ) {
            searching = true;
            SearchTask t = new SearchTask();
            ClientContext.getCurrentContext().getTaskManager().addTask(t);
        }
    }
    
    private void fetchSuggestions(String text) {
        if ( text != null && text.trim().length() > 0 ) {
            ClientContext ctx = ClientContext.getCurrentContext();
            try {
                MethodResolver mr = ctx.getMethodResolver();
                suggestions = (List) mr.invoke(binding.getBean(), onSuggest, new Class[]{String.class}, new Object[]{ text });
                
            } catch(Exception e) {
                ctx.getPlatform().showError(this, e);
            }
        }
    }
    
    private void updateSuggestions() {
        if ( getSelectedIndex() >= 0 ) {
            ComboItem ci = (ComboItem) getSelectedItem();
            setModel(new DefaultComboBoxModel(new Object[]{ci}));
            setSelectedIndex(0);
            
        } else {
            if ( suggestions == null || suggestions.size() == 0 ) {
                setModel(new DefaultComboBoxModel(new Object[]{new ComboItem(null, "<no suggestions>")}));
            } else {
                ExpressionResolver er = ClientContext.getCurrentContext().getExpressionResolver();
                Vector items = new Vector();
                for ( Object o: suggestions ) {
                    Object caption = null;
                    if ( !ValueUtil.isEmpty(getExpression()) ) {
                        caption = er.evaluate(o, getExpression());
                    }
                    if ( caption == null ) caption = o;
                    
                    items.addElement(new ComboItem(o, caption+""));
                }
                setModel(new DefaultComboBoxModel(items));
            }
            setSelectedIndex(-1);
        }
    }
    
    
    public Object getValue() {
        if ( Beans.isDesignTime() ) return null;
        if ( getSelectedIndex() < 0 )  {
            if ( allowNew && editor.getText().length() > 0 )
                return editor.getText();
            else
                return null;
        }
        
        Object value = ((ComboItem)super.getSelectedItem()).getValue();
        if ( value != null && !ValueUtil.isEmpty(getItemKey()) ) {
            PropertyResolver res = ClientContext.getCurrentContext().getPropertyResolver();
            value = res.getProperty(value, getItemKey());
        }
        
        return value;
    }
    
    public void setValue(Object value) {
        if ( Beans.isDesignTime() ) return;
        if ( value instanceof KeyEvent ) return;
        
        if ( suggestions == null || suggestions.size() == 0 ) {
            String text = ValueUtil.isEmpty(value)? "" : value+"";
            editor.setText(text);
            editor.setCaretPosition(text.length());
            
        } else {
            for(int i=0; i< getItemCount();i++) {
                ComboItem ci = (ComboItem) getItemAt(i);
                if( isSelected(ci, value) ) {
                    model.setSelectedItem(ci);
                    break;
                }
            }
            if ( getSelectedIndex() < 0 ) {
                editor.setText("");
            }
        }
    }
    
    public boolean isDynamic() {
        return true;
    }
    
    public String getOnSuggest() {
        return onSuggest;
    }
    
    public void setOnSuggest(String onSuggest) {
        this.onSuggest = onSuggest;
    }
    
    public void setModel(ComboBoxModel model) {
        super.model = (DefaultComboBoxModel) model;
        super.setModel(model);
    }
    
    public boolean isAllowNew() {
        return allowNew;
    }
    
    public void setAllowNew(boolean allowNew) {
        this.allowNew = allowNew;
    }
    
    
    
    //<editor-fold defaultstate="collapsed" desc="  SearchTask (class)  ">
    private class SearchTask extends Task {
        
        public boolean accept() {
            return true;
        }
        
        public void execute() {
            try {
                String text = editor.getText();
                suggestions = null;
                if ( text.trim().length() > 0 ) {
                    fetchSuggestions(text);
                }
                
                text = editor.getText();
                setSelectedIndex(-1);
                updateSuggestions();
                editor.setText(text);
                editor.setCaretPosition( text.length() );
                
                if ( suggestions == null || suggestions.size() == 0) {
                    hidePopup();
                } else {
                    showPopup();
                }
                searching = false;
                
            } catch(Exception ign) {
            } finally { setEnded(true); }
        }
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  EditorWrapper (class)  ">
    private class EditorWrapper implements ComboBoxEditor {
        
        private ComboBoxEditor orig;
        
        EditorWrapper(ComboBoxEditor orig) {
            this.orig = orig;
        }
        
        public void addActionListener(ActionListener l) {
            orig.addActionListener(l);
        }
        public Component getEditorComponent() {
            return orig.getEditorComponent();
        }
        public Object getItem() {
            return orig.getItem();
        }
        public void removeActionListener(ActionListener l) {
            orig.removeActionListener(l);
        }
        public void selectAll() {
            orig.selectAll();
        }
        public void setItem(Object object) {
            orig.setItem(object);
        }
        
    }
    //</editor-fold>
    
}
