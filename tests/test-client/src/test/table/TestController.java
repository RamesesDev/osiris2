/*
 * TestController.java
 *
 * Created on June 19, 2010, 10:31 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.table;

import com.rameses.rcp.common.Column;
import com.rameses.rcp.common.PageListModel;
import com.rameses.rcp.framework.UIController;
import com.rameses.rcp.framework.UIController.View;
import com.rameses.rcp.framework.UIViewPanel;
import java.util.List;
import java.util.Map;
import test.ServiceDelegate;

/**
 *
 * @author compaq
 */
public class TestController extends UIController {
    
    private Object handler = new TestHandler();
    
    
    public TestController() {
        super();
    }
    
    public UIController.View[] getViews() {
        return new View[] {
            new View("page1", "test.client3.TestPage"),
            new View("page2", "test.client3.TestPage2")
        };
    }
    
    public UIViewPanel getDefaultView() {
        return super.getView("page2");
    }
    
    public Object getCodeBean() {
        return this;
    }
    
    public Object init(Map params, String action) {
        return null;
    }
    
    
    public Object getHandler() { return handler; }
    
    public static class TestHandler extends PageListModel {
        
        
        public TestHandler() {}
        
        public int getRows() { return 20; }
        
        public List fetchList(Map o) {
            return ServiceDelegate.getList(o);
        }
        
        public Column[] getColumns() {
            return new Column[]{
                new Column("rownum", "#"),
                new Column("selected", "selected", "boolean", true, false),
                new Column("item.objid", "Id", "string", null, true),
                new Column("item.name", "Name", "string", true, true),
                new Column("item.position", "Position", "string", null, true),
                new Column("item.lguname", "LGU", "string", null)
            };
        }
        
    }
    
}
