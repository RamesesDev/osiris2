/*
 * TestController.java
 *
 * Created on June 19, 2010, 10:31 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.client;

import com.rameses.rcp.framework.UIController;
import com.rameses.rcp.framework.UIController.View;
import com.rameses.rcp.framework.UIViewPanel;
import java.util.Map;

/**
 *
 * @author compaq
 */
public class SecondController extends UIController {
    
    public SecondController() {
        super();
    }

    public UIController.View[] getViews() {
        return new View[] {
            new View("page1", "test.client.SecondControllerPage")
        };
    }

    public UIViewPanel getDefaultView() {
        return super.getView("page1");
    }

    public Object getCodeBean() {
        return this;
    }
    
    public String close() {
        return "_close";
    }

    public Object init(Map params, String action) {
        return null;
    }
}
