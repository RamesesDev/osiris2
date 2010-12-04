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
import java.beans.Beans;
import java.net.URL;
import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;

public class XWebBrowser extends JEditorPane implements UIControl {
    
    private Binding binding;
    private String[] depends;
    private int index;
    private boolean refreshed;
    
    private WebBrowserModel model;
    private HTMLDocument document;
    
    
    public XWebBrowser() {
        super();
        
        if ( Beans.isDesignTime() ) {
            setContentType("text/html");
        } else {
            setEditorKit(new WebEditorKit());
        }
        
        super.setEditable(false);
        
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
                    if ( desc.startsWith("/") ) {
                        URL u = new URL( model.getBaseUrl() );
                        String domain = u.getProtocol()+"://"+u.getHost() + ( u.getPort() > -1? ":" + u.getPort() : "");
                        model.setLocation( domain + desc );
                    } else {
                        model.setRelativeLocation( desc );
                    }
                    binding.notifyDepends(this);
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
            refreshContent();
        }
    }
    
    public void load() {
        document = (HTMLDocument) getDocument();
        model = (WebBrowserModel) UIControlUtil.getBeanValue(this);
        model.setListener(new WebBrowserModel.Listener() {
            
            public void refresh() {
                refreshContent();
            }
            
        });
    }
    
    private void refreshContent() {
        try {
            document.setBase( new URL(model.getBaseUrl()) );
            super.setPage( model.getLocation() );
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
