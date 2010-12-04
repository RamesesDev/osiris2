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
            String os = System.getProperty("os.name");
            if ( os.toLowerCase().contains("windows") )                
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            else
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            
        } catch (Exception e) {;}
        
        Map m = new HashMap();
        m.put("default.host", "10.0.0.104:8080");
        m.put("app.context", "mlglobal");
        m.put("app.help", "http://10.0.0.104:8080/mlglobal-downloads/help");
        OsirisTestPlatform.runTest(m);
    }
    
}
