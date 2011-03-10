
package com.rameses.calendar.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;

/**
 *
 * @author Windhel
 */
public class ScheduleModel extends JComponent {
    
    private static int HEIGHT_BOUNDS = 3;
    
    private Calendar from;
    private Calendar to;
    private String name = "";
    private String description = "";
    private String title = "";
    
    private Rectangle bounds = new Rectangle();
    private Color color;
    
    private boolean pressed = false;
    private boolean resize = false;
    private boolean north = false;
    private Point adjustedPoint;
    private JPanel parent;
    private JComponent thisPanel;
    private int xPos;
    private int yPos;
    private int yOffset;
    private int xOffset;
    
    private int time_gap;
    private String time_start;
    private String time_end;
    private Point currentPoint; // the x,y where the components starts to paint
    private int scheduleSize;
    private Dimension modelDimension;
    
    private Cursor defaultCursor;
    private Cursor resizeSouthCursor;
    private Cursor resizeNorthCursor;
    
    
    //<editor-fold defaultstate="collapsed" desc="  Main Constructor(s) ">
    public ScheduleModel() {
        from = Calendar.getInstance();
        to = Calendar.getInstance();
        defaultCursor = getCursor();
        resizeSouthCursor = new Cursor( Cursor.S_RESIZE_CURSOR );
        resizeNorthCursor = new Cursor( Cursor.N_RESIZE_CURSOR );
        thisPanel = this;
        
        addMouseListener( new MouseSupport() );
        addMouseMotionListener( new MouseMotionSupport() );
    }
    
