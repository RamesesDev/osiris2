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
            UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {;}
        
        Map m = new HashMap();
        m.put("default.host", "10.0.0.104:8080");
        //m.put("default.host", "localhost:8080");
        OsirisTestPlatform.runTest(m);
    }
    
//    public static class OsirisLookAndFeel extends PlasticXPLookAndFeel {
//        
//        public OsirisLookAndFeel() {
//            super();
//            UIManager.put("Table.evenBackground", Color.WHITE);
//            UIManager.put("Table.oddBackground", Color.decode("#e3e3e3"));
//        }
//        
//    }
}
