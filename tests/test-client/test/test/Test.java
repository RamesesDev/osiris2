package test;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.UIManager;
import junit.framework.*;

/*
 * Test.java
 * JUnit based test
 *
 * Created on June 22, 2010, 4:17 PM
 */

/**
 *
 * @author compaq
 */
public class Test extends TestCase {
    
    public Test(String testName) {
        super(testName);
    }
    
//    public void test() throws ParseException {
//        System.out.println("#eef433".matches("#[a-f\\d]{3,6}") );
//    }
    

    
//    public void test2() throws ParseException {
//        JDialog d = new JDialog();
//        d.setModal(true);
//        d.setContentPane(new TestPage());
//        d.pack();
//        d.setLocationRelativeTo(null);
//        d.setVisible(true);
//        
//    }
    
    public void test3() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {;}
        
        JDialog f = new JDialog();
        f.getContentPane().setLayout(new FlowLayout());
        
        JButton b = new JButton("Button");
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                javax.swing.JOptionPane.showMessageDialog(null, "clicked.");
            }
        });
        f.getContentPane().add( b );
        f.getContentPane().add( new JTextField("TextField") );
        
        f.setSize(new Dimension(500, 200));
        f.setGlassPane( new GPPanel() );
        f.getGlassPane().setVisible(true);
        f.getContentPane().setEnabled(false);
        f.setModal(true);
        f.setVisible(true);
    }
    
    private class GPPanel extends JComponent {
        
        GPPanel() {
            setFocusable(true);
            setOpaque(false);
            addFocusListener(new FocusListener() {
                public void focusGained(FocusEvent e) {}
                public void focusLost(FocusEvent e) {
                    grabFocus();
                }
            });
        }

        public void setVisible(boolean aFlag) {
            super.setVisible(aFlag);
            grabFocus();
        }

        public void paint(Graphics g) {
            super.paint(g);
            
            Graphics2D g2 = (Graphics2D) g.create();
            Color bg = new Color(0, 0, 0, 30);
            g2.setColor( bg );
            
            Dimension d = getSize();
            g2.fillRect(0, 0, d.width, d.height);
            
            g2.dispose();
        }
        
    }
    
}
