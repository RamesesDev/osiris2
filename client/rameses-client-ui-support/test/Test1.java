import java.beans.IntrospectionException;
import javax.swing.JDialog;
import javax.swing.JTextField;
import junit.framework.*;
import org.openide.explorer.propertysheet.PropertySheet;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Node;
import org.openide.nodes.Node.Property;
import org.openide.nodes.Node.PropertySet;

/*
 * Test1.java
 * JUnit based test
 *
 * Created on January 10, 2011, 2:08 PM
 * @author jaycverg
 */

public class Test1 extends TestCase {
    
    public Test1(String testName) {
        super(testName);
    }
    
    public void testHello() throws IntrospectionException {
        PropertySheet ps = new PropertySheet();
        
        BeanNode n = new BeanNode(new JTextField());
        n.setDisplayName("font");
        ps.setNodes(new Node[]{ n });
        
        JDialog d = new JDialog();
        d.add(ps);
        d.pack();
        d.setModal(true);
        d.setVisible(true);
        
        PropertySet[] pss = n.getPropertySets();
        for(PropertySet p : pss) {
            for(Property pp : p.getProperties()) {
                try {
                    if ( !pp.isDefaultValue() )
                        System.out.println( pp.getName()  );
                    
                } catch (Exception ex) {}
            }
        }
    }
    
}
