/*
 * TestController.java
 *
 * Created on June 19, 2010, 10:31 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.client;

import com.rameses.rcp.common.MsgBox;
import com.rameses.rcp.common.Opener;
import com.rameses.rcp.framework.UIController;
import com.rameses.rcp.framework.UIController.View;
import com.rameses.rcp.framework.UIViewPanel;
import java.util.Map;
import test.model.EmployeeType;

/**
 *
 * @author compaq
 */
public class TestController extends UIController {
    
    private String firstname;
    private String lastname;
    private String address;
    private boolean single;
    private EmployeeType employeeType;
    
    public TestController() {
        super();
    }
    
    public UIController.View[] getViews() {
        return new View[] {
            new View("page1", "test.client.TestPage"),
            new View("page2", "test.client.TestPage2")
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
    
    public String next() {
        return "page2";
    }
    
    public Object launch() {
        return new Opener("test.client.SecondController");
    }
    
    public Object launch2() {
        return new Opener("test.client.ThirdController");
    }
    
    public String back() {
        return "page1";
    }
    
    public void greet() {
        MsgBox.alert("Hi " + getFullname());
        System.out.println("single is " + single);
        System.out.println("employee type is " + employeeType);
    }
    
    public String getFullname() {
        return (firstname == null? "" : firstname + " ") + (lastname == null? "": lastname);
    }
    
    public String getFirstname() {
        return firstname;
    }
    
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    
    public String getLastname() {
        return lastname;
    }
    
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public boolean isSingle() {
        return single;
    }
    
    public void setSingle(boolean single) {
        this.single = single;
    }
    
    public EmployeeType getEmployeeType() {
        return employeeType;
    }
    
    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }
    
}