    public ScheduleModel(String strFrom, String strTo, String title, String desc) {
        this();
        
        ToolTipManager.sharedInstance().registerComponent( this);
        ToolTipManager.sharedInstance().setInitialDelay(0); //initializes the tooltip popup delay time to zero (0)
        setOpaque( false );
        setLayout( new BorderLayout() );
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
            from.setTime( format.parse(strFrom) );
            to.setTime( format.parse(strTo) );
            setTitle( title );
            setDescription( desc );
        } catch(Exception e) { e.printStackTrace(); }
    }
    
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="  Calculations (DROP) ">
    private void validateDrop(Calendar from, Calendar To, int min, int hour) {
        Calendar fromTemp = from;
        Calendar toTemp = to;
        
        fromTemp.add( Calendar.MINUTE, min );
        toTemp.add( Calendar.MINUTE, min );
        fromTemp.add( Calendar.HOUR, hour );
        toTemp.add( Calendar.HOUR, hour );
        
        if(fromTemp.get(Calendar.DAY_OF_MONTH) == toTemp.get(Calendar.DAY_OF_MONTH) || ( toTemp.get(Calendar.HOUR) == 0 && toTemp.get(Calendar.MINUTE) == 0 && fromTemp.get(Calendar.DAY_OF_MONTH) == toTemp.get(Calendar.DAY_OF_MONTH) - 1)) {
            from = fromTemp;
            to = toTemp;
        } else {
            fromTemp.add( Calendar.MINUTE, -(min) );
            toTemp.add( Calendar.MINUTE, -(min) );
            fromTemp.add( Calendar.HOUR, -(hour) );
            toTemp.add( Calendar.HOUR, -(hour) );
        }
    }
    
    private void calculateDrop() {
        //int x;//not used - for the x coordinate
        int y, a, b;
        
        try {
            y = Math.abs( currentPoint.y - adjustedPoint.y );
            a = (int)y/time_gap;
            b = y - (a * time_gap);
            
            if(currentPoint.y < adjustedPoint.y) {
                if( b > 0 && b <= 25 ) validateDrop(from, to, 0, a);
                else if( b > 25 && b <= 50 ) validateDrop(from, to, 15, a);
                else if( b > 50 && b <= 75 ) validateDrop(from, to, 30, a);
                else if( b > 75 && b <= 100 ) validateDrop(from, to, 45, a);
            }else {
                if( b > 0 && b <= 25 ) validateDrop(from, to, 0, -(a));
                else if( b > 25 && b <= 50 ) validateDrop(from, to, -(15), -(a));
                else if( b > 50 && b <= 75 ) validateDrop(from, to, -(30), -(a));
                else if( b > 75 && b <= 100 ) validateDrop(from, to, -(45), -(a));
            }
        }catch(Exception e) {}
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="  Calculations (RESIZE) ">
    private void validateResize(Calendar calendar, int min, int hour) {
        calendar.set( Calendar.MINUTE, min);
        calendar.set( Calendar.HOUR_OF_DAY, hour);
    }
    
    private void calculateResize(Calendar calendar) {
        int y, a, b;
        
        try {
            y = Math.abs( adjustedPoint.y );
            a = (int)y/time_gap;
            b = y - (a * time_gap);
            
            if(b % 15 != 0 ) return;
            
            if( b > 0 &&b <= 25) validateResize(calendar, 0, a);
            else if(b > 25 && b <= 50 ) validateResize(calendar, 15, a);
            else if(b > 50 && b <= 75 ) validateResize(calendar, 30, a);
            else if(b > 75 && b <= 100 ) validateResize(calendar,  45, a);
            
        }catch(Exception e) {}
    }
//</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="  Helper Methods ">
    public void supplyTimeData(int scheduleSize, int time_gap, String time_start, String time_end, Point currentPoint, Dimension modelDimension) {
        this.time_gap = time_gap;
        this.scheduleSize = scheduleSize;
        this.time_start = time_start;
        this.time_end = time_end;
        this.currentPoint = currentPoint;
        this.modelDimension = modelDimension;
    }
    
    public void init(Point start, Dimension size, Color color) {
        this.color = color;
        bounds.setLocation( start );
        bounds.setSize( size );
        
        setBorder( BorderFactory.createEmptyBorder() );
        setLocation( bounds.x, bounds.y );
        //setPreferredSize( new Dimension( bounds.width, bounds.height ));
        setSize( new Dimension( bounds.width, bounds.height ) );
    }
    
    public void setToLocation(Graphics2D g2) {
        if(! pressed) {
            g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
            g2.setPaint( color );
            g2.fillRect( bounds.x, bounds.y, bounds.width, bounds.height );
            g2.setPaint( color.darker() );
            g2.drawRect( bounds.x, bounds.y, bounds.width, bounds.height );
        } else {
            setLocation( xPos, yPos );
            g2.fillRect( xPos, yPos, bounds.width, bounds.height );
        }
    }
    
    private void determineMousePosition(int x, int y, boolean determineNow) {
        adjustedPoint = SwingUtilities.convertPoint(thisPanel, x, y, parent);
        xPos = adjustedPoint.x;
        yPos = adjustedPoint.y;
        
        if(determineNow) {
            adjustedPoint.x = xPos;
            adjustedPoint.y = yPos;
        }
    }
    
    private int getStringLineMetric(String str) {
        return getFontMetrics(getFont()).charsWidth(str.toCharArray(), 0, str.length());
    }
    
    public String addOneZero(int i) {
        String str = i + "";
        if(str.length() == 1) return "0" + i;
        
        return str;
    }
    
    public String AM_PM(int i) {
        if(i == 0) return "AM";
        
        return "PM";
    }
    
    public void renderTooltip() {
        setToolTipText( htmlWrapper(getTitle(), getDescription()) );
    }
    
    public String htmlWrapper(String title, String desc) {
        StringBuffer sb = new StringBuffer();
        sb.append( "<html>" );
        sb.append( "<b>" + title + "</b><br>");
        sb.append( desc + "<br> </html>");
        
        return sb.toString();
    }
    
    
//</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="  Setter/Getter ">
    public int getFromHr() { return from.get( Calendar.HOUR_OF_DAY ); }
    
    public int getFromMin() { return from.get( Calendar.MINUTE ); }
    
    public int getFromSec() { return from.get( Calendar.SECOND ); }
    
    public int getFromYr() { return from.get( Calendar.YEAR ); }
    
    public int getFromMon() { return from.get( Calendar.MONTH ); }
    
    public int getFromDay() { return from.get( Calendar.DAY_OF_MONTH ); }
    
    public int getToHr() { return to.get( Calendar.HOUR_OF_DAY ); }
    
    public int getToMin() { return to.get( Calendar.MINUTE ); }
    
    public int getToSec() { return to.get( Calendar.SECOND ); }
    
    public int getToYr() { return to.get( Calendar.YEAR ); }
    
    public int getToMon() { return to.get( Calendar.MONTH ); }
    
    public int getToDay() { return to.get( Calendar.DAY_OF_MONTH ); }
    
    public String getName() { return name; }
    
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    
    public void setDescription(String desc) { this.description = desc; }
    
    public void setTitle(String title) { this.title = title; }
    
    public void setParent(JPanel panel) { this.parent = panel; }
    
    public String getTitle() {
        StringBuffer sb = new StringBuffer();
        sb.append(" | ");
        //sb.append(from.get(Calendar.YEAR) + "/" + addOneZero( (from.get(Calendar.MONTH) + 1)) + "/" + addOneZero( from.get(Calendar.DAY_OF_MONTH)));
        sb.append(addOneZero(from.get(Calendar.HOUR_OF_DAY)) + ":" + addOneZero(from.get(Calendar.MINUTE)) + " " + AM_PM(from.get(Calendar.AM_PM)));
        sb.append(" to ");
        //sb.append(to.get(Calendar.YEAR) + "/" + addOneZero( (to.get(Calendar.MONTH) + 1)) + "/" + addOneZero( to.get(Calendar.DAY_OF_MONTH)));
        sb.append(addOneZero(to.get(Calendar.HOUR_OF_DAY)) + ":" + addOneZero(to.get(Calendar.MINUTE)) + " " + AM_PM(to.get(Calendar.AM_PM)));
        return title + sb.toString();
    }
    
    public Point getToolTipLocation(MouseEvent e) { //handles the location of the tooltip
        return new Point( e.getX() + 20, e.getY() );
    }
    
//</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="  MouseSupport ">
    private class MouseSupport extends MouseAdapter {
        public void mouseExited(MouseEvent e) { setCursor( defaultCursor ); }
        
        public void mousePressed(MouseEvent e) {
            if(e.getY() < HEIGHT_BOUNDS) {
                setCursor( resizeNorthCursor );
                resize = true;
                north = true;
            } else if(e.getY() > bounds.height - HEIGHT_BOUNDS) {
                setCursor( resizeSouthCursor );
                resize = true;
                north = false;
            } else {
                xOffset = e.getX();
                yOffset = e.getY();
                determineMousePosition(e.getX() - xOffset, e.getY() - yOffset, false);
                pressed = true;
            }
        }
        
        public void mouseReleased(MouseEvent e) {
            determineMousePosition(e.getX() - xOffset, e.getY() - yOffset, true);
            setCursor( defaultCursor );
            
            if(pressed)
                calculateDrop();
            
            pressed = false;
            resize = false;
            renderTooltip();
            repaint();
            parent.repaint();
        }
    }
    
//</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="  MouseMotionSupport ">
    private class MouseMotionSupport extends MouseMotionAdapter {
        public void mouseDragged(MouseEvent e) {
            determineMousePosition(e.getX() - xOffset, e.getY() - yOffset, true);
            if(resize) {
                if(north) calculateResize(from);
                else calculateResize(to);
            }
            repaint();
            parent.repaint();
        }
    }
//</editor-fold>
}
