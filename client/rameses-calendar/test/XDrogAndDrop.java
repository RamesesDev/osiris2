import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * 20100105
 * @Windhel
 */
public class XDrogAndDrop extends JScrollPane {
    
    private static int SIZE = 1;
    private MainPanel mainPanel = new MainPanel();
    
    private List<MovablePanel> panel1 = new ArrayList();
    private JLabel label1 = new JLabel("Task 1");
    private JLabel label2 = new JLabel("Task 2");
    
    public XDrogAndDrop() {
        mainPanel.setPreferredSize(new Dimension( 480,300 ));
        for(int i=0 ; i<SIZE ; i++) {
            panel1.add( new MovablePanel() );
            
            //mainPanel.addMouseMotionListener( new MouseMotionSupport(mp) );
            mainPanel.add( panel1.get(i) );
        }
        
        setViewportView( mainPanel );
    }
    
    private class MainPanel extends JPanel {
        
        public MainPanel() {
            addMouseListener(new MouseListener() {
                public void mouseClicked(MouseEvent e) {
                    
                    for(int i=0 ; i<SIZE ; i++) {
                        JComponent p = panel1.get(i);
                        
                        if(panel1.get(i).containInPoint( panel1.get(i).getBounds(), e.getPoint() )){
                            
                            System.out.println("> " + p.getX());
                            System.out.println(">> " + p.getY());
                            System.out.println("ll");
                        } else {
                            //System.out.println("> " + p.getX());
                            //System.out.println(">> " + p.getY());
                        }
                    }
                    
                }
                public void mouseEntered(MouseEvent e) {
                }
                public void mouseExited(MouseEvent e) {
                }
                public void mousePressed(MouseEvent e) {
                }
                public void mouseReleased(MouseEvent e) {
                }
            });
            addMouseMotionListener(new MouseMotionListener() {
                public void mouseDragged(MouseEvent e) {
                }
                public void mouseMoved(MouseEvent e) {
                }
            });
        }
        
    }
    
    private class MovablePanel extends JComponent {
        
        private Rectangle rec;
        
        public MovablePanel() {
            setSize(new Dimension( 50,50 ));
            
            setBorder( BorderFactory.createLineBorder( Color.RED ));
            rec = new Rectangle( getX(), getY(), getWidth(), getHeight() );
            
            System.out.println("x" + rec.x);
            System.out.println("y" + rec.y);
            System.out.println("width " + rec.width);
            System.out.println("height " + rec.height);
        }
        
        public boolean containInPoint(Rectangle rec, Point point) {
            this.rec = rec;
            if(rec.contains( point ))
                return true;
            else
                return false;
        }
    }
    
    private class MouseMotionSupport implements MouseMotionListener {
        
        private Point point;
        private JPanel panel;
        
        public MouseMotionSupport(JPanel panel) {
            this.panel = panel;
        }
        
        public void mouseDragged(MouseEvent e) {
            //point = SwingUtilities.convertPoint(panel, e.getX(), e.getY(), panel.getParent());
            // panel.setBounds(point.x, point.y, panel.getWidth(), panel.getHeight());
            panel.setBounds(e.getX(), e.getY(), panel.getWidth(), panel.getHeight());
        }
        
        public void mouseMoved(MouseEvent e) {
        }
        
    }
}
