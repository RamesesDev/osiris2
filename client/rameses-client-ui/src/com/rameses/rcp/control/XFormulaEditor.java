/*
 * XFormulaEditor.java
 *
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.rcp.common.FormulaEditorModel;
import com.rameses.rcp.constant.TrimSpaceOption;
import com.rameses.rcp.control.editor.FormulaDocument;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.ui.UIInput;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.rcp.util.UIInputUtil;
import com.rameses.util.ValueUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;



public class XFormulaEditor extends JTextPane implements UIInput {
    
    private Binding binding;
    private String[] depends;
    private int index;
    private String keywordItems;
    private boolean dynamic;
    private boolean required;
    private boolean readonly;
    private boolean nullWhenEmpty;
    private TrimSpaceOption trimSpaceOption = TrimSpaceOption.NONE;
    private String handler;
    
    private List<String> keywords;
    private FormulaDocument document;
    private FormulaEditorModel model;
    
    
    public XFormulaEditor() {
        document = new FormulaDocument();
        setDocument( document );
    }
    
    public void insert(String str) {
        AttributeSet attr = getInputAttributes().copyAttributes();
        int cp = getCaretPosition();
        try {
            document.insertString(cp, str, attr);
        } catch (BadLocationException ex) {
            if( ClientContext.getCurrentContext().isDebugMode() ) {
                ex.printStackTrace();
            }
        }
    }
    
    public void refresh() {
        if( dynamic ) loadVariables();
        try {
            Object value = UIControlUtil.getBeanValue(this);
            int caretPos = getCaretPosition();
            int length = getText().length();            
            setValue( value );            
            try { 
                if( caretPos == length )
                    setCaretPosition( getText().length() );
                else
                    setCaretPosition(caretPos); 
            }
            catch(Exception e){;}
        }
        catch(Exception e) {
            if( ClientContext.getCurrentContext().isDebugMode() ) {
                e.printStackTrace();
            }
        }
    }
    
    public void load() {
        if( !ValueUtil.isEmpty(handler) ) {
            try {
                Object value = UIControlUtil.getBeanValue(this, handler);
                if( value instanceof FormulaEditorModel ) {
                    model = (FormulaEditorModel) value;
                    model.setListener(new FormulaEditorModel.Listener() {                        
                        public void insert(String str) { XFormulaEditor.this.insert(str); }
                        public void load() { XFormulaEditor.this.load(); }
                        public void refresh() { XFormulaEditor.this.refresh(); }                        
                    });
                }
            }
            catch(Exception e) {
                if(ClientContext.getCurrentContext().isDebugMode()) {
                    e.printStackTrace();
                }
            }
        }
        
        if( !dynamic ) loadVariables();
        setInputVerifier( UIInputUtil.VERIFIER );
    }
    
    private void loadVariables() {
        List<String> list = new ArrayList();
        if( !getKeywords().isEmpty() )
            list.addAll( getKeywords() );
        
        try {
            Object value = UIControlUtil.getBeanValue(this, keywordItems);
            if( value == null ); //do nothing
            else if ( value instanceof Collection ) {
                for(Object o : (Collection) value) list.add( o+"" );
            }
            else if ( value.getClass().isArray() ) {
                for(Object o : (Object[]) value) list.add( o+"" );
            }
        } catch(Exception e) {
            if( ClientContext.getCurrentContext().isDebugMode() ) {
                e.printStackTrace();
            }
        }

        document.getKeywords().clear();
        document.getKeywords().addAll( list );
        
        if( model != null ) {
            List<String> items = model.getKeywords();
            if( items != null ) document.getKeywords().addAll( items );
        }
    }
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public void setName(String name) {
        super.setName(name);
        setText(name);
    }
    
    public String[] getDepends() {
        return depends;
    }
    
    public void setDepends(String[] depends) {
        this.depends = depends;
    }
    
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    
    public void setBinding(Binding binding) {
        this.binding = binding;
    }
    
    public Binding getBinding() {
        return binding;
    }

    public String getKeywordItems() {
        return keywordItems;
    }
    
    public void setKeywordItems(String varItems) {
        this.keywordItems = varItems;
    }
    
    public List<String> getKeywords() {
        if( keywords == null ) keywords = new ArrayList();
        return keywords;
    }
    
    public boolean isDynamic() {
        return dynamic;
    }
    
    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    public void setValue(Object value) {
        setText( value == null? "" : value+"" );
    }

    public boolean isNullWhenEmpty() {
        return nullWhenEmpty;
    }
    
    public void setNullWhenEmpty(boolean nullWhenEmpty) {
        this.nullWhenEmpty = nullWhenEmpty;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
        setFocusable(!readonly);
        setEditable(!readonly);
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setRequestFocus(boolean focus) {
        if( focus ) requestFocus();
    }

    public boolean isImmediate() {
        return false;
    }

    public Object getValue() {
        if( ValueUtil.isEmpty(getText()) && nullWhenEmpty )
            return null;
        
        if( trimSpaceOption != null )
            return trimSpaceOption.trim( getText() );
        
        return getText();
    } 

    public TrimSpaceOption getTrimSpaceOption() {
        return trimSpaceOption;
    }

    public void setTrimSpaceOption(TrimSpaceOption trimSpaceOption) {
        this.trimSpaceOption = trimSpaceOption;
    }
    
    public FormulaEditorModel getModel() {
        return model;
    }

    public void setModel(FormulaEditorModel model) {
        this.model = model;
    } 

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }
    //</editor-fold>
    
}
