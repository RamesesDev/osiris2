/*
 * XEditorPane.java
 *
 * Created on October 6, 2010, 9:39 AM
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.rcp.common.MsgBox;
import com.rameses.rcp.common.WebBrowserModel;
import com.rameses.rcp.control.webbrowser.WebEditorKit;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.util.ValueUtil;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.Beans;
import java.net.URL;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.KeyStroke;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;

public class XWebBrowser extends JEditorPane implements UIControl {
    
    private Binding binding;
    private String[] depends;
    private int index;
    private boolean refreshed;
    
    private WebBrowserModel model;
    
    
    public XWebBrowser() {
        super();
        
        if ( Beans.isDesignTime() ) {
            setContentType("text/html");
        }
        
        super.setEditable(false);
        
        attachEventsListeners();
    }
    
    //<editor-fold defaultstate="collapsed" desc="  helper methods  ">
    private void attachEventsListeners() {
        addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                processHyperlinkEvent(e);
            }
        });
        
        registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ( model != null ) model.back();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), JComponent.WHEN_FOCUSED);
        
        registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ( model != null ) model.forward();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 1), JComponent.WHEN_FOCUSED);
    }
    
    private void processHyperlinkEvent(HyperlinkEvent e) {
        EventType evt = e.getEventType();
        if (evt == EventType.ACTIVATED) {
            try {
                String desc = e.getDescription();
                if ( !ValueUtil.isEmpty(desc) )  {
                    desc = desc.trim();
                    //process reference
                    if ( desc.startsWith("#") ) {
                        if ( desc.length() > 1)
                            model.setLocation( desc );
                    }
                    //process url link
                    else {
                        if ( desc.startsWith("http") || desc.startsWith("www.") ) {
                            model.setLocation( desc );
                        } else {
                            model.setRelativeLocation( desc );
                        }
                    }
                }
            } catch(Exception ex){
                MsgBox.err(new IllegalStateException(ex));
            }
        }
    }
    //</editor-fold>
    
    
    public void refresh() {
        //refresh only on the first display
        //next refresh can be done in the model (model.refresh())
        if( !refreshed ) {
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    refreshContent();
                }
            });
        }
    }
    
    public void load() {
        model = (WebBrowserModel) UIControlUtil.getBeanValue(this);
        setEditorKit(new WebEditorKit( model.getCacheContext() ));
        model.setListener(new WebBrowserModel.Listener() {
            
            public void refresh() {
                refreshContent();
            }
            
        });
    }
    
    private void refreshContent() {
        try {
            if ( ValueUtil.isEqual(super.getPage(), model.getLocation()) ) {
                //force reload
                Document doc = getDocument();
                doc.putProperty(Document.StreamDescriptionProperty, null);
            }
            
            URL currentLoc = super.getPage();
            URL newLoc = model.getLocation();
            String extForm = newLoc.toExternalForm();
            int hashIdx = -1;
            
            if ( (hashIdx = extForm.indexOf("#")) != -1 ) {
                String[] ss = extForm.split("#");
                if ( currentLoc != null && currentLoc.toExternalForm().split("#")[0].equals(ss[0]) )
                    super.scrollToReference( ss[1] );
                else
                    super.setPage( newLoc );
                
            } else {
                super.setPage( newLoc );
            }
            
            binding.notifyDepends(this);
            
        } catch (Exception ex) {
            MsgBox.err(ex);
        }
        refreshed = true;
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
    
    public void setEditable(boolean editable) {}
    
    public String[] getDepends() { return depends; }
    public void setDepends(String[] depends) { this.depends = depends; }
    
    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }
    
    public void setBinding(Binding binding) { this.binding = binding; }
    public Binding getBinding() { return binding; }
    //</editor-fold>
    
}
