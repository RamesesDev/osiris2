/*
 * TestController.java
 *
 * Created on June 19, 2010, 10:31 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.client2;

import com.rameses.rcp.common.Opener;
import com.rameses.rcp.framework.UIController;
import com.rameses.rcp.framework.UIController.View;
import com.rameses.rcp.framework.UIViewPanel;
import java.util.Map;

/**
 *
 * @author compaq
 */
public class SubFormController extends UIController {
    
   private Opener opener = new Opener("test.client2.TestController");
   private String companyName;
   private String address;
    
    public SubFormController() {
        super();
    }
    
    public UIController.View[] getViews() {
        return new View[] {
            new View("page1", "test.client2.SubFormPage")
        };
    }
    
    public UIViewPanel getDefaultView() {
        return super.getView("page1");
    }
    
    public Object getCodeBean() {
        return this;
    }

    public Object init(Map params, String action) {
        return null;
    }
    
    
    public Opener getOpener() {
        return opener;
    }

    public void setOpener(Opener opener) {
        this.opener = opener;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    
}
