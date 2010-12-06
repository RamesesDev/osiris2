/*
 * WebEditorKit.java
 *
 * Created on December 3, 2010, 1:43 PM
 * @author jaycverg
 */

package com.rameses.rcp.control.webbrowser;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLEditorKit.HTMLFactory;

public class WebEditorKit extends HTMLEditorKit {
    
    private String cacheContext;
    
    public WebEditorKit(String context) {
        this.cacheContext = context;
    }

    public Document createDefaultDocument() {
        Document d = super.createDefaultDocument();
        d.putProperty("imageCache", new ImageCache(cacheContext));
        return d;
    }
        
    public ViewFactory getViewFactory() {
        return viewFactory;
    }
    
    
    private ViewFactory viewFactory = new HTMLFactory() {
        
        public View create(Element elem) {
            AttributeSet attrs = elem.getAttributes();
            Object elementName = attrs.getAttribute(AbstractDocument.ElementNameAttribute);
            Object o = (elementName != null) ? null : attrs.getAttribute(StyleConstants.NameAttribute);
            if (o instanceof HTML.Tag) {
                HTML.Tag kind = (HTML.Tag) o;
                if (kind == HTML.Tag.IMG) {
                    return new HTMLImageView(elem, cacheContext);
                }
            }
            return super.create(elem);
        }
        
    };
    
    
}