package test.error;
import com.rameses.rcp.common.MsgBox;
import com.rameses.util.ExceptionHandler;
/*
 * TestErrorHandler.java
 *
 * Created on August 24, 2010, 5:08 PM
 * @author jaycverg
 */

public class TestErrorHandler extends ExceptionHandler {
    
    public TestErrorHandler() {
    }
    
    public boolean accept(Exception e) {
        if ( e == null ) return false;
        if ( e.getMessage() == null ) return false;
        if ( e.getMessage().startsWith("session") ) {
            MsgBox.alert("Please re-login.");
            return true;
        }
        return false;
    }
    
}
