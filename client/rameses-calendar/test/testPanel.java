
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 *
 * @author Windhel
 */
public class testPanel extends JPanel{
    
    public testPanel() {
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2.setPaint(Color.GREEN);
        g2.fillRect(10, 0, 100, 50);
        g2.setPaint(Color.YELLOW);
        g2.fillRect(30, 0, 100, 10);
        
    }
}
