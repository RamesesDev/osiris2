
import com.rameses.util.ValueUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 *
 * @author Windhel
 */
public class ScheduleObject extends JPanel {
    
    private Rectangle bounds = new Rectangle();
    private Rectangle smallBox = new Rectangle();
    private Color color;
    private String name = "";
    private String title = "";
    private String description = "";
    private JPopupMenu popup = new JPopupMenu();
    private JPanel panel = new JPanel();
    private JLabel lblDescription = new JLabel();
    private JLabel lblTitle = new JLabel();
    
    public ScheduleObject() {
        lblTitle.setText( "TITLE" );
        lblDescription.setText( "DESCRIPTION" );
        lblTitle.setHorizontalAlignment( JLabel.CENTER );
        panel.setLayout( new BorderLayout() );
        panel.add( lblTitle, BorderLayout.NORTH );
        panel.add( lblDescription, BorderLayout.CENTER );
        popup.add( panel );
    }
    
    public void show(int x, int y) {
        lblDescription.setText( description );
        lblTitle.setText( title );
        popup.show(this, x, y);
        popup.setVisible(true);
    }
    
    public void hide() {
        popup.setVisible(false);
    }
    
    public void init(Point start, Dimension size, Color color) {
        this.color = color;
        bounds.setLocation( start );
        bounds.setSize( size );
        smallBox.setLocation( bounds.x + bounds.width - 15, bounds.y + 10 );
        smallBox.setSize( 10, 10 );
    }
    
    public Color darker(Color c) {
        return c.darker();
    }
    
    public void draw(Graphics2D g, boolean onHover) {
        g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        
        g.setPaint( color );
        g.fillRect( bounds.x, bounds.y, bounds.width, bounds.height );
        g.setColor( darker(color) );
        g.drawRect( bounds.x, bounds.y, bounds.width, bounds.height );
        
        int x = bounds.x + bounds.width - 15;
        int y = bounds.y + 10;
        Polygon polyLeft = new Polygon();
        polyLeft.addPoint( x + 3, y + 1);
        polyLeft.addPoint( x + 9, y + 5 );
        polyLeft.addPoint( x + 3, y + 9 );
        Polygon polyDown = new Polygon();
        polyDown.addPoint( x + 2, y + 2);
        polyDown.addPoint( x + 9, y + 2 );
        polyDown.addPoint( x + 5, y + 9 );
        
        if(onHover)
            g.setColor( Color.BLUE );
        else
            g.setColor( Color.BLACK );
        
        g.fillRect( x, y, 10, 10 );
        g.setColor( Color.GREEN );
        g.drawRect( x, y, 10, 10 );
        
        if(onHover)
            g.fillPolygon( polyLeft );
        else
            g.fillPolygon( polyDown );
        
        if(! ValueUtil.isEmpty( name )) {
            g.setColor(Color.BLACK);
            g.drawString( name, bounds.x, bounds.y);
        }
        
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="  Setter/Getter ">
    public Point getLocation() {
        return bounds.getLocation();
    }
    
    public Dimension getSize() {
        return bounds.getSize();
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public boolean contains(Point p) {
        return bounds.contains(p);
    }
    
    public boolean containsInSmallBox(Point p) {
        return smallBox.contains(p);
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    //</editor-fold>
    
    private class MouseMotionSupport extends MouseMotionAdapter {
        //public void mouseDragged(MouseEvent e) {}
        
        public void mouseMoved(MouseEvent e) {
            //floatingPanel.setLocation( e.getX() + 20, e.getY() );
            System.out.println(">> " + e.getX() );
            System.out.println(">> " + e.getY() );
        }
        
    }
}
