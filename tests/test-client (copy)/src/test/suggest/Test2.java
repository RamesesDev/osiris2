/*
 * Test2.java
 *
 * Created on August 31, 2010, 9:59 AM
 * @author jaycverg
 */

package test.suggest;

import com.rameses.rcp.control.XSuggest;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Test2 {
    
    public static void main(String[] args) {
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        
        XSuggest s = new XSuggest();
        p.add(s);
        p.add(new JTextField("test text field1"));
        
        s.load();
        
        JFrame f = new JFrame("Test");
        f.setContentPane(p);
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }    
    
}
