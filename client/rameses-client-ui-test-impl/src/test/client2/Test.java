package test.client2;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.framework.UIController;
import com.rameses.rcp.framework.UIControllerPanel;
import javax.swing.UIManager;

/*
 * Test.java
 * JUnit based test
 *
 * Created on June 18, 2010, 2:05 PM
 */

/**
 *
 * @author compaq
 */
public class Test {
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {;}
        
        UIController controller = new SubFormController();
        UIControllerPanel mainPanel = new UIControllerPanel(controller);
        ClientContext.getCurrentContext().getPlatform().showPopup(null, mainPanel, null);
    }
    
}
