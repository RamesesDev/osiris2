/*
 * CustomerImageView.java
 *
 * Created on December 3, 2010, 3:31 PM
 * @author jaycverg
 */

package com.rameses.rcp.control.webbrowser;

import com.rameses.rcp.util.ControlSupport;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.text.Element;
import javax.swing.text.html.ImageView;


class HTMLImageView extends ImageView {
    
    private String context;
    private ImageIcon loadingIcon;
    
    
    HTMLImageView(Element elem, String context) {
        super(elem);
        this.context = context;
        loadingIcon = ControlSupport.getImageIcon("com/rameses/rcp/icons/loading32.gif");
    }
    
    public Icon getLoadingImageIcon() {
        return loadingIcon;
    }

}