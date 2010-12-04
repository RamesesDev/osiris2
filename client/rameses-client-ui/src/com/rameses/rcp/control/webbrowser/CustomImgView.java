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


class CustomImgView extends ImageView {
    
    private ImageIcon loadingIcon;
    
    
    CustomImgView(Element elem) {
        super(elem);
        loadingIcon = ControlSupport.getImageIcon("com/rameses/rcp/icons/loading32.gif");
    }
    
    public Icon getLoadingImageIcon() {
        return loadingIcon;
    }

    public URL getImageURL() {
        URL u = super.getImageURL();
        try {
            return CacheUtil.getCache(u, "IMG");
            
        } catch(Exception e) {
            e.printStackTrace();
        }
        return u;
    }
    
}