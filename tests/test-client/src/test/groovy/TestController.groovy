package test.groovy;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;


public class TestController  {

    void fireAction() {
        MsgBox.alert('Action from external controller.');
    }

}