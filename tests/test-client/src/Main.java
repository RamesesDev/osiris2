import com.rameses.osiris2.client.OsirisTestPlatform;
import java.util.HashMap;
import java.util.Map;
import javax.swing.UIManager;
/*
 * Main.java
 *
 * Created on July 2, 2010, 2:15 PM
 * @author jaycverg
 */

public class Main {
    
    public static void main(String[] args) throws Exception {        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {;}
        
        Map m = new HashMap();
        m.put("default.host", "10.0.0.104:8080");
        OsirisTestPlatform.runTest(m);
    }

}
