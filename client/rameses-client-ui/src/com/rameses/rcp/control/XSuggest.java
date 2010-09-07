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
import com.rameses.rcp.constant.TextCase;
import com.rameses.rcp.control.XComboBox.ComboItem;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.support.TextDocument;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.rcp.util.UIInputUtil;
import com.rameses.util.ValueUtil;
import java.awt.Container;
import java.awt.Rectangle;
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
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ComboBoxUI;

public class XSuggest extends XComboBox {
    
    private String onSuggest;
    private JTextField editor;
    private Collection suggestions;
    
    /**
     * @description
     *  - <code>allowNew</code> property (if true) allows input that is not in the list of suggestions
     *  - <code>allowNew</code> is only availabe to String type input values
     */
    private boolean allowNew = true;
    private boolean showArrowButton = false;
    
    private boolean searching;
    private boolean showSuggestions;
    private TextDocument document;
    
    
    public XSuggest() {
        super();
        init();
    }
    
    //<editor-fold defaultstate="collapsed" desc="  initialize  ">
    private void init() {
        document = new TextDocument();
        document.setTextCase(TextCase.UPPER);
        
        super.setEditable(true);
        super.setUI(new javax.swing.plaf.metal.MetalComboBoxUI(){
            
            public void layoutComboBox(Container parent, MetalComboBoxLayoutManager manager) {
                if ( showArrowButton ) {
                    super.layoutComboBox(parent, manager);
                } else {
                    if ( isEditable() ) {
                        Rectangle rb = XSuggest.this.getPreferredBounds();
                        editor.setBounds(0, 0, rb.width, rb.height);
                    } else {
                        super.layoutComboBox(parent, manager);
                    }
                    arrowButton.setBounds(0,0,0,0);
                }
            }
            
        });
    }
    
    private Rectangle getPreferredBounds() {
        Rectangle r = super.getBounds();
        int w = super.getWidth();
        int h = super.getHeight();
        w = Math.max(w, r.width);
        h = Math.max(h, r.height);
        return new Rectangle(r.x, r.y, w, h);
    }
    
    //</editor-fold>
    
    public void setUI(ComboBoxUI ui) {}
    public void setEditable(boolean editable) {}
    
    public void refresh() {
        Object value = UIControlUtil.getBeanValue(this);
        setValue(value);
    }
    
    public void load() {
        ComboBoxEditor cboxEditor = getEditor();
        editor = (JTextField) cboxEditor.getEditorComponent();
        editor.setBounds(super.getBounds());
        editor.setBorder( (Border) UIManager.get("TextField.border") );
        editor.setDocument(document);
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
        
        updateSuggestions();
        
        if ( isImmediate() ) {
            super.addItemListener(this);
        } else {
            super.setInputVerifier(UIInputUtil.VERIFIER);
        }
        
    }
    
    //<editor-fold defaultstate="collapsed" desc="  helper methods  ">
    private void processKeyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if ( code == KeyEvent.VK_ESCAPE ) {
            showSuggestions = false;
        } else if (code == KeyEvent.VK_ENTER) {
            if ( suggestions != null && suggestions.size() > 0 ) {
                if ( getSelectedIndex() < 0 ) {
                    setSelectedIndex(0);
                }
                updateSuggestions();
            }
            showSuggestions = false;
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
        if ( !ValueUtil.isEmpty(onSuggest) && text != null && text.trim().length() > 0 ) {
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
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
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
    
    public void setSelectedItem(Object object) {
        ComboItem ci = (ComboItem) object;
        if (ci != null && ci.getValue() != null ) {
            super.setSelectedItem(object);
        } else {
            String text = editor.getText();
            super.setSelectedItem(null);
            editor.setText(text);
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
    
    public boolean isShowArrowButton() {
        return showArrowButton;
    }
    
    public void setShowArrowButton(boolean showArrowButton) {
        this.showArrowButton = showArrowButton;
        super.revalidate();
    }
    
    public TextCase getTextCase() {
        return document.getTextCase();
    }
    
    public void setTextCase(TextCase textcase) {
        document.setTextCase(textcase);
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="  SearchTask (class)  ">
    private class SearchTask extends Task {
        
        public boolean accept() {
            return true;
        }
        
        public void execute() {
            try {
                String search = editor.getText();
                suggestions = null;
                if ( search.trim().length() > 0 ) {
                    fetchSuggestions(search);
                }
                
                String text = editor.getText();
                //if the response took so long, dont display the result
                //if the text typed is no longer equal to the current typed text
                if ( search.equals(text) ) {
                    setSelectedIndex(-1);
                    updateSuggestions();
                    editor.setText(text);
                    editor.setCaretPosition( text.length() );
                    
                    if ( suggestions == null || suggestions.size() == 0) {
                        hidePopup();
                    } else {
                        showPopup();
                    }
                } else {
                    hidePopup();
                }
                searching = false;
                
            } catch(Exception ign) {
            } finally { setEnded(true); }
        }
        
    }
    //</editor-fold>
    
}
